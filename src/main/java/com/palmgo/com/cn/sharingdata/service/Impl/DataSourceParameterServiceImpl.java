package com.palmgo.com.cn.sharingdata.service.Impl;


import com.palmgo.com.cn.sharingdata.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceParameterInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.DataSourceParameter;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceParameterService;
import com.palmgo.com.cn.sharingdata.util.RegularUtils;

@Service
public class DataSourceParameterServiceImpl implements DataSourceParameterService {

	public Logger log = CommonLogFactory.getLog();

	@Override
	public ApiDataSourceParameterInfoResponse info(String countSql, String insertSql, String updateSql, String deleteSql,
			String selectSql,String loginName) throws Exception{
		ApiDataSourceParameterInfoResponse data = new ApiDataSourceParameterInfoResponse();
		try {
			DataSourceParameter dataSourceParameter = new DataSourceParameter();
			if(StringUtils.notNullOrBlank(countSql)){
				dataSourceParameter.setCountParameter(
						RegularUtils.getRegularContent(countSql) == null ? "" : RegularUtils.getRegularContent(countSql));
			}
			if(StringUtils.notNullOrBlank(insertSql)){
				dataSourceParameter.setInsertParameter(
						RegularUtils.getRegularContent(insertSql) == null ? "" : RegularUtils.getRegularContent(insertSql));
			}
			if(StringUtils.notNullOrBlank(updateSql)){
				dataSourceParameter.setUpdateParameter(
						RegularUtils.getRegularContent(updateSql) == null ? "" : RegularUtils.getRegularContent(updateSql));
			}
			if(StringUtils.notNullOrBlank(deleteSql)){
				dataSourceParameter.setDeleteParameter(
						RegularUtils.getRegularContent(deleteSql) == null ? "" : RegularUtils.getRegularContent(deleteSql));
			}
			if(StringUtils.notNullOrBlank(selectSql)){
				dataSourceParameter.setSelectParameter(
						RegularUtils.getRegularContent(selectSql) == null ? "" : RegularUtils.getRegularContent(selectSql));
			}
			data.setData(dataSourceParameter);
			data.setCode(MsgCode.code_200);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
			data.setCode(MsgCode.code_500);
			data.setMsg(e.getLocalizedMessage());
		}
		return data;
	}

}
