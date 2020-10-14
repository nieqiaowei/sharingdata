package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 用户主键id **/
	private String userid;
	
	/** 用户账号**/
	private String username;
	
	/** 用户密码**/
	private String password;
	
	/** 公钥 **/
	private String public_key;
	
	/** 用户私钥**/
	private String private_key;
	
	/** 角色id**/
	private String roleid;
	
	/** 角色name**/
	private String roleName;
	
	/** 用户姓名**/
	private String name;
	
	/** 创建用的名称 **/
	private String createName;
	
	/** 创建的时间**/
	private String createTime;
	
	/** 修改的时间**/
	private String updateTime;
	
	/** 过期时间**/
	private String overTime;
	

}
