package com.palmgo.com.cn.sharingdata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceSqlInfoResponse;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceParameterInfoResponse;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceParameterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author Administrator
 *
 */
@Api(description = "数据源SQL字段提取")
@RestController
public class DataSourceParameterController extends CommonController {

	protected static Logger log = CommonLogFactory.getLog();

	@Autowired
	private DataSourceParameterService service;


	@ApiOperation(value = "列表", notes = "数据源SQL字段提取")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "countSql", value = "统计sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "insertSql", value = "新增sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "updateSql", value = "更新sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "deleteSql", value = "删除sql", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "selectSql", value = "查询sql", defaultValue = "", required = false, paramType = "query", dataType = "string")})
	@RequestMapping(value = "datasourceparameterinfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiDataSourceParameterInfoResponse.class) })
	@ResponseBody
	public ApiDataSourceParameterInfoResponse datasourceparameterinfo(
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "countSql", required = false, defaultValue = "") String countSql,
			@RequestParam(value = "insertSql", required = false, defaultValue = "") String insertSql,
			@RequestParam(value = "updateSql", required = false, defaultValue = "") String updateSql,
			@RequestParam(value = "deleteSql", required = false, defaultValue = "") String deleteSql,
			@RequestParam(value = "selectSql", required = false, defaultValue = "") String selectSql,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(String.format("数据源SQL字段提取-列表-服务收到信息："));
		ApiDataSourceParameterInfoResponse data  = null;
		try {
			data = service.info(countSql, insertSql, updateSql, deleteSql, selectSql,loginName);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiDataSourceParameterInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}
}