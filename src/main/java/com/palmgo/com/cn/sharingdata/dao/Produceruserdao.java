package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.Produceruser;
import com.palmgo.com.cn.sharingdata.bean.Tableoffield;

/**
 *  生产者的相关操作
 * @author Administrator
 *
 */
@Mapper
@Repository
public interface Produceruserdao {

	
	/** 生产者的总条数**/
	public int GetproduceruserCount(Map<String, Object> paramMap);
	
	/** 生产者的详细数据**/
	public List<Produceruser> Getproduceruser(Map<String, Object> paramMap);
	
	/** 新增生产者数据 **/
	public int InsertproduceruserInfo(Produceruser produceruser);
	
	/** 创建生产者的业务表 **/
	public int createtableInfo(Map<String, Object> paramMap);
	
	/** 删除生产者 **/
	public int DeleteproduceruserInfo(Produceruser produceruser);
	
	/** 删除生产者的业务表所对应的字段数据 **/
	public int DeletetableoffieldInfo(Map<String, Object> paramMap);
	
	/** 删除生产者的业务表 **/
	public int DROPtableInfo(Map<String, Object> paramMap);
	
	/** 修改生产者 **/
	public int UpdateproduceruserInfo(Produceruser produceruser);
	
	
	
}
