package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.DataRoleSource;

@Mapper
@Repository
public interface DataRoleSourcedao {

	/** 总数 **/
	public int getRoleToDataSourceCount(Map<String, Object> paramMap);
	
	/** 数据**/
	public List<DataRoleSource> getRoleToDataSourceInfo(Map<String, Object> paramMap);
	
	/** 保存数据**/
	public int saveRoleToDataSource(DataRoleSource dataRoleSource);
	
	/** 删除数据**/
	public int delRoleToDataSource(DataRoleSource dataRoleSource);
	
}
