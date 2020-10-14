package com.palmgo.com.cn.sharingdata.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.caits.lbs.framework.log.CommonLogFactory;

public class RegularUtils {

	public static Logger log = CommonLogFactory.getLog();
	
	//static String key1 = "\\$\\{(.)+\\}";
	static String key1 = "\\$\\{[A-Za-z0-9]+\\}";
	
	//static String key2 = "\\#\\{(.)+\\}";
	static String key2 = "\\#\\{[A-Za-z0-9]+\\}";
	
	/**
	 * 内容提取
	 * @return
	 */
	public static synchronized String getRegularContent(String content) {
		String ParameterValue = null;
		Map<String, String> valueMap = new HashMap<>();
		if(StringUtils.notNullOrBlank(content)) {
			try {
				Pattern regex = Pattern.compile(key1, Pattern.CASE_INSENSITIVE);
				Matcher matcher = regex.matcher(content);
				StringBuffer sb = new StringBuffer();
				// 使用find()方法查找第一个匹配的对象
				// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
				while (matcher.find()) {
					String temp = matcher.group();
					int count = matcher.groupCount();
					String key = temp.substring(2, temp.length() - 1);
					valueMap.put(key, "");
				}
				
				regex = Pattern.compile(key2, Pattern.CASE_INSENSITIVE);
				matcher = regex.matcher(content);
				sb = new StringBuffer();
				// 使用find()方法查找第一个匹配的对象
				// 使用循环找出模式匹配的内容替换之,再将内容加到sb里
				while (matcher.find()) {
					String temp = matcher.group();
					int count = matcher.groupCount();
					String key = temp.substring(2, temp.length() - 1);
					valueMap.put(key, "");
				}
				
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(),e);
			}
		}
		if(valueMap.size() > 0 ) {
			ParameterValue = JsonUtils.getJsonStringFromMap(valueMap);
		}
		return ParameterValue;
	}
	
	public static void main(String[] args) {
		System.out.println(getRegularContent("${xxx_yy}"));
	}
}
