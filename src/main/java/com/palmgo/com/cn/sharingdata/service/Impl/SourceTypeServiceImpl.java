package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiSourceTypeInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.SourceType;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.SourceTypedao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.SourceTypeService;

@Service
public class SourceTypeServiceImpl implements SourceTypeService {

    public Logger log = CommonLogFactory.getLog();


    @Autowired
    private SourceTypedao dao;

    @Autowired
    private CommonService commonService;

    @Override
    public ApiSourceTypeInfoResponse info(String id, String name, String loginName) throws Exception {
        // TODO Auto-generated method stub
        ApiSourceTypeInfoResponse data = new ApiSourceTypeInfoResponse();
        SourceType sourceType = new SourceType();
        sourceType.setId(id);
        sourceType.setName(name);
        List<SourceType> infos = dao.getSourceTypeInfo(sourceType);
        if (infos.size() > 0) {
            data.setCode(MsgCode.code_200);
            data.setData(infos);
            data.setTotal(infos.size());
        } else {
            data.setCode(MsgCode.code_404);
        }
        return data;
    }

    @Override
    public ApiSourceTypeInfoResponse SaveInfo(String id, String name, String driver_class_name, String tablesSql, String tableFieldSql, String loginName) throws Exception {
        ApiSourceTypeInfoResponse data = new ApiSourceTypeInfoResponse();
        SourceType sourceType = new SourceType();
        if (StringUtils.isNullOrBlank(id)) {
            sourceType.setId(UUID.randomUUID().toString().replace("-", ""));
            sourceType.setName(name);
            sourceType.setDriver_class_name(driver_class_name);
            sourceType.setTableFieldSql(tableFieldSql);
            sourceType.setTablesSql(tablesSql);
            Object ok = dao.saveSourceType(sourceType);
            data.setCode(MsgCode.code_200);
        } else {
            sourceType.setId(id);
            sourceType.setName(name);
            sourceType.setDriver_class_name(driver_class_name);
            sourceType.setTableFieldSql(tableFieldSql);
            sourceType.setTablesSql(tablesSql);
            Object ok = dao.saveSourceType(sourceType);
            data.setCode(MsgCode.code_200);
        }
        return data;
    }

    @Override
    public ApiSourceTypeInfoResponse DelInfo(String id, String loginName) throws Exception {
        // TODO Auto-generated method stub
        ApiSourceTypeInfoResponse data = new ApiSourceTypeInfoResponse();
        SourceType sourceType = new SourceType();
        if (StringUtils.isNullOrBlank(id)) {
            data.setCode(MsgCode.code_401);
        } else {
            sourceType.setId(id);
            Object ok = dao.delSourceType(sourceType);
            data.setCode(MsgCode.code_200);
        }
        return data;
    }

}
