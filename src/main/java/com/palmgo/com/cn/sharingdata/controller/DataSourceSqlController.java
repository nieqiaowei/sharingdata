package com.palmgo.com.cn.sharingdata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceTablesInfoResponse;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceSqlInfoResponse;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceSqlService;

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
@Api(description = "数据源表SQL生成")
@RestController
public class DataSourceSqlController extends CommonController {

	protected static Logger log = CommonLogFactory.getLog();

	@Autowired
	private DataSourceSqlService service;


	@ApiOperation(value = "列表", notes = "数据源表SQL生成")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "id", value = "数据源id", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "tableName", value = "表名称", defaultValue = "test", required = true, paramType = "query", dataType = "string") })
	@RequestMapping(value = "datasourcesqlinfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiDataSourceSqlInfoResponse.class) })
	@ResponseBody
	public ApiDataSourceSqlInfoResponse datasourcesqlinfo(
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "id", required = true, defaultValue = "") String id,
			@RequestParam(value = "tableName", required = true, defaultValue = "") String tableName,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(String.format("数据源表SQL生成-列表-服务收到信息：%s,%s", id, tableName));
		ApiDataSourceSqlInfoResponse data  = null;
		try {
			data = service.info(id, tableName,loginName);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiDataSourceSqlInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}
}