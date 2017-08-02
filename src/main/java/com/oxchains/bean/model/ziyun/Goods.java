package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * Created by root on 17-7-3.
 * 货物
 */
public class Goods extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("Type")
    private String Type;//food, medicine

    @JsonProperty("GoodsType")
    private String GoodsType;//货物类型

    @JsonProperty("GoodsName")
    private String GoodsName;//货物名字

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//企业Id

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

    @JsonProperty("GoodsSize")
    private String GoodsSize;//货物规格

    @JsonProperty("ApprovalNumber")
    private String ApprovalNumber;//批准文号

    @JsonProperty("ProductAddress")
    private String ProductAddress;//生产地址

    @JsonProperty("ProductDeadline")
    private long ProductDeadline;//有效期

    @JsonProperty("ProductTags")
    private String ProductTags;//标签

    @JsonProperty("Pack")
    private String Pack;//包装

    @JsonProperty("Storage")
    private String Storage;//贮存

    @JsonProperty("Describe")
    private String Describe;//描述

    @JsonProperty("ProductionTime")
    private long ProductionTime;//生产时间 时间戳

    @JsonProperty("ParentCode")
    private String ParentCode;//父编码

    @JsonProperty("ProduceInfoId")
    private String ProduceInfoId;//生产过程ID

    @JsonProperty("ProductId")
    private String ProductId;//产品ID

    @JsonProperty("ProductCode")
    private String ProductCode;//产品编码

    @JsonProperty("UniqueCode")
    private String UniqueCode;//唯一编码

    @JsonProperty("CommodityCode")
    private String CommodityCode;//商品码

    @JsonProperty("ProductionBatch")
    private String ProductionBatch;//生产批次

    @JsonProperty("DrugInformation")
    private List<DrugInformation> DrugInformation;//药品信息

    @JsonProperty("FoodInformation")
    private List<FoodInformation> FoodInformation;//食品信息

    @JsonProperty("ProduceInformation")
    private List<ProduceInformation> ProduceInformation;//生产信息

    @JsonProperty("SalesId")
    private String SalesId;//生产销售ID

    @JsonProperty("Token")
    private String Token;//token

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    @JsonIgnore
    public String getParentCode() {
        return ParentCode;
    }
}
