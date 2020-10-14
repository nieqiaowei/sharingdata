package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Consumeruser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 消费者id **/
	private String id;
	/** 用户id**/
	private String userid;
	/** 业务代码 **/
	private String business_code;
	/** 字段名称**/
	private String fields;
	/** 是否推送**/
	private String push;
	/** 推送地址 **/
	private String pushadress;
	/** 推送的频道**/
	private String pushtopic;
	/**  推送tag **/
	private String pushtag;
	/** 历史数据查询地址**/
	private String oldaddress;
	/** 到期时间 **/
	private String overdueteime;
	/** 数据库查询条件 **/
	private String sql;
	/** 数据查询参数-动态（内容从sql中的提取） **/
	private String dataformat;
}
