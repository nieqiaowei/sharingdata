package com.palmgo.com.cn.sharingdata.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.caits.lbs.bean.dbmodel.ETBase;
import com.caits.lbs.framework.log.CommonLogFactory;

/**
 * <p>
 * JsonUtil
 * </p>
 * <p>
 * 用途：json工具类
 * </p>
 * 
 * @author 周华彬(zhouhuabin@ctfo.com, zhou_hua_bin@163.com)
 * @version 0.0.1 2011-11-11
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th>
 *          <th width="100px">动作</th>
 *          <th width="100px">修改人</th>
 *          <th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>zhb</td>
 *          <td>2011-11-11 上午11:41:10</td>
 *          </tr>
 *          <tr>
 *          <td>0.0.1</td>
 *          <td>创建类</td>
 *          <td>zhb</td>
 *          <td>2011-11-11 上午11:41:10</td>
 *          <td>0.0.2</td>
 *          <td>修改类</td>
 *          <td>xxx</td>
 *          <td>x年x月x日</td>
 *          </tr>
 *          </table>
 */
public class JsonUtils {

	/** 日志生成器 */
	protected static Logger log = CommonLogFactory.getLog();
	/** jackson的解析实例 */
	static ObjectMapper om = new ObjectMapper();
	static{
		//忽略掉不认识的属性
		om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	// static XmlMapper xml = new XmlMapper();
	/**
	 * 从一个JSON 对象字符格式中得到一个java对象 说明：Bean的无参构造函数一定要写, 否则会报:
	 * net.sf.json.JSONException: java.lang.NoSuchMethodException
	 * @param <T>
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	public static <T> T getObjectFromJsonString(String jsonString,
			Class<T> pojoCalss) {
		T pojo = null;
		// 传统json的实现,嵌套解析会有问题
		// JSONObject jsonObject = JSONObject.fromObject(jsonString);
		// pojo = JSONObject.toBean(jsonObject, pojoCalss);
		try {
			pojo = om.readValue(jsonString, pojoCalss);
		} catch (JsonParseException e) {
			log.error("json解析异常,jsonString="+jsonString, e);
		} catch (JsonMappingException e) {
			log.error("jsonMapping解析异常,msg="+e.getLocalizedMessage(), e);
		} catch (IOException e) {
			log.error("json输出IO异常,msg="+e.getLocalizedMessage(), e);
		}
		return pojo;
	}
	
	public static <T> T getObjectArrayFromJsonString(String jsonString, Class<T> pojoCalss) {
		return getObjectFromJsonString(jsonString, pojoCalss);
	}

	/**
	 * 从json HASH表达式中获取一个map
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapFromJsonString(String jsonString) {
		return getObjectFromJsonString(jsonString, Map.class);
	}


	/**
	 * 从Map对象得到Json字串
	 * 
	 * @param map
	 * @return
	 */
	public static String getJsonStringFromMap(Map<?,?> map) {
		return getJsonStringFromObject(map);
	}

	/**
	 * 用JSONStringer构造一个JsonString
	 * 
	 * @param m
	 * @return
	 */
	public static String getJsonStringFromObject(Object obj) {
		StringWriter sw = new StringWriter();
		try {
			om.writeValue(sw, obj);
		} catch (JsonGenerationException e) {
			log.error("json解析异常,msg="+e.getLocalizedMessage(), e);
		} catch (JsonMappingException e) {
			log.error("jsonMapping解析异常,msg="+e.getLocalizedMessage(), e);
		} catch (IOException e) {
			log.error("json输出IO异常,msg="+e.getLocalizedMessage(), e);
		}
		return sw.toString();
	}


	/**
	 * FIXME 
	 * @param args
	 */
	public static void main(String[] args) {
		String line_pro="{\"id\":123}";
		ETBase obj = getObjectFromJsonString(line_pro, ETBase.class);
		log.debug("obj="+obj);
		String line_arr= "[{\"id\":123},{\"id\":456}]";
		ETBase[] arr = (ETBase[]) getObjectFromJsonString(line_arr,ETBase[].class);
		log.debug("arr="+arr.length);
		Map<String,Object> map=getMapFromJsonString(line_pro);
		log.debug("map="+map);
		String line_json = getJsonStringFromMap(map);
		log.debug("map.line_json="+line_json);
		line_json = getJsonStringFromObject(obj);
		log.debug("obj.line_json="+line_json);
		List<ETBase> list = new ArrayList<ETBase>();
		list.add(obj);
		Object[] arr_obj=list.toArray();
		log.debug("list.obj="+arr_obj.length);
		
	}

}
