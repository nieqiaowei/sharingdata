package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;
import com.palmgo.com.cn.sharingdata.bean.ApiDataRoleInfoResponse;


public interface DataRoleService {


	/**
	 * 查询
	 * @param id
	 * @param name
	 * @param code
	 * @param pageIndex
	 * @param totalPerPage
	 * @return
	 */
	public ApiDataRoleInfoResponse info(String id,String name, String code,int pageIndex, int totalPerPage,String loginName) throws Exception;


	/**
	 * 保存
	 * @param id
	 * @param name
	 * @param code
	 * @param dataRoleSources
	 * @return
	 */
	public ApiDataRoleInfoResponse Saveinfo(String id,String name, String code, String[] dataRoleSources,String loginName)throws Exception;

	/**
	 * 	删除
	 * @param id
	 * @return
	 */
	public ApiDataRoleInfoResponse DelInfo(String id,String loginName)throws Exception;

}
