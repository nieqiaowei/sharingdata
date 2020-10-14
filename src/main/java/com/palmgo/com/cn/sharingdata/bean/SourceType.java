package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class SourceType  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** id**/
	private String id;
	
	/** 名称 **/
	private String name;
	
	/** 驱动 **/
	private String driver_class_name;
	
	/** 查询表 **/
	private String tablesSql;
	
	/** 查询表中的字段 **/
	private String tableFieldSql;
	
	
}
