package com.oxchains.dao;

import org.springframework.data.repository.CrudRepository;

import com.oxchains.bean.model.ziyun.TabUser;

public interface TabUserDao extends CrudRepository<TabUser,Integer>{
	TabUser findByUsername(String username);
	
}
