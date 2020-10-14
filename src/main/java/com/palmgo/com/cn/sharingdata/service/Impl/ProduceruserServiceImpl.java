package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.JsonUtils;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiConsumerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.Produceruser;
import com.palmgo.com.cn.sharingdata.bean.SystemProPerties;
import com.palmgo.com.cn.sharingdata.bean.Tableoffield;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.Produceruserdao;
import com.palmgo.com.cn.sharingdata.dao.Systemdao;
import com.palmgo.com.cn.sharingdata.dao.Userdao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.ConsumeruserService;
import com.palmgo.com.cn.sharingdata.service.Interface.ProduceruserService;

/**
 * 生产者相关实现
 *
 * @author Administrator
 */
@Service
public class ProduceruserServiceImpl implements ProduceruserService {

    public Logger log = CommonLogFactory.getLog();

    @Autowired
    private Produceruserdao dao;

    @Autowired
    private Userdao userdao;

    @Autowired
    private Systemdao systemdao;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ConsumeruserService consumeruserService;


    @Override
    public ApiProducerInfoResponse Getproduceruser(String userid, String business_code, String tableName, String loginName,
                                                   int pageIndex, int totalPerPage) throws Exception {
        ApiProducerInfoResponse data = new ApiProducerInfoResponse();
        if (pageIndex == 0) {
            // 无需分页处理
            totalPerPage = 0;
        } else {
            pageIndex = (pageIndex - 1) * totalPerPage;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userid", userid);
        paramMap.put("business_code", business_code);
        paramMap.put("tableName", tableName);
        paramMap.put("pageIndex", pageIndex);
        paramMap.put("totalPerPage", totalPerPage);
        int total = dao.GetproduceruserCount(paramMap);
        List<Produceruser> infos = dao.Getproduceruser(paramMap);
        if (infos.size() > 0) {
            data.setTotal(total);
            //查询字段信息
            for (Produceruser produceruser : infos) {
                paramMap.put("tableNamekey", produceruser.getTableName());
                //List<Tableoffield> tableoffields =  dao.Gettableoffield(paramMap);
                //if(tableoffields!=null) {
                //	produceruser.setTableoffields(tableoffields);
                //}
            }
            data.setCode(MsgCode.code_200);
            data.setData(infos);
        } else {
            data.setCode(MsgCode.code_404);
        }
        return data;
    }

    @Override
    public ApiProducerInfoResponse SaveproduceruserInfo(String userid, Produceruser produceruser, String loginName) throws Exception {
        ApiProducerInfoResponse data = new ApiProducerInfoResponse();
        Object ok;
        //创建
        List<Map<String, String>> requestList = new LinkedList<>();
        Map<String, String> requestMap = new HashMap<>();
        List<Tableoffield> tableoffields = produceruser.getTableoffields();
        String primary_key = "";
        if (tableoffields != null) {
            for (Tableoffield tableoffield : tableoffields) {
                requestMap.put(tableoffield.getFieldNamekey(), "");
                if (tableoffield.isPrimary_key()) {
                    primary_key += tableoffield.getFieldNamekey() + ",";
                }
                if (tableoffield.isNonullkey()) {
                    tableoffield.setIsNull("DEFAULT NULL");
                } else {
                    tableoffield.setIsNull("NOT NULL");
                }
                requestList.add(requestMap);
                tableoffield.setTableNamekey(produceruser.getTableName());
            }
            if (StringUtils.notNullOrBlank(primary_key)) {
                primary_key = primary_key.substring(0, primary_key.length() - 1);
            }
            if (StringUtils.isNullOrBlank(produceruser.getUserid())) {
                //创建
                produceruser.setUserid(userid);
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("userid", userid);
                paramMap.put("tableName", produceruser.getTableName());
                paramMap.put("primary_index", primary_key);
                paramMap.put("content", tableoffields);
				/*try {
					//创建生成者表与字段的关系
					ok = dao.InserttableoffieldInfo(paramMap);
				} catch (Exception e) {
					log.error("操作数据库异常！"+e.getLocalizedMessage(),e);
					//删除有部分成功的数据
					ok = dao.DeletetableoffieldInfo(paramMap);
					ok = userdao.DeleteUserInfo(paramMap);
					data.setCode(MsgCode.code_204);
					return data;
				}*/
                try {
                    //创建生产者的业务表
                    ok = dao.createtableInfo(paramMap);
                } catch (Exception e) {
                    log.error("操作数据库异常！" + e.getLocalizedMessage(), e);
                    ok = dao.DeletetableoffieldInfo(paramMap);
                    ok = userdao.DeleteUserInfo(paramMap);
                    data.setCode(MsgCode.code_204);
                    return data;
                }
                try {
                    //创建生产者数据
                    ok = dao.InsertproduceruserInfo(produceruser);
                } catch (Exception e) {
                    log.info("操作数据库异常！" + e.getLocalizedMessage(), e);
                    ok = dao.DeletetableoffieldInfo(paramMap);
                    ok = dao.DROPtableInfo(paramMap);
                    ok = userdao.DeleteUserInfo(paramMap);
                    data.setCode(MsgCode.code_204);
                    return data;
                }
                //生成接口参数 更新生产者
                try {
                    SystemProPerties systemProPerties = systemdao.GetSystem(new HashMap<>());
                    String Sysaddress = systemProPerties.getSysaddress();
                    String requestJson = JsonUtils.getJsonStringFromObject(requestList);
                    produceruser.setAddress(Sysaddress);
                    produceruser.setDataformat(requestJson);
                    ok = dao.UpdateproduceruserInfo(produceruser);
                } catch (Exception e) {
                    log.error("操作数据库异常！" + e.getLocalizedMessage(), e);
                    ok = dao.DeletetableoffieldInfo(paramMap);
                    ok = dao.DROPtableInfo(paramMap);
                    ok = userdao.DeleteUserInfo(paramMap);
                    data.setCode(MsgCode.code_204);
                    return data;
                }
                data.setCode(MsgCode.code_200);
            } else {
                //修改--只能修改消费者信息，不能再修改表的字段信息
                try {
                    SystemProPerties systemProPerties = systemdao.GetSystem(new HashMap<>());
                    String Sysaddress = systemProPerties.getSysaddress();
                    String requestJson = JsonUtils.getJsonStringFromObject(requestList);
                    produceruser.setAddress(Sysaddress);
                    produceruser.setDataformat(requestJson);
                    ok = dao.UpdateproduceruserInfo(produceruser);
                    data.setCode(MsgCode.code_201);
                } catch (Exception e) {
                    log.info("操作数据库异常！" + e.getLocalizedMessage(), e);
                    data.setCode(MsgCode.code_204);
                }

            }
        } else {
            data.setCode(MsgCode.code_401);
        }

        return data;
    }

    @Override
    public ApiProducerInfoResponse DeleteproduceruserInfo(Produceruser produceruser, String loginName) throws Exception {
        ApiProducerInfoResponse data = new ApiProducerInfoResponse();
        Object ok;
        if (!StringUtils.isNullOrBlank(produceruser.getUserid())) {
            //有消费者，则不能删除生产者
            ApiConsumerInfoResponse apiConsumerInfoResponse = consumeruserService.Getconsumeruser(null, produceruser.getBusiness_code(), null, 0, 0);
            if (apiConsumerInfoResponse.getCode() == MsgCode.code_200) {
                //存在数据不能删除--请先删除消费者
                data.setCode(MsgCode.code_208);
            } else {
                //删除生产者用户信息
                ok = dao.DeleteproduceruserInfo(produceruser);
                //删除生产业务表对应的字段信息
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("tableName", produceruser.getTableName());
                ok = dao.DeletetableoffieldInfo(paramMap);
                //删除生产者业务表
                ok = dao.DROPtableInfo(paramMap);
                data.setCode(MsgCode.code_200);
            }

        } else {
            data.setCode(MsgCode.code_401);
        }
        return data;
    }

}
