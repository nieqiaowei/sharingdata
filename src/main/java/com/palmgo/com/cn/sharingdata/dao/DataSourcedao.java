package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.DataSource;
import com.palmgo.com.cn.sharingdata.bean.SourceType;
import com.palmgo.com.cn.sharingdata.bean.SystemProPerties;

@Mapper
@Repository
public interface DataSourcedao {

	/** 总数 **/
	public int getDataSourceCount(Map<String, Object> paramMap);
	
	/** 数据**/
	public List<DataSource> getDataSourceInfo(Map<String, Object> paramMap);
	
	/** 保存数据**/
	public int saveDataSource(DataSource dataSource);
	
	/** 删除数据**/
	public int delDataSource(DataSource dataSource);
	
}
