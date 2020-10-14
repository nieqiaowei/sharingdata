package com.palmgo.com.cn.sharingdata.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.SystemProPerties;

@Mapper
@Repository
public interface Systemdao {

	
	/** 数据**/
	public SystemProPerties GetSystem(Map<String, Object> paramMap);
	
	/** 修改数据**/
	public int UpdateSystemInfo(SystemProPerties systemProPerties);
	
}
