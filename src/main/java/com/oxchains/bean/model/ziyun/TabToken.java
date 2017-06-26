package com.oxchains.bean.model.ziyun;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import lombok.Data;

@Entity
@Table(name = "tab_token")
@Data
public class TabToken extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty("id")
	private int id;
	
	@JsonProperty("username")
	private String username;

	@JsonProperty("token")
	private String token;
}
