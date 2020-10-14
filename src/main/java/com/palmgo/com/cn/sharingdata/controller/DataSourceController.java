package com.palmgo.com.cn.sharingdata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.caits.lbs.framework.utils.MD5;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceService;
import com.palmgo.com.cn.sharingdata.service.Interface.SourceTypeService;
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
@Api(description = "数据源")
@RestController
public class DataSourceController extends CommonController {

    protected static Logger log = CommonLogFactory.getLog();

    @Autowired
    private DataSourceService service;

    @ApiOperation(value = "列表", notes = "数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "编码", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "mysql", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "数据源名称", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "host", value = "ip地址", defaultValue = "mysql", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "database", value = "数据库名称", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageIndex", value = "页数", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "totalPerPage", value = "行数", defaultValue = "10", required = false, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "datasourceinfos", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiDataSourceInfoResponse.class)})
    @ResponseBody
    public ApiDataSourceInfoResponse datasourceinfos(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "host", required = false, defaultValue = "") String host,
            @RequestParam(value = "database", required = false, defaultValue = "") String database,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @RequestParam(value = "totalPerPage", required = false, defaultValue = "10") int totalPerPage,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("数据源类型-列表-服务收到信息"));
        ApiDataSourceInfoResponse data = null;
        try {
            data = service.info(id, name, username, host, database, pageIndex, totalPerPage, loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiDataSourceInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }

    @ApiOperation(value = "数据保存", notes = "数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "编码", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名称", defaultValue = "gongxiang", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "root", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "root", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "host", value = "ip地址", defaultValue = "172.16.30.241", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "port", value = "端口号", defaultValue = "3306", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "database", value = "数据库名称", defaultValue = "sharingdata", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "sourcetypeid", value = "数据类型id", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "remotejdbc_url", value = "数据库连接地址", defaultValue = "1", required = true, paramType = "query", dataType = "string")})
    @RequestMapping(value = "savedatasourceinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiDataSourceInfoResponse.class)})
    @ResponseBody
    public ApiDataSourceInfoResponse savedatasourceinfo(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @RequestParam(value = "password", required = false, defaultValue = "") String password,
            @RequestParam(value = "host", required = false, defaultValue = "") String host,
            @RequestParam(value = "port", required = false, defaultValue = "") String port,
            @RequestParam(value = "database", required = true, defaultValue = "") String database,
            @RequestParam(value = "sourcetypeid", required = false, defaultValue = "") String sourcetypeid,
            @RequestParam(value = "remotejdbc_url", required = true, defaultValue = "") String remotejdbc_url,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("数据源-数据保存-服务收到信息：%s,%s", id, name, database));
        ApiDataSourceInfoResponse data = null;
        try {
            data = service.saveDataSource(id, name, username, password, host, port, database, sourcetypeid,remotejdbc_url, loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiDataSourceInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }


    @ApiOperation(value = "数据删除", notes = "数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "编码", defaultValue = "1", required = false, paramType = "query", dataType = "string"),})
    @RequestMapping(value = "deldatasourceinfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "成功", response = ApiDataSourceInfoResponse.class)})
    @ResponseBody
    public ApiDataSourceInfoResponse deldatasourceinfo(
            @RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
            @RequestParam(value = "id", required = false, defaultValue = "") String id,
            HttpServletRequest request, HttpServletResponse response) {
        log.info(String.format("数据源类型-数据删除-服务收到信息：%s", id));
        ApiDataSourceInfoResponse data = null;
        try {
            data = service.DelInfo(id, loginName);
        } catch (Exception e) {
            log.error(e.getCause());
            data = new ApiDataSourceInfoResponse();
            data.setCode(MsgCode.code_500);
            data.setMsg(String.format("%s", e.getCause()));
        }
        return data;
    }
}