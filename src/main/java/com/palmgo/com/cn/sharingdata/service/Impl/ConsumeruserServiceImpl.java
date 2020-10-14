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
import com.caits.lbs.framework.utils.JsonUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiConsumerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.Consumeruser;
import com.palmgo.com.cn.sharingdata.bean.SystemProPerties;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.Consumeruserdao;
import com.palmgo.com.cn.sharingdata.dao.Systemdao;
import com.palmgo.com.cn.sharingdata.dao.Userdao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.ConsumeruserService;
import com.palmgo.com.cn.sharingdata.util.StringUtils;

@Service
public class ConsumeruserServiceImpl implements ConsumeruserService {

    public Logger log = CommonLogFactory.getLog();

    @Autowired
    private Consumeruserdao dao;

    @Autowired
    private Userdao userdao;

    @Autowired
    private Systemdao systemdao;

    @Autowired
    private CommonService commonService;

    @Override
    public ApiConsumerInfoResponse Getconsumeruser(String userid, String business_code, String loginName,
                                                   int pageIndex,
                                                   int totalPerPage) {
        ApiConsumerInfoResponse data = new ApiConsumerInfoResponse();
        if (pageIndex == 0) {
            // 无需分页处理
            totalPerPage = 0;
        } else {
            pageIndex = (pageIndex - 1) * totalPerPage;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userid", userid);
        paramMap.put("business_code", business_code);
        paramMap.put("pageIndex", pageIndex);
        paramMap.put("totalPerPage", totalPerPage);
        int total = dao.GetconsumeruserCount(paramMap);
        List<Consumeruser> infos = dao.Getconsumeruser(paramMap);
        if (infos.size() > 0) {
            data.setCode(MsgCode.code_200);
            data.setTotal(total);
            data.setData(infos);
        } else {
            data.setCode(MsgCode.code_404);
        }
        return data;
    }

    @Override
    public ApiConsumerInfoResponse SaveconsumeruserInfo(String userid, Consumeruser[] consumerusersdel,
                                                        Consumeruser[] consumerusers, String loginName) throws Exception {
        // TODO Auto-generated method stub
        // 批量更新或者新增
        ApiConsumerInfoResponse data = new ApiConsumerInfoResponse();
        Object ok;
        // 如果在前端删除了，没有传给后台,必须要传给后台才能删除（为了反正故意不传导致全部删除）
        // 组织内容
        SystemProPerties systemProPerties = systemdao.GetSystem(new HashMap<>());
        String Sysaddress = systemProPerties.getSysaddress();
        for (Consumeruser consumeruser : consumerusers) {
            if (StringUtils.isNullOrBlank(consumeruser.getId())) {
                consumeruser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            }
            consumeruser.setUserid(userid);
            Map<String, String> map = StringUtils.getVars(consumeruser.getSql());
            String requestJson = JsonUtils.getJsonStringFromObject(map);
            consumeruser.setOldaddress(Sysaddress);
            consumeruser.setDataformat(requestJson);
        }

        // 删除消费者类型和用户
        if (consumerusersdel != null) {
            for (Consumeruser consumeruser : consumerusers) {
                ok = dao.DeleteconsumeruserInfo(consumeruser);
            }
        }

        // 新增或者更新消费者
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userid", userid);
        paramMap.put("content", consumerusers);
        try {
            ok = dao.SaveconsumeruserInfo(paramMap);
            data.setCode(MsgCode.code_200);
        } catch (Exception e) {
            log.error("操作数据库异常！" + e.getLocalizedMessage(), e);
            ok = userdao.DeleteUserInfo(paramMap);
            data.setCode(MsgCode.code_204);
        }
        return data;
    }

    @Override
    public ApiConsumerInfoResponse DeleteconsumeruserInfo(List<Consumeruser> consumeruserslist, String loginName) throws Exception {
        Object ok;
        ApiConsumerInfoResponse data = new ApiConsumerInfoResponse();
        for (Consumeruser consumeruser : consumeruserslist) {
            if (!StringUtils.isNullOrBlank(consumeruser.getUserid())) {
                //删除消费者列表
                ok = dao.DeleteconsumeruserInfo(consumeruser);
            }
        }
        data.setCode(MsgCode.code_200);
        return data;
    }

}
