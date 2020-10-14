package com.palmgo.com.cn.sharingdata.bean;


import lombok.Data;

@Data
public class DataSourceMapper {

	private DataSessionAndMapper dataSessionAndMapper;
	
	private SourceType sourceType;
	
	private DataSource dataSource;
}
