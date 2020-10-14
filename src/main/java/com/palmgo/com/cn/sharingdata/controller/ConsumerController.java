package com.palmgo.com.cn.sharingdata.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiConsumerInfoResponse;
import com.palmgo.com.cn.sharingdata.service.Interface.ConsumeruserService;

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
@Api(description = "消费业务管理")
@RestController
public class ConsumerController extends CommonController {

	protected static Logger log = CommonLogFactory.getLog();

	@Autowired
	private ConsumeruserService consumeruserService;

	@ApiOperation(value = "消费业务列表", notes = "消费业务管理")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginName", value = "管理账号名", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "business_code", value = "业务代码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "pageIndex", value = "页数", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "totalPerPage", value = "行数", defaultValue = "10", required = false, paramType = "query", dataType = "string"), })
	@RequestMapping(value = "getconsumerinfos", method = RequestMethod.GET)
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiConsumerInfoResponse.class) })
	@ResponseBody
	public ApiConsumerInfoResponse getconsumerinfos(
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "userid", required = false, defaultValue = "") String userid,
			@RequestParam(value = "business_code", required = false, defaultValue = "") String business_code,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "totalPerPage", required = false, defaultValue = "10") int totalPerPage,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(String.format("消费业务列表服务收到信息：%s,%s", userid, business_code));
		ApiConsumerInfoResponse data = null;
		try {
			data = consumeruserService.Getconsumeruser(userid, business_code,loginName,pageIndex, totalPerPage);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiConsumerInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}
}