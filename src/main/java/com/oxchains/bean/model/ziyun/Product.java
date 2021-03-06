package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * Created by root on 17-7-3.
 * 产品
 */
public class Product extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("ProductName")
    private String ProductName;//产品名称

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//企业ID

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

    @JsonProperty("ProductOriginalUrl")
    private List<String> ProductOriginalUrl;//产品图片

    @JsonProperty("ProductBuyUrl")
    private String ProductBuyUrl;//产品购买url

    @JsonProperty("ProductAddress")
    private String ProductAddress;//生产地址

    @JsonProperty("ProductTime")
    private long ProductTime;//生产时间 (时间戳)

    @JsonProperty("ProductType")
    private String ProductType;//产品类型

    @JsonProperty("ProductDeadline")
    private int ProductDeadline;//保质期(月)

    @JsonProperty("ProductTags")
    private String ProductTags;//产品标签

    @JsonProperty("ProductWeight")
    private String ProductWeight;//产品重量

    @JsonProperty("ProductVolume")
    private String ProductVolume;//产品体积

    @JsonProperty("ProductCode")
    private String ProductCode;//产品编码

    @JsonProperty("Remarks")
    private String Remarks;//备注

    @JsonProperty("Size")
    private String Size;//规格

    @JsonProperty("Pack")
    private String Pack;//包装

    @JsonProperty("ApprovalNumber")
    private String ApprovalNumber;//批准文号

    @JsonProperty("Storage")
    private String Storage;//贮藏

    @JsonProperty("Describe")
    private String Describe;//描述

    @JsonProperty("Token")
    private String Token;//token

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    private String TxId;

    public void setTxId(String txId) {
        TxId = txId;
    }

    public String getTxId() {
        return TxId;
    }
}
