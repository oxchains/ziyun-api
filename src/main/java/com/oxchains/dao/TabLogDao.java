package com.oxchains.dao;

import org.springframework.data.repository.CrudRepository;

import com.oxchains.bean.model.ziyun.TabLog;

public interface TabLogDao extends CrudRepository<TabLog,Long>{
	TabLog findByToken(String token);
}
