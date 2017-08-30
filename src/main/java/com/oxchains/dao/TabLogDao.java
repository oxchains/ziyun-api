package com.oxchains.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.oxchains.bean.model.ziyun.TabLog;

public interface TabLogDao extends CrudRepository<TabLog,Long>{
	@Modifying
	@Query("update TabLog set logouttime = NOW() where token = ?1")
	int updateLogouttime(String token);
}
