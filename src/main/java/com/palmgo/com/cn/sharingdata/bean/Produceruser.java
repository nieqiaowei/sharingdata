package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Produceruser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 用户id **/
	private String Userid;
	/** 业务代码**/
	private String business_code;
	/** 业务名称**/
	private String business_name;
	/** 数据表名称**/
	private String tableName;
	/** 数据接收地址**/
	private String address;
	/** 数据接收的格式（json）**/
	private String dataformat;
	/** 数据查询sql**/
	private String selectsql;
	/** 修改sql**/
	private String updatesql;
	/** 删除sql**/
	private String delsql;
	/** 操作类型**/
	private String type;
	/** 数据结构**/
	private List<Tableoffield> tableoffields;
}
