package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.JsonUtils;
import com.caits.lbs.framework.utils.MD5;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiConsumerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiDataRoleInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiSystemInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiUserInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.Consumeruser;
import com.palmgo.com.cn.sharingdata.bean.Produceruser;
import com.palmgo.com.cn.sharingdata.bean.Tableoffield;
import com.palmgo.com.cn.sharingdata.bean.UserInfo;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.Userdao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.ConsumeruserService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataRoleService;
import com.palmgo.com.cn.sharingdata.service.Interface.ProduceruserService;
import com.palmgo.com.cn.sharingdata.service.Interface.SystemService;
import com.palmgo.com.cn.sharingdata.service.Interface.UserService;
import com.palmgo.com.cn.sharingdata.util.DateUitls;
import com.palmgo.com.cn.sharingdata.util.RSAEncrypt;
import com.palmgo.com.cn.sharingdata.util.TokenUtils;

/**
 * 用户相关操作
 *
 * @author Administrator
 */
@Service
public class UserServiceImpl implements UserService {

    public Logger log = CommonLogFactory.getLog();

    @Autowired
    private Userdao dao;

    @Autowired
    private DataRoleService dataRoleService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private CommonService commonService;


    @Override
    public ApiUserInfoResponse UserAll(String userid, String username, String roleid, String loginName,String name,
                                       int pageIndex, int totalPerPage) throws Exception {
        ApiUserInfoResponse data = new ApiUserInfoResponse();
        if (pageIndex == 0) {
            // 无需分页处理
            totalPerPage = 0;
        } else {
            pageIndex = (pageIndex - 1) * totalPerPage;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userid", userid);
        paramMap.put("name", name);
        paramMap.put("username", username);
        paramMap.put("roleid", roleid);
        paramMap.put("pageIndex", pageIndex);
        paramMap.put("totalPerPage", totalPerPage);
        int total = dao.GetUserCount(paramMap);
        List<UserInfo> infos = dao.GetUser(paramMap);
        if (infos.size() > 0) {
            data.setTotal(total);
            for (UserInfo userInfo : infos) {
                try {
                    userInfo.setPassword(RSAEncrypt.decrypt(userInfo.getPassword(), userInfo.getPrivate_key()));
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
                ApiDataRoleInfoResponse roledata = dataRoleService.info(userInfo.getRoleid(), null, null, 0, 0, loginName);
                if (roledata.getCode() == MsgCode.code_200) {
                    userInfo.setRoleName(roledata.getData().get(0).getName());
                }
            }
            data.setCode(MsgCode.code_200);
            data.setData(infos);
        } else {
            data.setCode(MsgCode.code_404);
        }
        return data;
    }

    @SuppressWarnings("unused")
    @Override
    public ApiUserInfoResponse SaveUserInfo(
            String userid,
            String username,
            String name,
            String password,
            String loginName,
            String roleid) throws Exception {
        ApiUserInfoResponse data = new ApiUserInfoResponse();
        Object ok;
        UserInfo userInfo = new UserInfo();
        userInfo.setUserid(userid);
        userInfo.setUsername(username);
        userInfo.setName(name);
        userInfo.setRoleid(roleid);
        if (StringUtils.isNullOrBlank(userid)) {
            // 新增
            // 加密--密码
            Map<Integer, String> keyMap = RSAEncrypt.genKeyPair();
            String public_key = keyMap.get(0);
            String private_key = keyMap.get(1);
            //log.info(public_key);
            String pwd = RSAEncrypt.encrypt(password, public_key);
            userInfo.setPassword(pwd);
            userInfo.setPublic_key(public_key);
            userInfo.setPrivate_key(private_key);
            userInfo.setCreateName(loginName);
            userInfo.setCreateTime(DateUitls.getNowDateTime());
            userInfo.setUpdateTime(DateUitls.getNowDateTime());
            userid = UUID.randomUUID().toString().replaceAll("-", "");
            userInfo.setUserid(userid);
            ok = dao.SaveUserInfo(userInfo);
            data.setCode(MsgCode.code_200);
        } else {
            //查询历史的公钥
            ApiUserInfoResponse userInfoTmp = UserAll(userid, username, null, loginName, null,0, 0);
            // 加密--密码
            String public_key = userInfoTmp.getData().get(0).getPublic_key();
            //log.info(public_key);
            String pwd = RSAEncrypt.encrypt(password, public_key);
            userInfo.setPassword(pwd);
            userInfo.setPublic_key(userInfoTmp.getData().get(0).getPublic_key());
            userInfo.setPrivate_key(userInfoTmp.getData().get(0).getPrivate_key());
            userInfo.setCreateTime(userInfoTmp.getData().get(0).getCreateTime());
            userInfo.setCreateName(loginName);
            userInfo.setUpdateTime(DateUitls.getNowDateTime());
            // 修改
            ok = dao.SaveUserInfo(userInfo);
            data.setCode(MsgCode.code_200);
        }
        return data;
    }

    @Override
    public ApiUserInfoResponse DeleteUserInfo(String userid, String loginName) throws Exception {
        // TODO Auto-generated method stub
        Object ok;
        ApiUserInfoResponse data = new ApiUserInfoResponse();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userid", userid);
        if (StringUtils.isNullOrBlank(userid)) {
            data.setCode(MsgCode.code_401);
        } else {
            // 查询用户类型
            ok = dao.DeleteUserInfo(paramMap);
            log.info("返回数据内容：" + ok);
            data.setCode(MsgCode.code_200);
        }
        return data;
    }

    @Override
    public ApiUserInfoResponse login(String username, String password, int pageIndex, int totalPerPage) throws Exception {
        ApiUserInfoResponse data = new ApiUserInfoResponse();
        data = new ApiUserInfoResponse();
        ApiUserInfoResponse result = UserAll(null, username, null, null,null, pageIndex, totalPerPage);
        if (result.getCode() == MsgCode.code_200) {
            List<UserInfo> datas = result.getData();
            UserInfo info = datas.get(0);
            // 解码
            String password_1 = RSAEncrypt.decrypt(password, info.getPrivate_key());
            if (password_1.equals(info.getPassword())) {
                String token = TokenUtils.token(username, password_1);
                log.info(username + ":" + token);
                data.setCode(MsgCode.code_200);
                data.setAuthorization(token);
                data.setData(datas);
            } else {
                data.setCode(MsgCode.code_202);
            }
        } else {
            data.setCode(result.getCode());
        }
        return data;
    }

    @Override
    public ApiUserInfoResponse getpublickey(String loginName) throws Exception {
        ApiUserInfoResponse data = new ApiUserInfoResponse();
        ApiUserInfoResponse result = UserAll(null, loginName, null, null,null, 0, 0);
        if (result.getCode() == MsgCode.code_200) {
            List<UserInfo> datas = result.getData();
            UserInfo info = datas.get(0);
            data.setPublicKey(info.getPublic_key());
        }
        data.setCode(result.getCode());
        return data;
    }

    @Override
    public ApiUserInfoResponse getpwdrsa(String publicKey, String password) throws Exception {
        ApiUserInfoResponse data = new ApiUserInfoResponse();
        if(StringUtils.notNullOrBlank(publicKey) && StringUtils.notNullOrBlank(password)){
            String pwd = RSAEncrypt.encrypt(password, publicKey);
            data.setPwdrsa(pwd);
            data.setCode(MsgCode.code_200);
        }else{
            data.setCode(MsgCode.code_404);
        }

        return data;
    }

    public static void main(String[] args) throws Exception {
        RSAEncrypt.encrypt("test", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK/CpvXqnqYP5RHU+Tb9aNBR3GspojX3BlXiExDXYh1zm3aDADZJ8F4Z9w/oVlLM1E0RBA9N98WPMPOL9WDGsGN04PfR8AUuIrfY2nbujD8MK5lC9H8Qf9vzsfn+7nvmgU237jGdaMzYCySLCO9MMQQj3VZ/DYrU6/4Md7OBpOTjAgMBAAECgYBYVmIqGWjxl+2O/9beH5GNmodJSRyhOO3zPnRMs6Q5n0BU2VTC9HieIzi8ww6YASD3NwUuWAafQe/diMukf1hpXWMCg0f7nvl4FRipSJf0HOeYDI2eHKN9Y7jg7kYRSAJpYm6bndq3Q5QrGt6XC2koKiCxOjbyuzu3UEHX+UV4eQJBAPHzOpUPXWTvsgM220n1tl4JPvuslPu4MZL9K6NwPnrqoBnQHc2pDzvQe2G9h5FcP3xLWNjuLPdUbCei2HMwlC0CQQC593Zy3Yok4lt8FcJiUpYKkXPysZWVJvcB6OlxAYDzOBkuuuY1vtCcQcJPntB45nHjkh7EEQMma2HVF5F4h7dPAkEAjQQbb8ddwrelhBQT6V5ppRM0f1EOIEwxsJ8YMRD5iYB4QbM3u6c+NrmDP+tMOV+PrOzpYr4mvRyaUNur1i9JHQJAHTRTka5g53H01APGxxIZJge5ob9pskawO8iMAIcv7QI+Uixfwsj4kdFx5ncWXiYydH6Z/kh9qibq8kAFRFxr2QJBAJM/kf0HIMDA7CCvyICYYP9jUPAdLzYMYp/f7O2fzNr3vvoKkjo1Im++Bo+pUZgPemte0TJtb89ESNesQBGkQaM=");
    }

    @Override
    public ApiUserInfoResponse logout(String username) throws Exception {
        ApiUserInfoResponse data = new ApiUserInfoResponse();
        //设置token的过期时间
        ApiUserInfoResponse result = UserAll(null, username, null, null,null, 0, 0);
        if (result.getCode() == MsgCode.code_200) {
            List<UserInfo> datas = result.getData();
            UserInfo info = datas.get(0);
        }
        data.setCode(result.getCode());
        return data;
    }

}
