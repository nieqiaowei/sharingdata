package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

/**
 *  数据源
 * @author Administrator
 *
 */
@Data
public class DataSourceTables  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 表名称**/
	private String tableName;
}
