package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiDataProducerInfoResponse;

public interface DataProducerService {

	/**
	 * 查询
	 * @param id
	 * @param name
	 * @param code
	 * @param tableName
	 * @param datasourceid
	 * @param type
	 * @param pageIndex
	 * @param totalPerPage
	 * @return
	 */
	public ApiDataProducerInfoResponse info(String id, String name, String code, String tableName, String datasourceid,String type,
			int pageIndex, int totalPerPage,String loginName)throws Exception;

	/**
	 * 保存
	 * @param id
	 * @param name
	 * @param code
	 * @param tableName
	 * @param datasourceid
	 * @param countSql
	 * @param selectSql
	 * @param insertSql
	 * @param updateSql
	 * @param deleteSql
	 * @param selectParameter
	 * @param updateParameter
	 * @param insertParameter
	 * @param deleteParameter
	 * @param type
	 * @return
	 */
	public ApiDataProducerInfoResponse saveData(String id, String name, String code, String tableName,
			String datasourceid, String countSql, String selectSql, String insertSql, String updateSql,
			String deleteSql, String selectParameter, String updateParameter, String insertParameter,
			String deleteParameter,String type,String loginName)throws Exception;

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public ApiDataProducerInfoResponse DelInfo(String id,String loginName) throws Exception;

}
