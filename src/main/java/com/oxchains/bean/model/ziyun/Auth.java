package com.oxchains.bean.model.ziyun;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import lombok.Data;

@Data
public class Auth extends BaseEntity {
	@JsonProperty("AuthList")
	private ArrayList<String> AuthList; // 被撤销/add权限用户id
	
	@JsonProperty("Id")
	private String Id; // 操作用户id
}
