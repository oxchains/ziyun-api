package com.oxchains.bean.model.ziyun;

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
@Table(name = "tab_user")
public class TabUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("id")
	private int id; // 用户唯一ID

	@JsonProperty("username")
	private String username;// 用户名

	@JsonProperty("password")
	private String password;// 密码

	@JsonProperty("phone")
	private String phone;// 电话号

	@JsonProperty("realname")
	private String realname;// 真实姓名

	@JsonProperty("status")
	private String status;// 用户状态

}
