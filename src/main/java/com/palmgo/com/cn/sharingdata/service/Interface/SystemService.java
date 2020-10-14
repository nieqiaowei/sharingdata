package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiSystemInfoResponse;

public interface SystemService {

	
	/**
	 *  查询所有数据
	 * @return
	 */
	public ApiSystemInfoResponse info(String loginName)throws Exception;


	/**
	 *
	 * 存储数据
	 * @param private_key
	 * @param sysname
	 * @param license
	 * @param sysaddress
	 * @param systemid
	 * @param dataBody
	 * @param type
	 * @return
	 */
	public ApiSystemInfoResponse SaveUserInfo(
			String loginName,
			String private_key,
			String public_key,
			String sysname, 
			String license,
			String sysaddress,
			String systemid,
			String dataBody,
			String type)throws Exception;
}
