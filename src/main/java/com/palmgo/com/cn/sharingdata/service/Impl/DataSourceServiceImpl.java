package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiDataSourceInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiSourceTypeInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.DataSessionAndMapper;
import com.palmgo.com.cn.sharingdata.bean.DataSource;
import com.palmgo.com.cn.sharingdata.bean.DataSourceMapper;
import com.palmgo.com.cn.sharingdata.bean.SourceType;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.DataSourcedao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceService;
import com.palmgo.com.cn.sharingdata.service.Interface.MybatisService;
import com.palmgo.com.cn.sharingdata.service.Interface.SourceTypeService;
import com.palmgo.com.cn.sharingdata.util.DateUitls;

import lombok.Data;

@Data
@Service
public class DataSourceServiceImpl implements DataSourceService {

    public Logger log = CommonLogFactory.getLog();

    /**
     * 初始化之后的数据库连接
     **/
    public Map<String, DataSourceMapper> source = new HashMap<>();

    @Autowired
    private DataSourcedao dao;

    @Autowired
    private SourceTypeService sourceTypeService;
    @Autowired
    private MybatisService mybatisService;

    @Autowired
    private CommonService commonService;


    @Override
    public ApiDataSourceInfoResponse info(String id, String name, String username, String host, String database, int pageIndex,
                                          int totalPerPage, String loginName) throws Exception {
        ApiDataSourceInfoResponse data = new ApiDataSourceInfoResponse();
        if (pageIndex == 0) {
            // 无需分页处理
            totalPerPage = 0;
        } else {
            pageIndex = (pageIndex - 1) * totalPerPage;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("username", username);
        paramMap.put("name", name);
        paramMap.put("host", host);
        paramMap.put("database", database);
        paramMap.put("pageIndex", pageIndex);
        paramMap.put("totalPerPage", totalPerPage);
        int total = dao.getDataSourceCount(paramMap);
        List<DataSource> infos = dao.getDataSourceInfo(paramMap);
        if (infos.size() > 0) {
            for (DataSource dataSource : infos) {
                ApiSourceTypeInfoResponse apiSourceTypeInfoResponse = sourceTypeService.info(dataSource.getSourcetypeid(), null,loginName);
                if (apiSourceTypeInfoResponse.getCode() == MsgCode.code_200) {
                    dataSource.setSourcetypeid(apiSourceTypeInfoResponse.getData().get(0).getName());
                }
            }
            data.setTotal(total);
            data.setCode(MsgCode.code_200);
            data.setData(infos);
        } else {
            data.setCode(MsgCode.code_404);
        }
        return data;
    }

    @Override
    public ApiDataSourceInfoResponse saveDataSource(String id, String name, String username, String password, String host,
                                                    String port, String database, String sourcetypeid,String remotejdbc_url, String loginName) throws Exception {
        ApiDataSourceInfoResponse data = new ApiDataSourceInfoResponse();
        DataSource dataSource = new DataSource();

        if (StringUtils.isNullOrBlank(id)) {
            id = UUID.randomUUID().toString().replaceAll("-", "");
        }
        dataSource.setId(id);
        dataSource.setName(name);
        dataSource.setHost(host);
        dataSource.setPort(port);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDatabase(database);
        dataSource.setSourcetypeid(sourcetypeid);
        dataSource.setCreatetime(DateUitls.getNowDateTime());
        dataSource.setRemotejdbc_url(remotejdbc_url);
        Object ok = dao.saveDataSource(dataSource);
        init(id,loginName);
        data.setCode(MsgCode.code_200);
        return data;
    }

    @Override
    public ApiDataSourceInfoResponse DelInfo(String id, String loginName) throws Exception {
        ApiDataSourceInfoResponse data = new ApiDataSourceInfoResponse();
        if (StringUtils.isNullOrBlank(id)) {
            data.setCode(MsgCode.code_401);
        } else {
            DataSource dataSource = new DataSource();
            dataSource.setId(id);
            Object ok = dao.delDataSource(dataSource);
            data.setCode(MsgCode.code_200);
        }
        return data;
    }

    @PostConstruct
    public void initsource() {
        //应该设定定时任务的更新内存中的数据库连接信息，确保连接被关闭和失效
        init(null,null);
    }

    @Override
    public void init(String id,String loginName) {
        // TODO Auto-generated method stub
        //初始化数据库
        try {
            ApiDataSourceInfoResponse data = info(id, null, null, null, null, 0, 0,loginName);
            if (data.getCode() == MsgCode.code_200) {
                List<DataSource> dataSources = data.getData();
                for (DataSource dataSource : dataSources) {
                    String idstr = dataSource.getId();
                    String Sourcetypeid = dataSource.getSourcetypeid();
                    ApiSourceTypeInfoResponse SourceTypeInfoResponse = sourceTypeService.info(null, Sourcetypeid,loginName);
                    if (SourceTypeInfoResponse.getCode() == MsgCode.code_200) {
                        DataSourceMapper dataSourceMapper = new DataSourceMapper();
                        SourceType sourceType = SourceTypeInfoResponse.getData().get(0);
                        DataSessionAndMapper dataSessionAndMapper = null;
                        String remotejdbc_url = null;
                        if(StringUtils.notNullOrBlank(dataSource.getRemotejdbc_url())){
                            remotejdbc_url = dataSource.getRemotejdbc_url();
                        }
                        /*
                        if (sourceType.getName().toString().toUpperCase().equals("MYSQL")) {
                            remotejdbc_url = "jdbc:" + sourceType.getName().toLowerCase() + "://" + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDatabase() + "?useUnicode=true&characterEncoding=utf-8";
                        } else if (sourceType.getName().toString().toUpperCase().equals("POSTGRESQL")) {
                            remotejdbc_url = "jdbc:" + sourceType.getName().toLowerCase() + "://" + dataSource.getHost() + ":" + dataSource.getPort() + "/" + dataSource.getDatabase() + "?useSSL=false";
                        }
                         */
                        log.info("初始化数据源：" + remotejdbc_url);
                        try {
                            if(source.containsKey(idstr)){
                                //数据连接更新
                                dataSessionAndMapper = source.get(idstr).getDataSessionAndMapper();
                                mybatisService.closeremoteDataSource(dataSessionAndMapper);
                            }
                            dataSourceMapper.setDataSource(dataSource);
                            dataSourceMapper.setSourceType(sourceType);
                            dataSessionAndMapper = mybatisService.remoteDataSource(sourceType.getDriver_class_name(), remotejdbc_url, dataSource.getUsername(), dataSource.getPassword());
                            dataSourceMapper.setDataSessionAndMapper(dataSessionAndMapper);
                        }catch (Exception e){
                            log.error(e.getLocalizedMessage(), e);
                        }
                        source.put(idstr, dataSourceMapper);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Map<String, DataSourceMapper> getsource(String loginName) {
        // TODO Auto-generated method stub
        return source;
    }


}
