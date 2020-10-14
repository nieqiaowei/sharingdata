package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.JsonUtils;
import com.caits.lbs.framework.utils.MD5;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiConsumerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiDataProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiUserInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.Consumeruser;
import com.palmgo.com.cn.sharingdata.bean.DataProducer;
import com.palmgo.com.cn.sharingdata.bean.DataRoleSource;
import com.palmgo.com.cn.sharingdata.bean.DataSourceMapper;
import com.palmgo.com.cn.sharingdata.bean.DataSourceTables;
import com.palmgo.com.cn.sharingdata.bean.RecevierDataInfo;
import com.palmgo.com.cn.sharingdata.conf.Config;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.DataRoleSourcedao;
import com.palmgo.com.cn.sharingdata.dao.Layerdao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.ConsumeruserService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataProducerService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataRoleService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceService;
import com.palmgo.com.cn.sharingdata.service.Interface.LayerService;
import com.palmgo.com.cn.sharingdata.service.Interface.MybatisService;
import com.palmgo.com.cn.sharingdata.service.Interface.ProduceruserService;
import com.palmgo.com.cn.sharingdata.service.Interface.UserService;
import com.palmgo.com.cn.sharingdata.util.DateUitls;

/**
 * 用户相关操作
 *
 * @author Administrator
 */
@Service
public class LayerServiceImpl extends Config implements LayerService {

    public Logger log = CommonLogFactory.getLog();

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @Autowired
    private DataRoleSourcedao dataRoleSourcedao;

