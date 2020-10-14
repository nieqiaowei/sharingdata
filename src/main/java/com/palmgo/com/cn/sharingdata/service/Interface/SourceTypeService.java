package com.palmgo.com.cn.sharingdata.service.Interface;


import com.palmgo.com.cn.sharingdata.bean.ApiSourceTypeInfoResponse;

public interface SourceTypeService {


	/**
	 * 查询
	 * @param id
	 * @param name
	 * @return
	 */
	public ApiSourceTypeInfoResponse info(String id , String name,String loginName)throws Exception;


	/***
	 * 保存
	 * @param id
	 * @param name
	 * @param driver_class_name
	 * @param tablesSql
	 * @param tableFieldSql
	 * @return
	 */
	public ApiSourceTypeInfoResponse SaveInfo(String id , String name, String driver_class_name,String tablesSql, String tableFieldSql,String loginName)throws Exception;


	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public ApiSourceTypeInfoResponse DelInfo(String id,String loginName)throws Exception;

	
}
