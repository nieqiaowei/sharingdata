package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class Tableoffield  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 表名称**/
	private String tableNamekey;
	/** 字段名称**/
	private String fieldNamekey;
	/** 字段类型**/
	private String fieldTypekey;
	/** 字段长度**/
	private String fieldLengthkey;
	/** 字段小数点数**/
	private String decimalkey;
	/** 是否为空 true 为空  false 不为空**/
	private boolean nonullkey;
	/** 是否为空 nonullkey **/
	private String isNull;
	/** 数据描述**/
	private String commentkey;
	/** 主键 false 不是 true 是**/
	private boolean primary_key;
	
}
