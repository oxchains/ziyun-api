package com.oxchains.dao;

import org.springframework.data.repository.CrudRepository;

import com.oxchains.bean.model.ziyun.TabToken;

public interface TabTokenDao extends CrudRepository<TabToken,Integer>{
	TabToken findByUsername(String username);
}
