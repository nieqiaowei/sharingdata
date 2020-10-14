package com.palmgo.com.cn.sharingdata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.MD5;
import com.palmgo.com.cn.sharingdata.bean.ApiSystemInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiUserInfoResponse;
import com.palmgo.com.cn.sharingdata.service.Interface.SystemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 公钥为:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMWtH/ajQQa5KPhCUqv+NYg/+ivktYGBMwsOHXNFoqo+ja6H7K8JutEi6F6f34Z7AZJmuQ+v4mYCd9SBG34/egdI5P6sOUGIkBomDMUsn96ugy/tIw1pUvXCqf8YCSzMPzlk9LQBWJlYgBVI14Qm2q+0nASAQ+kv8WAaRfLH2JfwIDAQAB
 * 私钥为:MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMxa0f9qNBBrko+EJSq/41iD/6K+S1gYEzCw4dc0Wiqj6Nrofsrwm60SLoXp/fhnsBkma5D6/iZgJ31IEbfj96B0jk/qw5QYiQGiYMxSyf3q6DL+0jDWlS9cKp/xgJLMw/OWT0tAFYmViAFUjXhCbar7ScBIBD6S/xYBpF8sfYl/AgMBAAECgYBypGi23z/CDnc0KDApoWjLWZHqHNNr4mWxpSmtb2M+FKInXNL0yrKrD04mNjUSTCkQkus5dHVr1fmRYQL/pNZfntgZNqpV0bEfd//mE5rG5OO1RhLrXJ1CQqPHftazA8N18/b3+PJVuJyTmQw2AxpcgwMI31hwPcfqyUY0859c0QJBAO/bqJCYj5jDEnXnAAtpcGybS4MLlmOL15umvptaUJfBhPvwVGAVUIba0eJhy6shr2eOA1QQIS26/+zDqsW2CssCQQDaG4Bh2CewkCFh+8gpA0Jy7sOR8cqfUQqMJ0kxEDqWHI8cLo7uvwuDVO91g59zIO0um0cRRbzKPduAOcXzBmGdAkBRXOq+OfIj+LAPbI+YT2kCfl9MEacvF2XI9/QaULWb9No+eBS3DyPkyt99cn0bDO8qKoM4hcwrbg6YavtH2+unAkBpt6bUxkcGp/XmXno4luHgf6a/2OkTq95m7KvnYixjklgzmsWhJ2zXLZYb6b6huTX9vzoFPz4w8Sax0k8GavNpAkBUl7dpJqtSdbwuGESt7SUwpgztntMK/ruzx4d+kWCjSYvbWxT+RsI0ddEFCbyWaIw9VF8f/EXY8ApG3ZF5/Zfz
 *
 * @author Administrator
 */
@Api(description = "系统信息")
@RestController
public class SysController extends CommonController {

    protected static Logger log = CommonLogFactory.getLog();

    @Autowired
    private SystemService service;

    @ApiOperation(value = "系统详情", notes = "系统信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "sysinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiSystemInfoResponse.class)})
    @ResponseBody
    public ApiSystemInfoResponse sysinfo(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("系统详情服务收到信息"));
        ApiSystemInfoResponse data = null;
        try {
            data = service.info(loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiSystemInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }

    @ApiOperation(value = "系统数据修改", notes = "系统信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "private_key", value = "私钥", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "public_key", value = "公钥", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sysname", value = "系统名称", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "license", value = "证书", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sysaddress", value = "服务器地址", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "systemid", value = "服务编码", defaultValue = "", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "data", value = "消息体", defaultValue = "", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "操作类型", defaultValue = "", required = true, paramType = "query", dataType = "string")})
    @RequestMapping(value = "syssaveinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiSystemInfoResponse.class)})
    @ResponseBody
    public ApiSystemInfoResponse syssaveinfo(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "private_key", required = true, defaultValue = "") String private_key,
            @RequestParam(value = "public_key", required = true, defaultValue = "") String public_key,
            @RequestParam(value = "sysname", required = true, defaultValue = "") String sysname,
            @RequestParam(value = "license", required = true, defaultValue = "") String license,
            @RequestParam(value = "sysaddress", required = true, defaultValue = "") String sysaddress,
            @RequestParam(value = "systemid", required = true, defaultValue = "") String systemid,
            @RequestParam(value = "data", required = true, defaultValue = "") String dataBody,
            @RequestParam(value = "type", required = true, defaultValue = "") String type,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("系统数据服务收到信息：%s,%s,%s", systemid, sysname, sysaddress));
        ApiSystemInfoResponse data = null;
        try {
            data = service.SaveUserInfo(loginName,private_key, public_key,sysname, license, sysaddress, systemid, dataBody, type);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiSystemInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }
}