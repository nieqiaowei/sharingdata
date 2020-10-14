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
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceTablesInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.DataSource;
import com.palmgo.com.cn.sharingdata.bean.DataSourceColumn;
import com.palmgo.com.cn.sharingdata.bean.DataSourceMapper;
import com.palmgo.com.cn.sharingdata.bean.DataSourceTables;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceColumnService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceTablesService;

@Service
public class DataSourceColumnServiceImpl implements DataSourceColumnService {

	public Logger log = CommonLogFactory.getLog();

	@Autowired
	private DataSourceService dataSourceService;
	
	@Override
	public ApiDataSourceColumnInfoResponse info(String id , String tableName,String loginName)throws Exception {
		// TODO Auto-generated method stub
		ApiDataSourceColumnInfoResponse data = new ApiDataSourceColumnInfoResponse();
		try {
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
				List<DataSourceColumn> lists = sqlMapper.selectList(sqlMapper.getNewSql(TableFieldSql,dataSource.getRemotejdbc_url()),paramMap,DataSourceColumn.class);
				data.setData(lists);
				data.setCode(MsgCode.code_200);
			}else {
				data.setCode(MsgCode.code_401);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			data.setCode(MsgCode.code_500);
			data.setMsg(e.getLocalizedMessage());
		}
		return data;
	}


}
