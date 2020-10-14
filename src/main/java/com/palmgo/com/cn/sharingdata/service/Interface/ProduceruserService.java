package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.Produceruser;

/**
 * 	生产者相关操作
 * @author Administrator
 *
 */
public interface ProduceruserService {

	
	/**
	 *  生产者信息
	 * @param userid
	 * @param pageIndex
	 * @param totalPerPage
	 * @return
	 */
	public ApiProducerInfoResponse Getproduceruser(String userid,String business_code,String tableName,String loginName,
			 int pageIndex, int totalPerPage)throws Exception;

	/**
	 * 保存生产者
	 * @param userid
	 * @param produceruser
	 * @return
	 */
	public ApiProducerInfoResponse SaveproduceruserInfo(String userid,Produceruser produceruser,String loginName)throws Exception;

	/**
	 * 删除生产者
	 * @param produceruser
	 * @return
	 */
	public ApiProducerInfoResponse DeleteproduceruserInfo(Produceruser produceruser,String loginName)throws Exception;
}
