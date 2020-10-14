package com.palmgo.com.cn.sharingdata.service.Impl;

import com.palmgo.com.cn.sharingdata.util.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.DataSessionAndMapper;
import com.palmgo.com.cn.sharingdata.service.Interface.MybatisService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class MybatisServiceImpl implements MybatisService {

	public Logger log = CommonLogFactory.getLog();
	
	public DataSessionAndMapper remoteDataSource(String remotejdbc_driver,String remotejdbc_url,
			String remotejdbc_username,String remotejdbc_password) throws Exception {
		SqlMapper sqlMapper = null;
		DataSessionAndMapper dataSessionAndMapper = null;
		HikariDataSource remotedataSource = null;
		remotedataSource = new HikariDataSource();
		remotedataSource.setDriverClassName(remotejdbc_driver);
		remotedataSource.setJdbcUrl(remotejdbc_url);
		if(StringUtils.notNullOrBlank(remotejdbc_username)){
			remotedataSource.setUsername(remotejdbc_username);
		}
		if(StringUtils.notNullOrBlank(remotejdbc_password)){
			remotedataSource.setPassword(remotejdbc_password);
		}
		remotedataSource.setAutoCommit(true);
		//连接只读数据库时配置为true， 保证安全
		remotedataSource.setReadOnly(false);
		//等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒
		remotedataSource.setConnectionTimeout(30000);
		//一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
		remotedataSource.setIdleTimeout(30000);
		//一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒
		remotedataSource.setMaxLifetime(1800000);
		remotedataSource.setPoolName("DatebookHikariCP");
		//连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)
		remotedataSource.setMaximumPoolSize(50);
		remotedataSource.setValidationTimeout(3000);
		remotedataSource.setMinimumIdle(10);
		remotedataSource.setLoginTimeout(5);
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(remotedataSource);
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
		SqlSession sqlsession = sqlSessionFactory.openSession();
		sqlMapper= new SqlMapper(sqlsession);
		dataSessionAndMapper = new DataSessionAndMapper();
		dataSessionAndMapper.setSqlsession(sqlsession);
		dataSessionAndMapper.setSqlMapper(sqlMapper);
		return dataSessionAndMapper;
	}
	
	
	public void closeremoteDataSource(DataSessionAndMapper dataSessionAndMapper) throws Exception {
		if(dataSessionAndMapper!=null){
			if(dataSessionAndMapper.getSqlsession()!= null) {
				dataSessionAndMapper.getSqlsession().close();
			}
		}
	}
}
