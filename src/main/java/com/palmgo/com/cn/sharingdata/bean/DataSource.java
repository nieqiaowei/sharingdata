package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

/**
 *  数据源
 * @author Administrator
 *
 */
@Data
public class DataSource  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** id**/
	private String id;
	
	/** 数据名称 **/
	private String name;
	
	/** 用户名 **/
	private String username;
	
	/** 密码 **/
	private String password;
	
	/** ip **/
	private String host;
	
	/** 端口 **/
	private String port;
	
	/** 数据库 **/
	private String database;
	
	/** 数据源类型 **/
	private String sourcetypeid;
	
	/** 创建时间 **/
	private String createtime;

	/** 连接地址 **/
	private String remotejdbc_url;
}
