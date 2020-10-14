package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiInfoResponse;

public interface LayerService {

	/**
	 * 	数据接入服务
	 * @param code
	 * @param type
	 * @param username
	 * @param dataBody
	 * @return
	 */
	public ApiInfoResponse recevier(String code, String type , String loginName,String dataBody)throws Exception;
	
	
}
