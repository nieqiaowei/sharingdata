/**
 * <p>文件名:		Servlets.java</p>
 * <p>版权:		CopyrightTag</p>
 * <p>公司:		CompanyTag</p>
 * @author		周华彬(zhouhuabin@chinatransinfo.com, zhou_hua_bin@163.com)
 */

package com.palmgo.com.cn.sharingdata.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;

import com.caits.lbs.framework.utils.Base64Codec;

/**
 * <p>
 * Servlets
 * </p>
 * <p>
 * servlet工具类
 * </p>
 * @author 周华彬(zhouhuabin@chinatransinfo.com, zhou_hua_bin@163.com)
 * @version 0.0.0
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
 *          <td>zhouhuabin</td>
 *          <td>2015年9月15日 上午10:00:11</td>
 *          </tr>
 *          <tr>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          <td>XXX</td>
 *          </tr>
 *          </table>
 */
public class ServletUtils {

	public static final long ONE_YEAR_SECONDS = 31536000L;

	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000L);

		response.setHeader("Cache-Control", new StringBuilder().append("private, max-age=").append(expiresSeconds).toString());
	}

	public static void setNoCacheHeader(HttpServletResponse response) {
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");

		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}

	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}

	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}

	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1L) && (lastModified < ifModifiedSince + 1000L)) {
			response.setStatus(304);
			return false;
		}
		return true;
	}

	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while ((!conditionSatisfied) && (commaTokenizer.hasMoreTokens())) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag))
						conditionSatisfied = true;
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(304);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}

	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", new StringBuilder().append("attachment; filename=\"").append(encodedfileName).append("\"").toString());
		} catch (UnsupportedEncodingException e) {
		}
	}

	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null", new Object[0]);
		Enumeration paramNames = request.getParameterNames();
		Map params = new TreeMap();
		if (prefix == null) {
			prefix = "";
		}
		while ((paramNames != null) && (paramNames.hasMoreElements())) {
			String paramName = (String) paramNames.nextElement();
			if (("".equals(prefix)) || (paramName.startsWith(prefix))) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if ((values != null) && (values.length != 0)) {
					if (values.length > 1)
						params.put(unprefixed, values);
					else
						params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		if ((params == null) || (params.size() == 0)) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}
		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append((String) entry.getKey()).append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}

	public static String encodeHttpBasic(String userName, String password) {
		String encode = new StringBuilder().append(userName).append(':').append(password).toString();
		return new StringBuilder().append("Basic ").append(Base64Codec.encode(encode.getBytes())).toString();
	}

	/**
	 * 获取请求参数串，拼成一行
	 * @param request
	 * @return String k=v&...
	 */
	public static String getParameterValues(HttpServletRequest request) {
		if (request.getMethod().equals("GET")) {
			return request.getQueryString();
		} else {
			@SuppressWarnings("unchecked")
			Enumeration<String> enu = request.getParameterNames();
			StringBuilder sb = new StringBuilder();
			while (enu.hasMoreElements()) {
				String paraName = enu.nextElement();
				String paraValue = request.getParameter(paraName);
				sb.append(paraName).append("=").append(paraValue).append("&");
			}
			return sb.length() == 0 ? sb.toString() : sb.toString().substring(0, sb.length() - 1);
		}
	}

	/**
	 * 获取request的服务路径，不包括参数 
	 * @param request
	 * @return String
	 */
	public static String getUri(HttpServletRequest request) {
		String contextPath = new StringBuilder().append("").append(request.getContextPath()).toString();
		String str1 = request.getRequestURI();
		if (!contextPath.equals("/"))
			str1 = str1.replaceFirst(contextPath, "");
		str1 = str1.replaceAll("/{2,}", "/");
		return str1;
	}
}
