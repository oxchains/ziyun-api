package com.oxchains.bean.model.ziyun;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "tab_log")
public class TabLog extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("id")
	private long id; // 用户唯一ID

	@JsonProperty("username")
	private String username;// 用户名

	@JsonProperty("loginip")
	private String loginip;// loginip

	@JsonProperty("logintime")
	private Date logintime;// logintime

	@JsonProperty("logouttime")
	private Date logouttime;// logouttime
	
	@JsonProperty("token")
	private String token;// token
}
