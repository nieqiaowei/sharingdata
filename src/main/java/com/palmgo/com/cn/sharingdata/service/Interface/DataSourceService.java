package com.palmgo.com.cn.sharingdata.service.Interface;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiSourceTypeInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.DataSourceMapper;

public interface DataSourceService {
	
	
	/**
	 * 获取数据源
	 * @return
	 */
	public Map<String, DataSourceMapper> getsource(String loginName)throws Exception;


	/**
	 * 初始化数据库连接
	 * @param id
	 */
	public void init (String id,String loginName)throws Exception;


	/**
	 * 查询
	 * @param id
	 * @param name
	 * @param username
	 * @param host
	 * @param database
	 * @param pageIndex
	 * @param totalPerPage
	 * @return
	 */
	public ApiDataSourceInfoResponse info(String id,String name, String username, String host, String database,
			int pageIndex, int totalPerPage,String loginName)throws Exception;

	/**
	 * 保存
	 * @param id
	 * @param name
	 * @param username
	 * @param password
	 * @param host
	 * @param port
	 * @param database
	 * @param sourcetypeid
	 * @return
	 */
	public ApiDataSourceInfoResponse saveDataSource(String id,String name, String username, String password, String host,
			String port, String database, String sourcetypeid,String remotejdbc_url,String loginName)throws Exception;

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public ApiDataSourceInfoResponse DelInfo(String id,String loginName)throws Exception;

}
