package com.palmgo.com.cn.sharingdata.bean;

import java.io.Serializable;

import lombok.Data;

/**
 *  数据发布
 * @author Administrator
 *
 */
@Data
public class DataProducer  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** id**/
	private String id;
	
	/** 数据名称 **/
	private String name;
	
	/** 业务代码 **/
	private String code;
	
	/** 表名称 **/
	private String tableName;
	
	/** 数据源id **/
	private String datasourceid;
	
	/** 数据源id **/
	private String datasourcename;
	
	/** 统计sql **/
	private String countSql;
	
	/** 查询sql **/
	private String selectSql;
	
	/** 新增sql **/
	private String insertSql;
	
	/** 修改sql **/
	private String updateSql;
	
	/** 删除sql **/
	private String deleteSql;
	
	/** 创建时间 **/
	private String createtime;
	
	/** 查询参数 **/
	private String selectParameter;
	
	/** 修改参数**/
	private String updateParameter;
	
	/** 新增参数**/
	private String insertParameter;
	
	/** 删除参数**/
	private String deleteParameter;
	
	/** 0 生成者 1 消费 **/
	private String  type;
	
	/** 系统配置**/
	private SystemProPerties systemProPerties;
}
