package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Token {
	 @JsonProperty("token")
	 private String token;
	 
	 @JsonProperty("expiresIn")
	 private int expiresIn;
}
