package com.palmgo.com.cn.sharingdata.service.Interface;

import javax.servlet.http.HttpServletRequest;

import com.palmgo.com.cn.sharingdata.bean.ApiUserInfoResponse;

public interface UserService {

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param pageIndex
	 * @param totalPerPage
	 * @return
	 */
	public ApiUserInfoResponse login(String username, String password, int pageIndex, int totalPerPage) throws Exception;


	/**
	 * 获取用户公钥
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public ApiUserInfoResponse getpublickey(String loginName) throws Exception;


	/**
	 * 加密密码rsa
	 * @param publicKey
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public ApiUserInfoResponse getpwdrsa(String publicKey,String password) throws Exception;

	/**
	 * 登出
	 * @param username
	 * @return
	 */
	public ApiUserInfoResponse logout(String username) throws Exception;

	/**
	 * 通过用户名查找用户是否存在
	 * @param userid
	 * @param username
	 * @param roleid
	 * @param loginName
	 * @param pageIndex
	 * @param totalPerPage
	 * @return
	 */
	public ApiUserInfoResponse UserAll(String userid, String username, String roleid, String loginName,String name, int pageIndex, int totalPerPage) throws Exception;

	/**
	 * 保存用户信息
	 * @param userid
	 * @param username
	 * @param name
	 * @param password
	 * @param loginName
	 * @param roleid
	 * @return
	 */
	public ApiUserInfoResponse SaveUserInfo(String userid, String username, String name, String password,
			String loginName, String roleid) throws Exception;

	/**
	 * 删除用户信息，根据用户id进行删除
	 * @param userid
	 * @param loginName
	 * @return
	 */
	public ApiUserInfoResponse DeleteUserInfo(String userid, String loginName) throws Exception;
}
