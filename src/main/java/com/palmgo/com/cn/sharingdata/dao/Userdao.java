package com.palmgo.com.cn.sharingdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.palmgo.com.cn.sharingdata.bean.UserInfo;

@Mapper
@Repository
public interface Userdao {

	/** 总条数 **/
	public int GetUserCount(Map<String, Object> paramMap);
	
	/** 列表数据**/
	public List<UserInfo> GetUser(Map<String, Object> paramMap);
	
	/** 保存数据**/
	public int SaveUserInfo(UserInfo userInfo);
	
	/** 删除数据**/
	public int DeleteUserInfo(Map<String, Object> paramMap);
}
