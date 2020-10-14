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
import com.palmgo.com.cn.sharingdata.service.Interface.DataProducerService;
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
 * @author Administrator
 *
 */
@Api(description = "数据发布与消费")
@RestController
public class DataProducerController extends CommonController {

	protected static Logger log = CommonLogFactory.getLog();

	@Autowired
	private DataProducerService service;

	@ApiOperation(value = "列表", notes = "数据发布与消费")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "id", value = "编码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "name", value = "名称", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "code", value = "业务代码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "tableName", value = "表名称", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "datasourceid", value = "数据源id", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "type", value = "业务类型", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "pageIndex", value = "页数", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "totalPerPage", value = "行数", defaultValue = "10", required = false, paramType = "query", dataType = "string") })
	@RequestMapping(value = "dataproducerinfos", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiDataProducerInfoResponse.class) })
	@ResponseBody
	public ApiDataProducerInfoResponse dataproducerinfos(
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "tableName", required = false, defaultValue = "") String tableName,
			@RequestParam(value = "datasourceid", required = false, defaultValue = "") String datasourceid,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "totalPerPage", required = false, defaultValue = "10") int totalPerPage,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(String.format("数据发布与消费-列表-服务收到信息"));
		ApiDataProducerInfoResponse data = null;
		try {
			data = service.info(id, name, code, tableName, datasourceid,type,pageIndex,
					totalPerPage,loginName);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiDataProducerInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}

	@ApiOperation(value = "数据保存", notes = "数据发布与消费")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "id", value = "编码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "name", value = "名称", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "code", value = "业务代码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "tableName", value = "表名称", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "datasourceid", value = "数据源id", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "countSql", value = "统计sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "selectSql", value = "查询sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "insertSql", value = "新增sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "updateSql", value = "修改sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "deleteSql", value = "删除sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "selectParameter", value = "查询参数", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "updateParameter", value = "修改参数", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "insertParameter", value = "新增参数", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "deleteParameter", value = "删除参数", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "type", value = "删除参数", defaultValue = "", required = false, paramType = "query", dataType = "string")})
	@RequestMapping(value = "saveproducerinfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiDataProducerInfoResponse.class) })
	@ResponseBody
	public ApiDataProducerInfoResponse saveproducerinfo(
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "id", required = false, defaultValue = "") String id,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
			@RequestParam(value = "tableName", required = false, defaultValue = "") String tableName,
			@RequestParam(value = "datasourceid", required = false, defaultValue = "") String datasourceid,
			@RequestParam(value = "countSql", required = false, defaultValue = "") String countSql,
			@RequestParam(value = "selectSql", required = false, defaultValue = "") String selectSql,
			@RequestParam(value = "insertSql", required = false, defaultValue = "") String insertSql,
			@RequestParam(value = "updateSql", required = false, defaultValue = "") String updateSql,
			@RequestParam(value = "deleteSql", required = false, defaultValue = "") String deleteSql,
			@RequestParam(value = "selectParameter", required = false, defaultValue = "") String selectParameter,
			@RequestParam(value = "updateParameter", required = false, defaultValue = "") String updateParameter,
			@RequestParam(value = "insertParameter", required = false, defaultValue = "") String insertParameter,
			@RequestParam(value = "deleteParameter", required = false, defaultValue = "") String deleteParameter,
			@RequestParam(value = "type", required = false, defaultValue = "") String type,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(String.format("数据发布与消费-数据保存-服务收到信息：%s,%s", id, name));
		ApiDataProducerInfoResponse data = null;
		try {
			data = service.saveData(id, name, code, tableName, datasourceid, countSql,
					selectSql, insertSql, updateSql, deleteSql, selectParameter, updateParameter,
					insertParameter, deleteParameter,type,loginName);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiDataProducerInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}

	@ApiOperation(value = "数据删除", notes = "数据发布与消费")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "id", value = "编码", defaultValue = "1", required = false, paramType = "query", dataType = "string"), })
	@RequestMapping(value = "delproducerinfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiDataProducerInfoResponse.class) })
	@ResponseBody
	public ApiDataProducerInfoResponse delproducerinfo(
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "id", required = false, defaultValue = "") String id, HttpServletRequest request,
			HttpServletResponse response) {
		log.info(String.format("数据发布与消费-数据删除-服务收到信息：%s", id));
		ApiDataProducerInfoResponse data = null;
		try {
			data = service.DelInfo(id,loginName);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiDataProducerInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}
}