    @Autowired
    private DataProducerService dataProducerService;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public ApiInfoResponse recevier(String code, String type, String loginName, String dataBody) throws Exception {
        // TODO Auto-generated method stub
        ApiInfoResponse data = new ApiInfoResponse();
        try {
            //数据接入参数
            Map<String, Object> dataBodyMap = JsonUtils.getMapFromJsonString(dataBody);
            ApiUserInfoResponse apiUserInfoResponse = userService.UserAll(null, loginName, null, null,null, 0, 0);
            if (apiUserInfoResponse.getCode() == MsgCode.code_200) {
                String Roleid = apiUserInfoResponse.getData().get(0).getRoleid();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("roleid", Roleid);
                //查询角色id对应的页面id（每一个业务可以理解为视图）
                List<DataRoleSource> dataRoleSources = dataRoleSourcedao.getRoleToDataSourceInfo(paramMap);
                if (dataRoleSources != null) {
                    String DataProducerid = null;
                    String CountSQL = null;
                    String SelectSQL = null;
                    String InsertSQL = null;
                    String UpadateSQL = null;
                    String DeleteSQL = null;
                    boolean codeOK = false;
                    for (DataRoleSource dataRoleSource : dataRoleSources) {
                        log.info("数据源id：" + dataRoleSource.getDatasourceroleid());
                        ApiDataProducerInfoResponse apiDataProducerInfoResponse = dataProducerService.info(dataRoleSource.getDatasourceroleid(), null, null, null, null, null, 0, 0, loginName);
                        if (apiDataProducerInfoResponse.getCode() == MsgCode.code_200) {
                            if (apiDataProducerInfoResponse.getData().get(0).getCode().equals(code)) {
                                DataProducer dataProducer = apiDataProducerInfoResponse.getData().get(0);
                                DataProducerid = dataProducer.getDatasourceid();
                                CountSQL = dataProducer.getCountSql();
                                SelectSQL = dataProducer.getSelectSql();
                                InsertSQL = dataProducer.getInsertSql();
                                UpadateSQL = dataProducer.getUpdateSql();
                                DeleteSQL = dataProducer.getDeleteSql();
                                codeOK = true;
                                break;
                            } else {
                                log.info("数据源id：" + dataRoleSource.getDatasourceroleid() + "业务代码：" + apiDataProducerInfoResponse.getData().get(0).getCode() + "传入的代码：" + code);
                            }
                        } else {
                            log.info("数据源id：" + dataRoleSource.getDatasourceroleid() + "返回状态：" + apiDataProducerInfoResponse.getCode());
                        }
                    }
                    if (codeOK) {
                        //通过id查找数据源
                        Map<String, DataSourceMapper> map = dataSourceService.getsource(loginName);
                        if (map.containsKey(DataProducerid)) {
                            DataSourceMapper dataSourceMapper = map.get(DataProducerid);
                            SqlMapper sqlMapper = dataSourceMapper.getDataSessionAndMapper().getSqlMapper();
                            switch (type.toUpperCase()) {
                                case "SELECT":
                                    if (StringUtils.notNullOrBlank(SelectSQL)) {
                                        long total = 0;
                                        if (StringUtils.notNullOrBlank(CountSQL)) {
                                            Map<String, Object> Countmap = null;
                                            try {
                                                Countmap = sqlMapper.selectOne(sqlMapper.getNewSql(CountSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                            } catch (Exception e) {
                                                log.error(e.getLocalizedMessage(), e);
                                                //重新连接数据源
                                                dataSourceService.init(DataProducerid, loginName);
                                                dataSourceMapper = dataSourceService.getsource(loginName).get(DataProducerid);
                                                sqlMapper = dataSourceMapper.getDataSessionAndMapper().getSqlMapper();
                                                Countmap = sqlMapper.selectOne(sqlMapper.getNewSql(CountSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                            }
                                            total = (long) Countmap.get("total");
                                        }
                                        List<Map<String, Object>> infos = sqlMapper.selectList(sqlMapper.getNewSql(SelectSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                        if (infos.size() > 0) {
                                            data.setData(infos);
                                            data.setTotal(total);
                                            data.setCode(MsgCode.code_200);
                                        } else {
                                            data.setCode(MsgCode.code_404);
                                        }
                                    }else{
                                        data.setCode(MsgCode.code_219);
                                        data.setMsg(MsgCode.msg_219);
                                    }
                                    break;
                                case "INSERT":
                                    if (StringUtils.notNullOrBlank(InsertSQL)) {
                                        try {
                                            sqlMapper.insert(sqlMapper.getNewSql(InsertSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                        } catch (Exception e) {
                                            log.error(e.getLocalizedMessage(), e);
                                            //重新连接数据源
                                            dataSourceService.init(DataProducerid, loginName);
                                            dataSourceMapper = dataSourceService.getsource(loginName).get(DataProducerid);
                                            sqlMapper = dataSourceMapper.getDataSessionAndMapper().getSqlMapper();
                                            sqlMapper.insert(sqlMapper.getNewSql(InsertSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                        }
                                        data.setCode(MsgCode.code_200);
                                    }else{
                                        data.setCode(MsgCode.code_219);
                                        data.setMsg(MsgCode.msg_219);
                                    }
                                    break;
                                case "UPDATE":
                                    if (StringUtils.notNullOrBlank(UpadateSQL)) {
                                        try {
                                            sqlMapper.update(sqlMapper.getNewSql(UpadateSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                        } catch (Exception e) {
                                            log.error(e.getLocalizedMessage(), e);
                                            //重新连接数据源
                                            dataSourceService.init(DataProducerid, loginName);
                                            dataSourceMapper = dataSourceService.getsource(loginName).get(DataProducerid);
                                            sqlMapper = dataSourceMapper.getDataSessionAndMapper().getSqlMapper();
                                            sqlMapper.update(sqlMapper.getNewSql(UpadateSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                        }
                                        data.setCode(MsgCode.code_200);
                                    }else{
                                        data.setCode(MsgCode.code_219);
                                        data.setMsg(MsgCode.msg_219);
                                    }
                                    break;
                                case "DELETE":
                                    if (StringUtils.notNullOrBlank(DeleteSQL)) {
                                        try {
                                            sqlMapper.delete(sqlMapper.getNewSql(DeleteSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                        } catch (Exception e) {
                                            log.error(e.getLocalizedMessage(), e);
                                            //重新连接数据源
                                            dataSourceService.init(DataProducerid, loginName);
                                            dataSourceMapper = dataSourceService.getsource(loginName).get(DataProducerid);
                                            sqlMapper = dataSourceMapper.getDataSessionAndMapper().getSqlMapper();
                                            sqlMapper.delete(sqlMapper.getNewSql(DeleteSQL,dataSourceMapper.getDataSource().getRemotejdbc_url()), dataBodyMap);
                                        }
                                        data.setCode(MsgCode.code_200);
                                    }else{
                                        data.setCode(MsgCode.code_219);
                                        data.setMsg(MsgCode.msg_219);
                                    }
                                    break;
                                default:
                                    data.setCode(MsgCode.code_217);
                                    break;
                            }
                        } else {
                            data.setCode(MsgCode.code_218);
                        }

                    } else {
                        data.setCode(MsgCode.code_216);
                    }
                } else {
                    data.setCode(MsgCode.code_216);
                }
            } else {
                data.setCode(apiUserInfoResponse.getCode());
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            data.setCode(MsgCode.code_500);
            data.setMsg(e.getLocalizedMessage() + e);
        }
        return data;
    }

}
