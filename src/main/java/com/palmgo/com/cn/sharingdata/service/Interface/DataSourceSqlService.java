package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceSqlInfoResponse;

public interface DataSourceSqlService {


	/**
	 * 查询
	 * @param id
	 * @param tableName
	 * @return
	 */
	public ApiDataSourceSqlInfoResponse info(String id , String tableName,String loginName)throws Exception;

	

}
