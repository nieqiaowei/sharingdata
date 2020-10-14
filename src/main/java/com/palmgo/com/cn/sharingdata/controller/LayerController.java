package com.palmgo.com.cn.sharingdata.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiInfoResponse;
import com.palmgo.com.cn.sharingdata.service.Interface.LayerService;

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
@Api(description = "数据服务")
@RestController
public class LayerController extends CommonController {

	protected static Logger log = CommonLogFactory.getLog();
	
	@Autowired
	private LayerService layerService;

	@ApiOperation(value = "数据接入和查询", notes = "数据服务")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "service", value = "服务名称", defaultValue = "BLOCK", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "type", value = "操作类型", defaultValue = "(select|insert|update|delete)", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "loginName", value = "用户名", defaultValue = "test", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "dataBody", value = "数据", defaultValue = "", required = true, paramType = "query", dataType = "string") })
	@RequestMapping(value = "layerinfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiInfoResponse.class) })
	@ResponseBody
	public ApiInfoResponse layerinfo(
			@RequestParam(value = "service", required = true, defaultValue = "") String service,
			@RequestParam(value = "type", required = true, defaultValue = "") String type,
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "dataBody", required = true, defaultValue = "") String dataBody,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(String.format("数据接入服务收到信息：%s,%s,%s,%s", loginName, service,type, dataBody));
		ApiInfoResponse data = null;
		try {
			data = layerService.recevier(service,type, loginName, dataBody);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}

}