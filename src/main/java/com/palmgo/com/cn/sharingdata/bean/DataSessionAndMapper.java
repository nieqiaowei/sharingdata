package com.palmgo.com.cn.sharingdata.bean;

import org.apache.ibatis.session.SqlSession;

import com.palmgo.com.cn.sharingdata.service.Impl.SqlMapper;

import lombok.Data;

@Data
public class DataSessionAndMapper {

	private SqlSession sqlsession;

	private SqlMapper SqlMapper;
}
