package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class RecevierDataInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String data;
	
	private String tableName;
	
	private String business_code;
	
	private String userid;
}
