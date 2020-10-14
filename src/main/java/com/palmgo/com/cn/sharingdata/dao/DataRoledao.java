package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.DataRole;
import com.palmgo.com.cn.sharingdata.bean.DataSource;

@Mapper
@Repository
public interface DataRoledao {

	/** 总数 **/
	public int getDataRoleCount(Map<String, Object> paramMap);
	
	/** 数据**/
	public List<DataRole> getDataRoleInfo(Map<String, Object> paramMap);
	
	/** 保存数据**/
	public int saveDataRole(DataRole dataRole);
	
	/** 删除数据**/
	public int delDataRole(DataRole dataRole);
	
}
