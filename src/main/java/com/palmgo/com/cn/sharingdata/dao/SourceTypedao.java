package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.SourceType;
import com.palmgo.com.cn.sharingdata.bean.SystemProPerties;

@Mapper
@Repository
public interface SourceTypedao {

	
	/** 数据**/
	public List<SourceType> getSourceTypeInfo(SourceType sourceType);
	
	/** 保存数据**/
	public int saveSourceType(SourceType sourceType);
	
	/** 删除数据**/
	public int delSourceType(SourceType sourceType);
	
}
