package com.palmgo.com.cn.sharingdata.conf;


import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;


@Service
public class EhcacheService{

	protected static Logger log = CommonLogFactory.getLog();
	
	@Autowired
	private CacheManager manager;
	
	
	/**
	 * 读取ehcache缓存数据
	 * @param str
	 * @return
	 */
	public Object get(String str){
		Cache cache = manager.getCache("visulPublishCache");
		Element element = cache.get(str);
        return element == null ? null : element.getObjectValue();
	}
	
	/**
	 * 返回所有的缓存key
	 * @return
	 */
	public List<Cache> getkeys(){
		List<Cache> cache = manager.getCache("visulPublishCache").getKeys();
		return cache;
	}
	
	/**
	 *  写入ehcache缓存数据
	 * @param str
	 * @param o
	 */
	public void put(String str , Object o){
		Cache cache = manager.getCache("visulPublishCache");
		Element element = new Element(str, o);
	    cache.put(element);
		log.debug("写入本地缓存数据成功！ key="+str + "  value"+o.toString());
	}
	/**
	 * 删除ehcache缓存
	 * @param str
	 */
	public void del(String str){
		Cache cache = manager.getCache("visulPublishCache");
		cache.remove(str);
		log.debug("删除本地缓存数据成功！ key="+str);
	}
	/**
	 * 清空ehcache缓存
	 */
	public void clear(){
		Cache cache = manager.getCache("visulPublishCache");
		cache.removeAll();
		log.debug("清空本地缓存数据成功!");
	}
}