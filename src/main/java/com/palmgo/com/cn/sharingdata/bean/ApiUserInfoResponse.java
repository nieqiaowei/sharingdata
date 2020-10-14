package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 *  输入输出数据格式
 * @author Administrator
 *
 */
@Data
public class ApiUserInfoResponse implements Serializable {
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
	 * 认证标识
	 */
	private String Authorization;


	/** **
	 * 公钥
	 */
	private String publicKey;

	/**
	 * 加密之后的密码
	 */
	private String pwdrsa;
	
	/**
	 * 数据
	 */
	private List<UserInfo> data;

	
}