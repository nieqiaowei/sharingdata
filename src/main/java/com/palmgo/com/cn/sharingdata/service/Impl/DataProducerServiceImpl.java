package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiDataProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiSystemInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.DataProducer;
import com.palmgo.com.cn.sharingdata.bean.DataSourceMapper;
import com.palmgo.com.cn.sharingdata.bean.SystemProPerties;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.DataProducerdao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataProducerService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceService;
import com.palmgo.com.cn.sharingdata.service.Interface.SystemService;
import com.palmgo.com.cn.sharingdata.util.DateUitls;

@Service
public class DataProducerServiceImpl implements DataProducerService {

    public Logger log = CommonLogFactory.getLog();

    @Autowired
    private DataProducerdao dao;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private CommonService commonService;

    @Override
    public ApiDataProducerInfoResponse info(String id, String name, String code, String tableName, String datasourceid, String type,
                                            int pageIndex, int totalPerPage, String loginName) throws Exception {
        // TODO Auto-generated method stub
        ApiDataProducerInfoResponse data = new ApiDataProducerInfoResponse();
        try {
            ApiSystemInfoResponse apiSystemInfoResponse = systemService.info(loginName);
            if (pageIndex == 0) {
                // 无需分页处理
                totalPerPage = 0;
            } else {
                pageIndex = (pageIndex - 1) * totalPerPage;
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("id", id);
            paramMap.put("name", name);
            paramMap.put("code", code);
            paramMap.put("type", type);
            paramMap.put("tableName", tableName);
            paramMap.put("datasourceid", datasourceid);
            paramMap.put("pageIndex", pageIndex);
            paramMap.put("totalPerPage", totalPerPage);
            int total = dao.getDataProducerCount(paramMap);
            List<DataProducer> infos = dao.getDataProducerInfo(paramMap);
            if (infos.size() > 0) {
                data.setCode(MsgCode.code_200);
                for (DataProducer dataProducer : infos) {
                    DataSourceMapper dataSourceMapper = dataSourceService.getsource(loginName)
                            .get(dataProducer.getDatasourceid());
                    if(dataSourceMapper!=null){
                        dataProducer.setDatasourcename(dataSourceMapper.getDataSource().getName());
                    }
                    dataProducer.setSystemProPerties(apiSystemInfoResponse.getData());
                }
                data.setData(infos);
                data.setTotal(total);
            } else {
                data.setCode(MsgCode.code_404);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            data.setCode(MsgCode.code_500);
            data.setMsg(e.getLocalizedMessage());
        }
        return data;
    }

    @Override
    public ApiDataProducerInfoResponse saveData(String id, String name, String code, String tableName,
                                                String datasourceid, String countSql, String selectSql, String insertSql, String updateSql,
                                                String deleteSql, String selectParameter, String updateParameter, String insertParameter,
                                                String deleteParameter, String type, String loginName) throws Exception {
        // TODO Auto-generated method stub
        ApiDataProducerInfoResponse data = new ApiDataProducerInfoResponse();
        try {
            if (StringUtils.isNullOrBlank(id)) {
                id = UUID.randomUUID().toString().replaceAll("-", "");
            }
            DataProducer dataProducer = new DataProducer();
            dataProducer.setId(id);
            dataProducer.setName(name);
            dataProducer.setCode(code);
            dataProducer.setTableName(tableName);
            dataProducer.setDatasourceid(datasourceid);
            dataProducer.setCountSql(countSql);
            dataProducer.setSelectSql(selectSql);
            dataProducer.setInsertSql(insertSql);
            dataProducer.setUpdateSql(updateSql);
            dataProducer.setDeleteSql(deleteSql);
            dataProducer.setSelectParameter(selectParameter);
            dataProducer.setInsertParameter(insertParameter);
            dataProducer.setUpdateParameter(updateParameter);
            dataProducer.setDeleteParameter(deleteParameter);
            dataProducer.setCreatetime(DateUitls.getNowDateTime());
            dataProducer.setType(type);
            Object ok = dao.saveDataProducer(dataProducer);
            data.setCode(MsgCode.code_200);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            data.setCode(MsgCode.code_500);
            data.setMsg(e.getLocalizedMessage());
        }
        return data;
    }

    @Override
    public ApiDataProducerInfoResponse DelInfo(String id, String loginName) throws Exception {
        ApiDataProducerInfoResponse data = new ApiDataProducerInfoResponse();
        try {
            if (StringUtils.isNullOrBlank(id)) {
                data.setCode(MsgCode.code_401);
            } else {
                DataProducer dataProducer = new DataProducer();
                dataProducer.setId(id);
                Object ok = dao.delDataProducer(dataProducer);
                data.setCode(MsgCode.code_200);
            }
        } catch (Exception e) {
            data.setCode(MsgCode.code_500);
            data.setMsg(e.getLocalizedMessage());
        }
        return data;
    }

}
