package com.palmgo.com.cn.sharingdata.service.Impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceColumnInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceSqlInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceTablesInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.DataSource;
import com.palmgo.com.cn.sharingdata.bean.DataSourceColumn;
import com.palmgo.com.cn.sharingdata.bean.DataSourceMapper;
import com.palmgo.com.cn.sharingdata.bean.DataSourceSql;
import com.palmgo.com.cn.sharingdata.bean.DataSourceTables;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceColumnService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceSqlService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceTablesService;

@Service
public class DataSourceSqlServiceImpl implements DataSourceSqlService {

	public Logger log = CommonLogFactory.getLog();

	@Autowired
	private DataSourceService dataSourceService;
	
	@Override
	public ApiDataSourceSqlInfoResponse info(String id , String tableName,String loginName) throws Exception {
		// TODO Auto-generated method stub
		ApiDataSourceSqlInfoResponse data = new ApiDataSourceSqlInfoResponse();
		//通过id查找数据源
		Map<String, DataSourceMapper> map =  dataSourceService.getsource(loginName);
		if(map.containsKey(id)) {
			DataSourceMapper dataSourceMapper = map.get(id);
			DataSource dataSource = dataSourceMapper.getDataSource();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("tableName", tableName);
			paramMap.put("database", dataSource.getDatabase());
			String TableFieldSql = dataSourceMapper.getSourceType().getTableFieldSql();
			SqlMapper sqlMapper = dataSourceMapper.getDataSessionAndMapper().getSqlMapper();
			List<DataSourceColumn> lists = sqlMapper.selectList(sqlMapper.getNewSql(TableFieldSql,dataSourceMapper.getDataSource().getRemotejdbc_url()),paramMap,DataSourceColumn.class);
			if(lists.size() > 0) {
				DataSourceSql dataSourceSql = new DataSourceSql();
				StringBuffer countSql = new StringBuffer("select count(*) as total from ").append(tableName);
				countSql.append(" <where> ");
				StringBuffer selectSql = new StringBuffer("select ");
				StringBuffer select_field = new StringBuffer();
				StringBuffer select_where = new StringBuffer(" 1=1 ");
				StringBuffer insertSql =  new StringBuffer("insert ").append(" into ").append(tableName);
				StringBuffer insert_value = new StringBuffer();
				StringBuffer updateSql =  new StringBuffer("update ").append(tableName).append(" SET ");
				StringBuffer update_field = new StringBuffer();
				StringBuffer update_where = new StringBuffer();
				StringBuffer deleteSql = new StringBuffer("delete from ").append(tableName);
				
				for (DataSourceColumn dataSourceColumn : lists) {
					String Column_name = dataSourceColumn.getColumn_name();
					select_field.append(Column_name).append(",");
					select_where.append("<if test=\""+Column_name+" != '' and  "+Column_name+" != null \">").append("\r\n");
					select_where.append(" and "+Column_name+"= #{"+Column_name+"} ").append("\r\n");
					select_where.append(" </if> ").append("\r\n");
					insert_value.append("#{"+Column_name+"}").append(",");
					update_field.append(""+Column_name+" = #{"+Column_name+"}").append(",");
				}
				//统计
				countSql.append(select_where);
				countSql.append(" </where> ");
				//查询
				selectSql.append(select_field.delete(select_field.length()-1, select_field.length()));
				selectSql.append(" ").append(" from ").append(tableName).append("\r\n");
				selectSql.append(" <where> ").append("\r\n");
				selectSql.append(select_where);
				//增加分页参数
				selectSql.append("<if test=\"totalPerPage != 0\">").append("\r\n");
				selectSql.append(" limit #{pageIndex} , #{totalPerPage}").append("\r\n");
				selectSql.append("</if>").append("\r\n");
				selectSql.append(" </where> ");
				//修改
				updateSql.append(update_field.delete(update_field.length()-1, update_field.length()));
				updateSql.append(" <where> ");
				updateSql.append(update_where.append(" 1=1 "));
				updateSql.append(" </where> ");
				//新增
				insertSql.append(" ( ");
				insertSql.append(select_field);
				insertSql.append(" ) ");
				insertSql.append(" VALUES ");
				insertSql.append(" ( ");
				insertSql.append(insert_value.delete(insert_value.length()-1, insert_value.length()));
				insertSql.append(" ) ");
				//删除
				deleteSql.append(" <where> ");
				deleteSql.append(select_where);
				deleteSql.append(" </where> ");
				
				dataSourceSql.setCountSql(countSql.toString());
				dataSourceSql.setSelectSql(selectSql.toString());
				dataSourceSql.setInsertSql(insertSql.toString());
				dataSourceSql.setUpdateSql(updateSql.toString());
				dataSourceSql.setDeleteSql(deleteSql.toString());
				data.setData(dataSourceSql);
				data.setCode(MsgCode.code_200);
			}else {
				data.setCode(MsgCode.code_404);
			}
		}else {
			data.setCode(MsgCode.code_401);
		}
		return data;
	}


}
