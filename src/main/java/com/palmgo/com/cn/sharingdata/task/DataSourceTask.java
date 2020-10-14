package com.palmgo.com.cn.sharingdata.task;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.caits.lbs.framework.log.CommonLogFactory;
import com.palmgo.com.cn.sharingdata.service.Interface.DataSourceService;

@Service
public class DataSourceTask {

	protected static Logger log = CommonLogFactory.getLog();
	
	@Autowired
	private DataSourceService dataSourceService;

	@Scheduled(cron = "${job.datasourcetaskinit}")
	@Async
	public void init() throws ParseException {
		log.info("开始检测数据库连接---");
		//dataSourceService.init(null);
		log.info("结束检测数据库连接---");
	}
}
