package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.DataProducer;

@Mapper
@Repository
public interface DataProducerdao {

	/** 总数 **/
	public int getDataProducerCount(Map<String, Object> paramMap);
	
	/** 数据**/
	public List<DataProducer> getDataProducerInfo(Map<String, Object> paramMap);
	
	/** 保存数据**/
	public int saveDataProducer(DataProducer dataProducer);
	
	/** 删除数据**/
	public int delDataProducer(DataProducer dataProducer);
	
}
