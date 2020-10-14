package com.palmgo.com.cn.sharingdata.service.Interface;


import com.palmgo.com.cn.sharingdata.bean.DataSessionAndMapper;

public interface MybatisService {

	/**
	 * 初始化数据库连接
	 */
	public DataSessionAndMapper remoteDataSource(String remotejdbc_driver, String remotejdbc_url, String remotejdbc_username,
			String remotejdbc_password) throws Exception;
	
	
	public void closeremoteDataSource(DataSessionAndMapper dataSessionAndMapper)throws Exception;

}
