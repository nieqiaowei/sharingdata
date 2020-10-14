package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class MonitorInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户名称 **/
	private String username;
	/** 处理的条数**/
	private String countnum;
	/** 处理的时间**/
	private String updatetime;
	/** 处理的类型**/
	private String type;
}
