package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

/**
 *  数据源
 * @author Administrator
 *
 */
@Data
public class DataSourceColumn  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 字段名称**/
	private String column_name;
	
	/** 字段类型 **/
	private String data_type;
}
