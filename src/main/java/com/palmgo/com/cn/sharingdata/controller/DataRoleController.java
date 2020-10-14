package com.palmgo.com.cn.sharingdata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceColumnInfoResponse;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiDataRoleInfoResponse;
import com.palmgo.com.cn.sharingdata.service.Interface.DataRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Administrator
 */
@Api(description = "数据角色")
@RestController
public class DataRoleController extends CommonController {

    protected static Logger log = CommonLogFactory.getLog();

    @Autowired
    private DataRoleService service;

    @ApiOperation(value = "列表", notes = "数据角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "编码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名称", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "业务代码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageIndex", value = "页数", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "totalPerPage", value = "行数", defaultValue = "10", required = false, paramType = "query", dataType = "string")})
    @RequestMapping(value = "dataroleinfos", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiDataRoleInfoResponse.class)})
    @ResponseBody
    public ApiDataRoleInfoResponse dataroleinfos(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "code", required = false, defaultValue = "") String code,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @RequestParam(value = "totalPerPage", required = false, defaultValue = "10") int totalPerPage,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("数据角色-列表-服务收到信息"));
        ApiDataRoleInfoResponse data = null;
        try {
            data = service.info(id, name, code, pageIndex, totalPerPage, loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiDataRoleInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }

    @ApiOperation(value = "数据保存", notes = "数据角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "编码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名称", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "业务代码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "dataRoleSources[]", value = "业务代码", defaultValue = "", required = false, paramType = "query", dataType = "string")})
    @RequestMapping(value = "saveroleinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiDataRoleInfoResponse.class)})
    @ResponseBody
    public ApiDataRoleInfoResponse saveroleinfo(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "code", required = false, defaultValue = "") String code,
            @RequestParam(value = "dataRoleSources[]", required = false, defaultValue = "") String[] dataRoleSources, HttpServletRequest request,
            HttpServletResponse response) {
        log.info(String.format("数据角色-数据保存-服务收到信息：%s,%s", id, name));
        ApiDataRoleInfoResponse data = null;
        try {
            data = service.Saveinfo(id, name, code, dataRoleSources, loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiDataRoleInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }

    @ApiOperation(value = "数据删除", notes = "数据角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "编码", defaultValue = "1", required = true, paramType = "query", dataType = "string"),})
    @RequestMapping(value = "delroleinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiDataRoleInfoResponse.class)})
    @ResponseBody
    public ApiDataRoleInfoResponse delroleinfo(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "id", required = true, defaultValue = "") String id,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("数据角色-数据删除-服务收到信息：%s", id));
        ApiDataRoleInfoResponse data = null;
        try {
            data = service.DelInfo(id,loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiDataRoleInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }
}