package com.palmgo.com.cn.sharingdata.fifter;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.util.JsonUtils;
import com.palmgo.com.cn.sharingdata.util.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.util.CommonUtil;
import com.palmgo.com.cn.sharingdata.util.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 过滤器 公共的处理在这里解决
 * @author Administrator
 *
 */
public class CommonFifter implements HandlerInterceptor {

	protected static Logger log = CommonLogFactory.getLog();
	
	/**
	 * 拦截器处理之前执行的方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		String uri = ServletUtils.getUri(request);
		ServletUtils.getParameterValues(request);
		String url = "收到请求 url:"+ uri + "?" + ServletUtils.getParameterValues(request) + ",client=" + CommonUtil.getClientIpAddr(request);
		log.info(url);

		if(uri.indexOf("sharingdata/login")!=-1 ||
				uri.indexOf("sharingdata/getpublickey")!=-1 ||
				uri.indexOf("sharingdata/pwdrsa")!=-1 ||
				uri.equals("/sharingdata") ||
				uri.equals("/error") ||
				uri.equals("/") ||
				uri.equals("/index.html") ||
				uri.indexOf(".js")!=-1 ||
				uri.indexOf(".css")!=-1 ||
				uri.indexOf(".jpg")!=-1 ||
				uri.indexOf(".png")!=-1 ||
				uri.indexOf(".woff")!=-1 ||
				uri.indexOf(".ttf")!=-1 ||
				uri.equals("/sharingdata/swagger-resources/configuration/ui") ||
				uri.equals("/sharingdata/swagger-resources/configuration/security") ||
				uri.equals("/sharingdata/swagger-resources") ||
				uri.equals("/sharingdata/v2/api-docs") ||
				uri.equals("/sharingdata/")){
			return HandlerInterceptor.super.preHandle(request, response, handler);
		}else{
			String token = request.getHeader("Authorization");
			String loginName = request.getParameter("loginName");
			if (!StringUtils.isEmpty(token)) {
				DecodedJWT verify = TokenUtils.verify(token);
				if(verify!=null){
					String username = verify.getClaim("username").asString();
					if(username.equals(loginName)){
						log.info(username+"认证成功！");
						return HandlerInterceptor.super.preHandle(request, response, handler);
					}else{
						Map<String,Object> value = new HashMap<>();
						value.put("code", MsgCode.code_209);
						value.put("msg", "用户名与token中不一致，"+MsgCode.msg_209);
						String json = JsonUtils.getJsonStringFromMap(value);
						returnJson(response,json);
						return false;
					}
				}else{
					Map<String,Object> value = new HashMap<>();
					value.put("code", MsgCode.code_209);
					value.put("msg", "token超时，"+MsgCode.msg_209);
					String json = JsonUtils.getJsonStringFromMap(value);
					returnJson(response,json);
					return false;
				}
			}else{
				Map<String,Object> value = new HashMap<>();
				value.put("code", MsgCode.code_209);
				value.put("msg", "token未能成功获取到，"+MsgCode.msg_209);
				String json = JsonUtils.getJsonStringFromMap(value);
				returnJson(response,json);
				return false;
			}
		}
	}
	
	/**
	 * 拦截器处理后方法
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	/**
	 * 拦截器处理完后方法
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}


	public void returnJson(HttpServletResponse response, String json){
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try {
			writer = response.getWriter();
			writer.print(json);
		} catch (IOException e) {
			log.error("response error",e);
		} finally {
			if (writer != null)
				writer.flush();
				writer.close();
		}
	}
	
	
}
