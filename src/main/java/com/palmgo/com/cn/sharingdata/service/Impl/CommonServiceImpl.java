package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.caits.lbs.exception.LBSException;
import com.palmgo.com.cn.sharingdata.conf.Config;
import com.palmgo.com.cn.sharingdata.service.RedisCacheManager;
import com.palmgo.com.cn.sharingdata.util.TokenUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.bean.ApiUserInfoResponse;
import com.palmgo.com.cn.sharingdata.conf.EhcacheService;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;

/**
 * 公共服务实现，我们将ehcache修改为redis
 * 
 * @author Administrator
 *
 */
@Service
public class CommonServiceImpl implements CommonService {

	protected static Logger log = CommonLogFactory.getLog();
	
	//@Autowired
	//private EhcacheService cacheservice;

	@Autowired
	private RedisCacheManager cacheservice;

	@Autowired
	private Config config;

	@Override
	public void setCache(String key, Object obj) throws LBSException{
		log.info("key:"+key);
		//cacheservice.set(key,config.getCachetime(),obj);
	}

	@Override
	public Object getCache(String key) throws LBSException {
		log.info("key:"+key);
		//Object obj = cacheservice.getO(key);
		return null;
	}

	public boolean getsqlvalue(String sql) {
		boolean ok = true;
		if (sql.toUpperCase().indexOf("insert".toUpperCase()) != -1) {
			ok = false;
		} else if (sql.toUpperCase().indexOf("update".toUpperCase()) != -1) {
			ok = false;
		} else if (sql.toUpperCase().indexOf("drop".toUpperCase()) != -1) {
			ok = false;
		} else if (sql.toUpperCase().indexOf("delete".toUpperCase()) != -1) {
			ok = false;
		}
		return ok;
	}

}
