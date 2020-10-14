package com.palmgo.com.cn.sharingdata.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.palmgo.com.cn.sharingdata.bean.ApiSourceTypeInfoResponse;
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
import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.service.Interface.ConsumeruserService;
import com.palmgo.com.cn.sharingdata.service.Interface.ProduceruserService;

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
@Api(description = "生产业务管理")
@RestController
public class ProducerController extends CommonController {

	protected static Logger log = CommonLogFactory.getLog();

	@Autowired
	private ProduceruserService produceruserService;

	@ApiOperation(value = "生产业务列表", notes = "生产业务管理")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginName", value = "管理账号名", defaultValue = "admin", required = true, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "business_code", value = "业务代码", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "tableName", value = "业务表名称", defaultValue = "", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "pageIndex", value = "页数", defaultValue = "1", required = false, paramType = "query", dataType = "string"),
			@ApiImplicitParam(name = "totalPerPage", value = "行数", defaultValue = "10", required = false, paramType = "query", dataType = "string"), })
	@RequestMapping(value = "getproducerinfos", method = RequestMethod.GET)
	@ApiResponses({ @ApiResponse(code = 200, message = "成功", response = ApiProducerInfoResponse.class) })
	@ResponseBody
	public ApiProducerInfoResponse getproducerinfos(
			@RequestParam(value = "loginName", required = true, defaultValue = "") String loginName,
			@RequestParam(value = "userid", required = false, defaultValue = "") String userid,
			@RequestParam(value = "business_code", required = false, defaultValue = "") String business_code,
			@RequestParam(value = "tableName", required = false, defaultValue = "") String tableName,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(value = "totalPerPage", required = false, defaultValue = "10") int totalPerPage,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(String.format("生产业务列表服务收到信息：%s,%s,%s", userid, business_code,tableName));
		ApiProducerInfoResponse data = null;
		try {
			data = produceruserService.Getproduceruser(userid, business_code,tableName,loginName, pageIndex, totalPerPage);
		} catch (Exception e) {
			log.error(e.getCause());
			data = new ApiProducerInfoResponse();
			data.setCode(MsgCode.code_500);
			data.setMsg(String.format("%s",e.getCause()));
		}
		return data;
	}
}