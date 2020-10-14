package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

/**
 *   数据接入的增删改查可用于消费和生产，消费至考虑查询
 * @author Administrator
 *
 */
@Data
public class DataSourceParameter  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 统计sql **/
	private String countParameter;
	
	/** 查询sql  **/
	private String selectParameter;
	
	/** 写入sql **/
	private String insertParameter;
	
	/** 修改sql **/
	private String updateParameter;
	
	/** 删除sql **/
	private String deleteParameter;
}
