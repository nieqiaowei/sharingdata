package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.Date;
import java.util.HashMap;


import com.caits.lbs.exception.LBSException;
import com.palmgo.com.cn.sharingdata.util.DateUitls;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.MD5;
import com.palmgo.com.cn.sharingdata.bean.ApiSystemInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.SystemProPerties;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.Systemdao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.SystemService;
import com.palmgo.com.cn.sharingdata.util.RSAEncrypt;

@Service
public class SystemServiceImpl implements SystemService {

    public Logger log = CommonLogFactory.getLog();
    @Autowired
    private Systemdao dao;

    @Autowired
    private CommonService commonService;

    @Override
    public ApiSystemInfoResponse info(String loginName) throws Exception {
        ApiSystemInfoResponse data = new ApiSystemInfoResponse();
        String MD5key = MD5.encode("sys").toUpperCase();
        data = (ApiSystemInfoResponse) commonService.getCache(MD5key);
        if (data == null) {
            data = new ApiSystemInfoResponse();
            SystemProPerties systemProPerties = dao.GetSystem(new HashMap<>());
            if (systemProPerties != null) {
                //解码
                String License = systemProPerties.getLicense();
                if (License != null) {
                    String date = RSAEncrypt.decrypt(License, systemProPerties.getPrivate_key());
                    //记录的是utc时间
                    long utc = Long.parseLong(date);
                    String overtime = DateUitls.simpleDateFormat(utc,null);
                    systemProPerties.setOvertime(overtime);
                    if (new Date().getTime() <= utc) {
                        data.setCode(MsgCode.code_200);
                        data.setData(systemProPerties);
                        commonService.setCache(MD5key, data);
                    } else {
                        data.setCode(MsgCode.code_1001);
                    }
                } else {
                    data.setCode(MsgCode.code_1000);
                }
            } else {
                data.setCode(MsgCode.code_404);
            }
        }
        return data;
    }

    @Override
    public ApiSystemInfoResponse SaveUserInfo(String loginName,String private_key,String public_key, String sysname, String license, String sysaddress,
                                              String systemid, String dataBody, String type) throws Exception {
        // TODO Auto-generated method stub
        ApiSystemInfoResponse data = new ApiSystemInfoResponse();
        SystemProPerties systemProPerties = new SystemProPerties();
        systemProPerties.setLicense(license);
        systemProPerties.setPrivate_key(private_key);
        systemProPerties.setPublic_key(public_key);
        systemProPerties.setSysaddress(sysaddress);
        systemProPerties.setSystemid(systemid);
        systemProPerties.setSysname(sysname);
        systemProPerties.setData(dataBody);
        systemProPerties.setType(type);
        dao.UpdateSystemInfo(systemProPerties);
        data.setCode(MsgCode.code_200);
        return data;
    }

}
