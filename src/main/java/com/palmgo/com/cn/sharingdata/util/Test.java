package com.palmgo.com.cn.sharingdata.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.User;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.palmgo.com.cn.sharingdata.bean.UserInfo;
import com.palmgo.com.cn.sharingdata.dao.Userdao;
import com.zaxxer.hikari.HikariDataSource;

public class Test {

	
	/** 变量:连接池对象,类型:ComboPooledDataSource */
	private transient HikariDataSource remotedataSource  = null;
	
	public static void main(String[] args) {
		
		Test  test = new Test();
		try {
			test.xx();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	
	
	public void remoteDataSource(){
		
		
	}
	
	
	public void xx() {
		
		HikariDataSource remotedataSource = null;
		
try {
			
			
			String remotejdbc_driver = "com.mysql.jdbc.Driver";
			String remotejdbc_url = "jdbc:mysql://172.16.30.241:3306/sharingdata?useUnicode=true&characterEncoding=utf-8";
			String remotejdbc_username = "root";
			String remotejdbc_password = "root";
			
			
			remotedataSource = new HikariDataSource();
			
			remotedataSource.setDriverClassName(remotejdbc_driver);
			remotedataSource.setJdbcUrl(remotejdbc_url);
			remotedataSource.setUsername(remotejdbc_username);
			remotedataSource.setPassword(remotejdbc_password);
			//连接只读数据库时配置为true， 保证安全
			remotedataSource.setReadOnly(true);
			//等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒
			remotedataSource.setConnectionTimeout(30 * 1000);
			//一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
			remotedataSource.setIdleTimeout(1000 * 60 * 10);
			//一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒
			remotedataSource.setMaxLifetime(60 * 1000);
			//连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count) 
			remotedataSource.setMaximumPoolSize(50);
			
			remotedataSource.setMinimumIdle(10);
			
			remotedataSource.getConnection();
			
		} catch (Exception e) {
		}
		
		
		
Resource[] resources = null;
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			resources = resolver.getResources("classpath:/mybatis/UserMapper.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
		sqlSessionFactoryBean.setMapperLocations(resources);
		sqlSessionFactoryBean.setDataSource(remotedataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage("com.palmgo.com.cn.sharingdata.bean");
		
		
		try {
			SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
			SqlSession sqlsession = sqlSessionFactory.openSession();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("username", "admin");
			paramMap.put("tableName", null);
			paramMap.put("totalPerPage", 0);
			
			//创建sqlMapper
			SqlMapper sqlMapper = new SqlMapper(sqlsession);
			List<Map<String, Object>> lists = sqlMapper.selectList("<script>"
			+"SHOW TABLES  like '%${tableName}%'"
					+"</script>",paramMap);
			System.out.println("---:"+JsonUtils.getJsonStringFromObject(lists));
			//msUtils
			
			//Userdao userdao =  sqlsession.getMapper(Userdao.class);
			
			//List<UserInfo> xx =  userdao.GetUser(paramMap);
			//System.out.println(JsonUtils.getJsonStringFromObject(xx));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void cc() throws IOException {
		
		//字符串模板加载器
		StringTemplateResourceLoader resourceLoaderStr = new StringTemplateResourceLoader();
		Configuration cfgStr = Configuration.defaultConfiguration();
		GroupTemplate gtStr = new GroupTemplate(resourceLoaderStr, cfgStr);
		Map<String,Object> sharedStr = new HashMap<String,Object>();
		sharedStr.put("age", "beetl");
		sharedStr.put("sex", null);
		gtStr.setSharedVars(sharedStr);
		//获取模板
		Template tStr = gtStr.getTemplate("select * from user where 1=1  if(age!=null){ age=${age} }if(sex!=null) sex=${sex} }");
		//t.binding("name", "beetl");
		//渲染结果
		String str1 = tStr.render();
		System.out.println(str1);
		
	}
	
}
