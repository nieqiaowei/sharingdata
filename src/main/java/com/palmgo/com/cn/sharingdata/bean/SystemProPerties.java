package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class SystemProPerties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键id **/
	private String systemid;
	/** 私钥 **/
	private String private_key;
	/** 公钥 **/
	private String public_key;
	/** 系统名称 **/
	private String sysname;
	/** 许可码**/
	private String license;
	/** 系统地址 **/
	private String sysaddress;
	/** 消息体**/
	private String data;
	/** 操作类型**/
	private String type;
	/** 系统到期时间 **/
	private String overtime;
}
