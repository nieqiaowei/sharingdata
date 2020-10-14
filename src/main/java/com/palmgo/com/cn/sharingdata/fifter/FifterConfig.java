package com.palmgo.com.cn.sharingdata.fifter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FifterConfig  implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		InterceptorRegistration registration = registry.addInterceptor(new CommonFifter());
		//限制拦截器的请求
		//registration.addPathPatterns("/test");
	}
}
