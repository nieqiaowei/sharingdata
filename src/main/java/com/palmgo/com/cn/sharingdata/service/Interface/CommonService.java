package com.palmgo.com.cn.sharingdata.service.Interface;

import com.caits.lbs.exception.LBSException;

import javax.servlet.http.HttpServletRequest;

public interface CommonService {

	/**
	 * 设置缓存
	 * @param key
	 * @param obj
	 */
	public void setCache(String key , Object obj) throws LBSException;

	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public Object getCache(String key) throws LBSException;
}
