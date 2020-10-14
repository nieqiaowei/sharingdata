package com.palmgo.com.cn.sharingdata.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palmgo.com.cn.sharingdata.bean.*;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.service.Interface.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Administrator
 */
@Api(description = "用户管理")
@RestController
public class UserController extends CommonController {

    protected static Logger log = CommonLogFactory.getLog();

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户校验", notes = "用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "EybULNfO5ITeI/p6Z6pMJVk3rI+YwpSkqw/4m6lVrVH4J3kFbJ6Gcljd+9qHpY+0v9DmUsuNrUuPafJW3lPLWFj6heRfXHxGPRE8YrCnk4A+2+sgOJKUnEBgVy2lj2hGWF/KTXrscbu+1IwPYPR87GyYWTc6sejRkApPPQvN9iI=", required = true, paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiUserInfoResponse.class)})
    @ResponseBody
    public ApiUserInfoResponse login(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String username,
            @RequestParam(value = "password", required = true, defaultValue = "") String password,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @RequestParam(value = "totalPerPage", required = false, defaultValue = "10") int totalPerPage,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("用户校验服务收到信息：%s,%s", username, password));
        ApiUserInfoResponse data = null;
        try {
            data = userService.login(username, password, pageIndex, totalPerPage);
            if (data.getCode() == MsgCode.code_200) {
                response.setHeader("Authorization", data.getAuthorization());
            }
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiUserInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s",e.getLocalizedMessage()));
        }
        return data;
    }


    @ApiOperation(value = "用户登出", notes = "用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "admin", required = true, paramType = "query", dataType = "string")})
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiUserInfoResponse.class)})
    @ResponseBody
    public ApiUserInfoResponse logout(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String username,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("用户登出服务收到信息：%s", username));
        ApiUserInfoResponse data = null;
        try {
            data = userService.logout(username);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiUserInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s",e.getCause()));
        }
        return data;
    }


    @ApiOperation(value = "用户列表", notes = "用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "用户姓名", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "roleid", value = "用户角色", defaultValue = "2", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "loginName", value = "管理账号名", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageIndex", value = "页数", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "totalPerPage", value = "行数", defaultValue = "10", required = false, paramType = "query", dataType = "string"),})
    @RequestMapping(value = "getuserinfos", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiUserInfoResponse.class)})
    @ResponseBody
    public ApiUserInfoResponse getuserinfos(
            @RequestParam(value = "userid", required = false, defaultValue = "") String userid,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @RequestParam(value = "roleid", required = false, defaultValue = "") String roleid,
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @RequestParam(value = "totalPerPage", required = false, defaultValue = "10") int totalPerPage,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("用户列表服务收到信息：%s,%s", username, roleid));
        ApiUserInfoResponse data = null;
        try {
            data = userService.UserAll(userid, username, roleid, loginName,name, pageIndex, totalPerPage);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiUserInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s",e.getCause()));
        }
        return data;
    }

    @ApiOperation(value = "用户添加", notes = "用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "test", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "loginName", value = "管理账号名", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "姓名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "roleid", value = "角色id", defaultValue = "2", required = true, paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "saveinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiUserInfoResponse.class)})
    @ResponseBody
    public ApiUserInfoResponse saveinfo(
            @RequestParam(value = "userid", required = false, defaultValue = "") String userid,
            @RequestParam(value = "username", required = true, defaultValue = "") String username,
            @RequestParam(value = "password", required = true, defaultValue = "") String password,
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "name", required = true, defaultValue = "") String name,
            @RequestParam(value = "roleid", required = true, defaultValue = "") String roleid,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("用户添加服务收到信息：%s,%s", username, password));
        ApiUserInfoResponse data = null;
        try {
            data = userService.SaveUserInfo(userid, username, name, password, loginName, roleid);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiUserInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s",e.getCause()));
        }
        return data;
    }


    @ApiOperation(value = "用户删除", notes = "用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "loginName", value = "管理账号名", defaultValue = "admin", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "deleteinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiUserInfoResponse.class)})
    @ResponseBody
    public ApiUserInfoResponse Deleteinfo(
            @RequestParam(value = "userid", required = true, defaultValue = "") String userid,
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("用户删除服务收到信息：%s", userid));
        ApiUserInfoResponse data = null;
        try {
            data = userService.DeleteUserInfo(userid, loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiUserInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s",e.getCause()));
        }
        return data;
    }

    @ApiOperation(value = "获取用户公钥", notes = "用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "admin", required = true, paramType = "query", dataType = "string")})
    @RequestMapping(value = "getpublickey", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiUserInfoResponse.class)})
    @ResponseBody
    public ApiUserInfoResponse getpublickey(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("获取用户公钥服务收到信息：%s", loginName));
        ApiUserInfoResponse data = null;
        try {
            data = userService.getpublickey(loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiUserInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s",e.getCause()));
        }
        return data;
    }

    @ApiOperation(value = "获取加密之后的密码", notes = "用户管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "publickey", value = "密码", defaultValue = "admin", required = true, paramType = "query", dataType = "string")})
    @RequestMapping(value = "pwdrsa", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiUserInfoResponse.class)})
    @ResponseBody
    public ApiUserInfoResponse pwdrsa(
            @RequestParam(value = "password", required = true, defaultValue = "") String password,
            @RequestParam(value = "publickey", required = true, defaultValue = "") String publickey,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("获取加密之后的密码服务收到信息：%s", password));
        ApiUserInfoResponse data = null;
        try {
            data = userService.getpwdrsa(publickey,password);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiUserInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s",e.getCause()));
        }
        return data;
    }


    public static void main(String[] args) {
        List<Produceruser> Producerusers = new ArrayList<>();
        Produceruser produceruser = new Produceruser();
        produceruser.setBusiness_code("BLOCK");
        produceruser.setBusiness_name("阻断事件");
        produceruser.setTableName("block");
        Producerusers.add(produceruser);

        Produceruser produceruser1 = new Produceruser();
        produceruser1.setBusiness_code("BLOCK1");
        produceruser1.setBusiness_name("阻断事件1");
        Producerusers.add(produceruser1);

        List<Tableoffield> lists = new ArrayList<>();
        Tableoffield tableoffield = new Tableoffield();
        tableoffield.setTableNamekey("BLOCK");
        tableoffield.setCommentkey("主键id");
        tableoffield.setDecimalkey("DEFAULT");
        tableoffield.setFieldLengthkey("100");
        tableoffield.setFieldNamekey("id");
        tableoffield.setFieldTypekey("varchar");
        tableoffield.setNonullkey(false);
        tableoffield.setPrimary_key(true);
        Tableoffield tableoffield1 = new Tableoffield();
        tableoffield1.setTableNamekey("BLOCK");
        tableoffield1.setCommentkey("道路名称");
        tableoffield1.setDecimalkey("DEFAULT");
        tableoffield1.setFieldLengthkey("1000");
        tableoffield1.setFieldNamekey("roadid");
        tableoffield1.setFieldTypekey("varchar");
        tableoffield1.setNonullkey(false);
        tableoffield1.setPrimary_key(false);
        lists.add(tableoffield);
        lists.add(tableoffield1);
        produceruser.setTableoffields(lists);
        //消费者
        List<Consumeruser> consumerusers = new ArrayList<>();
        Consumeruser consumeruser = new Consumeruser();
        consumeruser.setBusiness_code("BLOCK");
        consumeruser.setFields("id,roadid");
        consumeruser.setOverdueteime("2020-03-18 11:23:00");

    }
}