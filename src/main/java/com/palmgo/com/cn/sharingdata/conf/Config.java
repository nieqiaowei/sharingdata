package com.palmgo.com.cn.sharingdata.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@EnableConfigurationProperties
@Configuration
@ConfigurationProperties(prefix = "common")
//@EnableApolloConfig
public class Config {

	public int globalQueueSize;

	public List<String> cacheServer;

	public int cachetime;
	

}
