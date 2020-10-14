package com.palmgo.com.cn.sharingdata.controller;

import com.palmgo.com.cn.sharingdata.conf.SwaggerConfig;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses({
	@ApiResponse( code = 401, message = "校验数据合法性失败"),
	@ApiResponse( code = 402, message = "数据查询异常"),
	@ApiResponse( code = 403, message = "数据输入不详细"),
	@ApiResponse( code = 404, message = "数据查询为空"),
	@ApiResponse( code = 405, message = "数据查询为空"),
	@ApiResponse( code = 406, message = "坐标点与最近的道路距离超过阈值"),
	@ApiResponse( code = 407, message = "测试数据"),
	@ApiResponse( code = 500, message = "系统内部错误")
})
@RequestMapping(value=SwaggerConfig.DEFAULT_PATH)
@CrossOrigin(origins = "*")
public class CommonController {

}
