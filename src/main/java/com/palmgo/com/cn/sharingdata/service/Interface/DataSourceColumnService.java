package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceColumnInfoResponse;

public interface DataSourceColumnService {


	/**
	 * 查询
	 * @param id
	 * @param tableName
	 * @return
	 */
	public ApiDataSourceColumnInfoResponse info(String id , String tableName,String loginName)throws Exception;

	

}
