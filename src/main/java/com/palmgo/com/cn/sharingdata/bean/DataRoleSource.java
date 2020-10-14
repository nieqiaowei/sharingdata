package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

/**
 *   	角色与数据的关联
 * @author Administrator
 *
 */
@Data
public class DataRoleSource  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** id**/
	private String id;
	
	/** 角色id **/
	private String roleid;
	
	/** 数据发布或者数据消费id **/
	private String datasourceroleid;
	
	/** 数据发布或者数据消费name**/
	private String datasourcerolename;

	/** 业务代码 **/
	private String datasourcerolecode;

	
}
