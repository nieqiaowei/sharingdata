package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiDataProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiDataRoleInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.DataProducer;
import com.palmgo.com.cn.sharingdata.bean.DataRole;
import com.palmgo.com.cn.sharingdata.bean.DataRoleSource;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.DataRoleSourcedao;
import com.palmgo.com.cn.sharingdata.dao.DataRoledao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataProducerService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataRoleService;
import com.palmgo.com.cn.sharingdata.util.DateUitls;
import com.palmgo.com.cn.sharingdata.util.JsonUtils;

import lombok.Data;

@Data
@Service
public class DataRoleServiceImpl implements DataRoleService {

	public Logger log = CommonLogFactory.getLog();

	@Autowired
	private DataRoledao dao;
	
	@Autowired
	private DataProducerService dataProducerService;
	
	@Autowired
	private DataRoleSourcedao dataRoleSourcedao;

	@Autowired
	private CommonService commonService;

	@Override
	public ApiDataRoleInfoResponse info(String id, String name, String code, int pageIndex, int totalPerPage,String loginName)throws Exception {
		// TODO Auto-generated method stub
		ApiDataRoleInfoResponse data = new ApiDataRoleInfoResponse();
		try {
			if (pageIndex == 0) {
				// 无需分页处理
				totalPerPage = 0;
			} else {
				pageIndex = (pageIndex - 1) * totalPerPage;
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", id);
			paramMap.put("name", name);
			paramMap.put("code", code);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("totalPerPage", totalPerPage);
			int total = dao.getDataRoleCount(paramMap);
			List<DataRole> infos = dao.getDataRoleInfo(paramMap);
			if (infos.size() > 0) {
				data.setCode(MsgCode.code_200);
				for (DataRole dataRole : infos) {
					paramMap = new HashMap<>();
					paramMap.put("roleid", dataRole.getId());
					List<DataRoleSource> dataRoleSources =  dataRoleSourcedao.getRoleToDataSourceInfo(paramMap);
					for (DataRoleSource dataRoleSource : dataRoleSources) {
						String datasourceroleid = dataRoleSource.getDatasourceroleid();
						ApiDataProducerInfoResponse apiDataProducerInfoResponse =  dataProducerService.info(datasourceroleid, null, null, null, null, null, 0, 0,loginName);
						if(apiDataProducerInfoResponse.getCode() == MsgCode.code_200) {
							dataRoleSource.setDatasourcerolename(apiDataProducerInfoResponse.getData().get(0).getName());
							dataRoleSource.setDatasourcerolecode(apiDataProducerInfoResponse.getData().get(0).getCode());
						}
					}
					dataRole.setDataRoleSources(dataRoleSources);
				}
				data.setData(infos);
				data.setTotal(total);
			} else {
				data.setCode(MsgCode.code_404);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getLocalizedMessage(), e);
			data.setCode(MsgCode.code_500);
			data.setMsg(e.getLocalizedMessage());
		}
		return data;
	}

	@Override
	public ApiDataRoleInfoResponse Saveinfo(String id, String name, String code, String[] dataRoleSources,String loginName) {
		// TODO Auto-generated method stub
		ApiDataRoleInfoResponse data = new ApiDataRoleInfoResponse();
		try {
			
			if (StringUtils.isNullOrBlank(id)) {
				id = UUID.randomUUID().toString().replaceAll("-", "");
			}
			DataRole dataRole = new DataRole();
			dataRole.setId(id);
			dataRole.setName(name);
			dataRole.setCode(code);
			dataRole.setCreatetime(DateUitls.getNowDateTime());
			List<DataRoleSource> dataRoleSources_new = new ArrayList<>();
			for (String roleProducerId : dataRoleSources) {
				DataRoleSource dataRoleSource = new DataRoleSource();
				dataRoleSource.setDatasourceroleid(roleProducerId);
				dataRoleSource.setRoleid(id);
				dataRoleSource.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				dataRoleSources_new.add(dataRoleSource);
			}
			//需要将角色和业务关联起来
			//删除
			for (DataRoleSource dataRoleSource : dataRoleSources_new) {
				dataRoleSourcedao.delRoleToDataSource(dataRoleSource);
			}
			//新增
			for (DataRoleSource dataRoleSource : dataRoleSources_new) {
				dataRoleSourcedao.saveRoleToDataSource(dataRoleSource);
			}
			int ok = dao.saveDataRole(dataRole);
			data.setCode(MsgCode.code_200);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getLocalizedMessage(), e);
			data.setCode(MsgCode.code_500);
			data.setMsg(e.getLocalizedMessage());
		}
		return data;
	}

	@Override
	public ApiDataRoleInfoResponse DelInfo(String id,String loginName) throws Exception{
		// TODO Auto-generated method stub
		ApiDataRoleInfoResponse data = new ApiDataRoleInfoResponse();
		if (StringUtils.isNullOrBlank(id)) {
			data.setCode(MsgCode.code_401);
		} else {
			DataRoleSource dataRoleSource = new DataRoleSource();
			dataRoleSource.setRoleid(id);
			Object ok = dataRoleSourcedao.delRoleToDataSource(dataRoleSource);
			DataRole dataRole = new DataRole();
			dataRole.setId(id);
			ok = dao.delDataRole(dataRole);
			data.setCode(MsgCode.code_200);
		}
		return data;
	}
}
