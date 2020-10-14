package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.MonitorInfo;

/**
 *  业务相关操作
 * @author Administrator
 *
 */

@Mapper
@Repository
public interface BussinessTabledao {

	
	/** 数据总数**/
	public int GetbusinesstableCount(Map<String, Object> paramMap);
	
	/** 数据列表**/
	public List<Map<String, Object>> Getbusinesstable(Map<String, Object> paramMap);
	
	/** 保存**/
	public int savebusinesstable(Map<String, Object> paramMap);
	
}
