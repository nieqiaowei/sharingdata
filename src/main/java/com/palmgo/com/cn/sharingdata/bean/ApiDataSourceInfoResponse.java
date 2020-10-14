package com.palmgo.com.cn.sharingdata.bean;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ApiDataSourceInfoResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 列表的总条数
	 */
	private long total;
	/**
	 * 状态码
	 */
	private int code;
	
	/**
	 * 描述
	 */
	private String msg;
	/**
	 * 数据
	 */
	private List<DataSource> data;

	
}