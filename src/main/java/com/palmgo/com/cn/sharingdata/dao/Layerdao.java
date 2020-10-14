package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface Layerdao {

	
	/** 数据Count**/
	public int GetLayerCount(Map<String, Object> paramMap);
	
	/** 数据Info**/
	public List<Map<String, Object>> GetLayerInfo(Map<String, Object> paramMap);
	
	/**
	 * 新增或者修改
	 * @param paramMap
	 * @return
	 */
	public int SaveLayerInfo(Map<String, Object> paramMap);
}
