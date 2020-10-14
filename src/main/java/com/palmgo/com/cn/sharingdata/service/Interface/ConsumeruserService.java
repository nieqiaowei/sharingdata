package com.palmgo.com.cn.sharingdata.service.Interface;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiConsumerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.ApiProducerInfoResponse;
import com.palmgo.com.cn.sharingdata.bean.Consumeruser;
import com.palmgo.com.cn.sharingdata.bean.Produceruser;
import com.palmgo.com.cn.sharingdata.bean.Tableoffield;

/**
 * 	消费者相关操作
 * @author Administrator
 *
 */
public interface ConsumeruserService {

	/**
	 * 消费者列表
	 * @param userid
	 * @param business_code
	 * @param loginName
	 * @param pageIndex
	 * @param totalPerPage
	 * @return
	 */
	public ApiConsumerInfoResponse Getconsumeruser(String userid,String business_code, String loginName,
			int pageIndex, int totalPerPage)throws Exception;


	/**
	 * 保存消费者
	 * @param userid
	 * @param consumerusersdel
	 * @param consumerusers
	 * @return
	 */
	public ApiConsumerInfoResponse SaveconsumeruserInfo(String userid,Consumeruser[] consumerusersdel,Consumeruser[] consumerusers,String loginName)throws Exception;


	/**
	 * 删除消费
	 * @param consumerusers
	 * @return
	 */
	public ApiConsumerInfoResponse DeleteconsumeruserInfo(List<Consumeruser> consumerusers,String loginName)throws Exception;

}
