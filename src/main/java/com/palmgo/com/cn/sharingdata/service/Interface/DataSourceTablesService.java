package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceTablesInfoResponse;

public interface DataSourceTablesService {

	/**
	 * 查询 查询表名称
	 * @param id
	 * @param tableName
	 * @return
	 */
	public ApiDataSourceTablesInfoResponse info(String id , String tableName,String loginName)throws Exception;

	

}
