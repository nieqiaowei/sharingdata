package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.Consumeruser;

/**
 * 消费者的相关操作
 * 
 * @author Administrator
 *
 */
@Mapper
@Repository
public interface Consumeruserdao {

	/** 消费者的总条数 **/
	public int GetconsumeruserCount(Map<String, Object> paramMap);

	/** 消费者的详细数据 **/
	public List<Consumeruser> Getconsumeruser(Map<String, Object> paramMap);

	/** 新增|修改消费者数据 **/
	public int SaveconsumeruserInfo(Map<String, Object> paramMap);

	/** 删除消费者 **/
	public int DeleteconsumeruserInfo(Consumeruser consumeruser);


}
