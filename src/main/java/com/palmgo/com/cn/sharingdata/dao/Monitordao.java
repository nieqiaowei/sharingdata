package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.MonitorInfo;

/**
 *  监控相关操作
 * @author Administrator
 *
 */

@Mapper
@Repository
public interface Monitordao {

	
	/** 数据总数**/
	public int GetmonitorCount(Map<String, Object> paramMap);
	
	/** 数据列表**/
	public List<MonitorInfo> Getmonitor(Map<String, Object> paramMap);
	
}
