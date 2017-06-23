package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import lombok.Data;

@Data
public class User extends BaseEntity{

	 @JsonProperty("id")
	 private String id; //用户唯一ID
	 
	 @JsonProperty("username")
	 private String username;//用户名
	 
	 @JsonProperty("password")
	 private String password;//密码
	 
	 @JsonProperty("phone")
	 private String phone;//电话号
	 
	 @JsonProperty("realname")
	 private String realname;//真实姓名
	 
	 @JsonProperty("status")
	 private String status;//用户状态
	 
	 
}
