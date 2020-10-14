package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 *  数据源
 * @author Administrator
 *
 */
@Data
public class DataRole  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** id**/
	private String id;
	
	/** 名称 **/
	private String name;
	
	/** 代码 **/
	private String code;
	
	/** 创建时间 **/
	private String createtime;
	
	/** 数据业务集合**/
	private List<DataRoleSource> dataRoleSources;
}
