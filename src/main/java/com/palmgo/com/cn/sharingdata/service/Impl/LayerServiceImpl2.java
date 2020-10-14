/*package com.palmgo.com.cn.sharingdata.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.caits.lbs.framework.log.CommonLogFactory;
import com.caits.lbs.framework.utils.JsonUtils;
import com.caits.lbs.framework.utils.MD5;
import com.caits.lbs.framework.utils.StringUtils;
import com.palmgo.com.cn.sharingdata.bean.ApiConsumerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiUserInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.Consumeruser;
import com.palmgo.com.cn.sharingdata.bean.RecevierDataInfo;
import com.palmgo.com.cn.sharingdata.conf.Config;
import com.palmgo.com.cn.sharingdata.conf.MsgCode;
import com.palmgo.com.cn.sharingdata.dao.Layerdao;
import com.palmgo.com.cn.sharingdata.service.Interface.CommonService;
import com.palmgo.com.cn.sharingdata.service.Interface.ConsumeruserService;
import com.palmgo.com.cn.sharingdata.service.Interface.LayerService;
import com.palmgo.com.cn.sharingdata.service.Interface.ProduceruserService;
import com.palmgo.com.cn.sharingdata.util.DateUitls;

*//**
 * 用户相关操作
 * 
 * @author Administrator
 *
 *//*
@Service
public class LayerServiceImpl2 extends Config implements LayerService {

	public Logger log = CommonLogFactory.getLog();

	*//** 单条数据 **//*
	public BlockingQueue<RecevierDataInfo> Queue ;

	*//** 线程池 **//*
	protected ExecutorService executorService = Executors.newFixedThreadPool(100);

	@Autowired
	private ProduceruserService produceruserService;

	@Autowired
	private ConsumeruserService consumeruserService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private Layerdao layerdao;

	@PostConstruct
	public void init() {
		Queue = new LinkedBlockingQueue<RecevierDataInfo>(GlobalQueueSize);
		Hander hander = new Hander();
		executorService.submit(hander);
		log.info("初始化接收线程成功！");
	}

	@Override
	public ApiInfoResponse recevier(String token, String service, String username, String dataformat,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		ApiInfoResponse data = new ApiInfoResponse();
		ApiUserInfoResponse userdata = (ApiUserInfoResponse) commonService.getCache(token, request);
		if (userdata != null) {
			if (userdata.getData().get(0).getUsername().equals(username)) {
				data = dealwithrecevier(service, userdata.getData().get(0).getUserid(), dataformat, request);
				commonService.setCache(token, userdata, request);
			} else {
				data.setCode(MsgCode.code_209);
			}
		} else {
			data.setCode(MsgCode.code_209);
		}
		return data;
	}

	*//***
	 * 生产数据解析
	 * 
	 * @param service
	 * @param dataformat
	 * @return
	 *//*
	public ApiInfoResponse dealwithrecevier(String service, String userid, String dataformat,
			HttpServletRequest request) {
		ApiInfoResponse data = new ApiInfoResponse();
		// 将数据写入队列中
		// 业务代码需要去数据表中查找对应的数据表
		ApiProducerInfoResponse apiProducerInfoResponse;
		// 先检查自己是否有该业务
		String Md5Key = MD5.encode(userid + service).toUpperCase();
		apiProducerInfoResponse = (ApiProducerInfoResponse) commonService.getCache(Md5Key, request);
		if (apiProducerInfoResponse == null) {
			apiProducerInfoResponse = produceruserService.Getproduceruser(userid, service,null,null,null,null, 0, 0);
		}
		if (apiProducerInfoResponse.getCode() == MsgCode.code_200) {
			commonService.setCache(Md5Key, apiProducerInfoResponse, request);
			// 写入队列，通过线程来处理
			RecevierDataInfo dataInfo = new RecevierDataInfo();
			dataInfo.setData(dataformat);
			dataInfo.setTableName(apiProducerInfoResponse.getData().get(0).getTableName());
			dataInfo.setBusiness_code(service);
			dataInfo.setUserid(userid);
			add(dataInfo);
			data.setCode(MsgCode.code_200);
		} else {
			data.setCode(MsgCode.code_211);
			log.warn("找不到业务对应的数据表！");
		}
		return data;
	}

	*//**
	 * 数据存入队列
	 * 
	 * @param dataInfo
	 *//*
	public void add(RecevierDataInfo dataInfo) {
		try {
			Queue.add(dataInfo);
		} catch (Exception e) {
			log.info("数据接收队列已满，需要清除！");
			Queue.clear();
		}

	}

	*//**
	 * 消费数据解析
	 * 
	 * @param service
	 * @param dataformat
	 * @return
	 *//*
	public ApiInfoResponse dealwithsend(String service, String userid, String dataformat, HttpServletRequest request,
			int pageIndex, int totalPerPage) {
		if (pageIndex == 0) {
			// 无需分页处理
			totalPerPage = 0;
		} else {
			pageIndex = (pageIndex - 1) * totalPerPage;
		}
		ApiInfoResponse data = new ApiInfoResponse();
		String Md5Key = MD5.encode(userid + service).toUpperCase();
		// 查询自己是否有该业务
		ApiConsumerInfoResponse Consumer = (ApiConsumerInfoResponse) commonService.getCache(Md5Key, request);
		if (Consumer == null) {
			Consumer = consumeruserService.Getconsumeruser(userid, service,null,null,null, 0, 0);
			commonService.setCache(Md5Key, Consumer, request);
		}

		if (Consumer.getCode() == MsgCode.code_200) {
			String dateStr = Consumer.getData().get(0).getOverdueteime();
			long utc = DateUitls.getDateByPattern(dateStr, "yyyy-MM-dd HH:mm:ss").getTime();
			if(utc >= new Date().getTime()) {
				String sql = Consumer.getData().get(0).getSql();
				String dataformat_local =  Consumer.getData().get(0).getDataformat();
				ApiProducerInfoResponse apiProducerInfoResponse;
				Md5Key = MD5.encode(userid + service + "tableName").toUpperCase();
				apiProducerInfoResponse = (ApiProducerInfoResponse) commonService.getCache(Md5Key, request);
				if (apiProducerInfoResponse == null) {
					apiProducerInfoResponse = produceruserService.Getproduceruser(null, service,null, null,null,null,0, 0);
				}
				if (apiProducerInfoResponse.getCode() == MsgCode.code_200) {
					commonService.setCache(Md5Key, apiProducerInfoResponse, request);
					// 需要考虑分页
					try {
						// 用户动态参数
						Map<String, Object> content;
						if(StringUtils.notNullOrBlank(dataformat)) {
							content = JsonUtils.getMapFromJsonString(dataformat);
						}else {
							content = new HashMap<>();
						}
						log.info("用户参数" + content);
						String NewSQL = sql;
						if(content.size() > 0) {
							Map<String, Object> sqlfifter =  JsonUtils.getMapFromJsonString(dataformat_local);
							Iterator<Entry<String, Object>> it = content.entrySet().iterator();
							while(it.hasNext()) {
								Entry<String, Object> n = it.next();
								if(!sqlfifter.containsKey(n.getKey())) {
									it.remove();
								}
							}
							NewSQL = StringUtils.replaceVars(sql, content);
						}
						if (StringUtils.notNullOrBlank(NewSQL)) {
							Map<String, Object> paramMap = new HashMap<>();
							paramMap.put("fields", Consumer.getData().get(0).getFields());
							paramMap.put("sql", NewSQL);
							paramMap.put("tableName", apiProducerInfoResponse.getData().get(0).getTableName());
							paramMap.put("pageIndex", pageIndex);
							paramMap.put("totalPerPage", totalPerPage);
							paramMap.put("content", content);
							int total = layerdao.GetLayerCount(paramMap);
							List<Map<String, Object>> infos = layerdao.GetLayerInfo(paramMap);
							if (infos.size() > 0) {
								data.setTotal(total);
								data.setData(infos);
								data.setCode(MsgCode.code_200);
							} else {
								data.setCode(MsgCode.code_404);
							}
						} else {
							data.setCode(MsgCode.code_213);
						}
					} catch (Exception e) {
						log.error(e.getLocalizedMessage(), e);
						data.setMsg(e.getLocalizedMessage());
						data.setCode(MsgCode.code_500);
					}
				} else {
					data.setCode(MsgCode.code_211);
				}
			}else {
				data.setCode(MsgCode.code_215);
			}
		} else {
			data.setCode(MsgCode.code_212);
		}
		return data;
	}

	@Override
	public ApiInfoResponse send(String token, String service, String username, String dataformat,
			HttpServletRequest request, int pageIndex, int totalPerPage) {
		ApiInfoResponse data = new ApiInfoResponse();
		ApiUserInfoResponse userdata = (ApiUserInfoResponse) commonService.getCache(token, request);
		if (userdata != null) {
			if (userdata.getData().get(0).getUsername().equals(username)) {
				commonService.setCache(token, userdata, request);
				data = dealwithsend(service, userdata.getData().get(0).getUserid(), dataformat, request, pageIndex,
						pageIndex);
			} else {
				data.setCode(MsgCode.code_209);
			}
		} else {
			data.setCode(MsgCode.code_209);
		}
		return data;
	}

	class Hander extends Thread {

		@Override
		public void run() {
			while (true) {
				String line = null;
				String tableName = null;
				String business_code = null;
				String userid = null;
				try {
					RecevierDataInfo data = Queue.take();
					line = data.getData();
					tableName = data.getTableName();
					business_code = data.getBusiness_code();
					userid = data.getUserid();
					Map<String, Object>[] content = JsonUtils.getObjectFromJsonString(line, Map[].class);
					Map<String, Object> paramMap = new HashMap<>();
					paramMap.put("business_code", business_code);
					for (Map<String, Object> map : content) {
						paramMap.put("tableName", tableName);
						paramMap.put("content", map);
						Object ok = layerdao.SaveLayerInfo(paramMap);
						log.info("影响的行数：" + ok);
						// 需要将该业务的数据分发给对应的消费者，只要支持推送的必须发送
						ApiConsumerInfoResponse Consumer = consumeruserService.Getconsumeruser(null, business_code,null,null,null, 0,
								0);
						if (Consumer.getCode() == MsgCode.code_200) {
							List<Consumeruser> infos = Consumer.getData();
							for (Consumeruser consumeruser : infos) {
								if (consumeruser.getPush().equals("1")) {
									// 可以推送，检查用户是否到期
									String dateStr = consumeruser.getOverdueteime();
									long utc = DateUitls.getDateByPattern(dateStr, "yyyy-MM-dd HH:mm:ss").getTime();
									if (utc >= new Date().getTime()) {
										paramMap.remove("tableName");
										//需要过滤字段
										List<String> Fields = Arrays.asList(Consumer.getData().get(0).getFields().split(","));
										Map<String, Object> newMap = new HashMap<>();
										for (String key : map.keySet()) {
											if(Fields.contains(key)) {
												newMap.put(key, map.get(key));
											}
										}
										paramMap.put("content", newMap);
										sendconsumer(paramMap, consumeruser.getPushadress(),
												consumeruser.getPushtopic(), consumeruser.getPushtag());
									} else {
										log.warn("用户已到期，不再推送数据!");
									}
								}
							}
						}
					}
					log.info(data.getTableName());
					log.info(data.getData());
				} catch (Exception e) {
					log.info("接收数据处理异常，请及时处理！");
					log.info("接入数据：" + line);
					log.info("接入业务：" + business_code);
					log.info("接入用户id：" + userid);
					log.error(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	*//**
	 * 发送个消费者
	 * 
	 * @param paramMap
	 *//*
	public void sendconsumer(Map<String, Object> paramMap, String Pushadress, String Pushtopic, String Pushtag) {
		String json = JsonUtils.getJsonStringFromMap(paramMap);
		DefaultMQProducer producer = getconnect(Pushadress);
		try {
			if (producer != null) {
				String tag = "*";
				if (Pushtag != null) {
					tag = Pushtag;
				}
				Message message = new Message(Pushtopic, tag, (json).getBytes(RemotingHelper.DEFAULT_CHARSET));
				SendResult sendResult = producer.send(message);
				log.info("发送的消息ID:" + sendResult.getMsgId() + "--- 发送消息的状态：" + sendResult.getSendStatus());
			}
		} catch (Exception e) {
			shutdown(producer);
			log.error("发送数据异常！");
			log.error(e.getLocalizedMessage(), e);
		}

	}

	*//**
	 * 创建
	 * 
	 * @param address
	 * @return
	 *//*
	public DefaultMQProducer getconnect(String address) {
		DefaultMQProducer producer = null;
		try {
			producer = (DefaultMQProducer) commonService.getCache(address, null);
			if (producer == null) {
				// 需要一个producer group名字作为构造方法的参数，这里为producer1
				producer = new DefaultMQProducer(MD5.encode(address).toUpperCase());
				// 设置NameServer地址,此处应改为实际NameServer地址，多个地址之间用；分隔
				// NameServer的地址必须有，但是也可以通过环境变量的方式设置，不一定非得写死在代码里
				producer.setNamesrvAddr(address);
				producer.setVipChannelEnabled(false);
				// 为避免程序启动的时候报错，添加此代码，可以让rocketMq自动创建topickey
				producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
				producer.start();
				commonService.setCache(address, producer, null);
			}
		} catch (Exception e) {
			log.info("创建rocketmq连接失败！");
			log.error(e.getLocalizedMessage(), e);
		}
		return producer;
	}

	*//**
	 * 关闭
	 * 
	 * @param producer
	 *//*
	public void shutdown(DefaultMQProducer producer) {
		producer.shutdown();
	}

}
*/