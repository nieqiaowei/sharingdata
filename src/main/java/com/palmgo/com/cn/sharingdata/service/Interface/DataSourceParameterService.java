package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceParameterInfoResponse;

public interface DataSourceParameterService {

	/**
	 * 查询
	 * @param countSql
	 * @param insetSql
	 * @param updateSql
	 * @param deleteSql
	 * @param selectSql
	 * @return
	 */
	public ApiDataSourceParameterInfoResponse info(String countSql, String insetSql, String updateSql, String deleteSql,
			String selectSql,String loginName)throws Exception;

}
