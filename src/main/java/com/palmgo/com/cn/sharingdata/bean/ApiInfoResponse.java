package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ApiInfoResponse implements Serializable{
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
	private List<Map<String, Object>> data;

	
}