package com.oxchains.bean.model.ziyun;

import java.util.Date;

import com.oxchains.common.BaseEntity;

import lombok.Data;

@Data
public class JwtToken extends BaseEntity{
	
	private String id;
	
	private String subject;
	
	private String issuer;
	
	private Date issueAt;
	
	private Date expiratioin;
}
