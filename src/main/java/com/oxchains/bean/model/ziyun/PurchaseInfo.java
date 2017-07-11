package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-7-3.
 * 采购信息
 */
public class PurchaseInfo extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("PurchaseTitle")
    private String PurchaseTitle;//采购标题

    @JsonProperty("Count")
    private int Count;//采购数量

    @JsonProperty("TransportId")
    private String TransportId;//运输id

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//所属企业id

    @JsonProperty("ProductionAddress")
    private String ProductionAddress;//产地

    @JsonProperty("GoodsId")
    private String GoodsId;//货物id

    @JsonProperty("ProductionSpecification")
    private String ProductionSpecification;//规格

    @JsonProperty("ProductionTime")
    private long ProductionTime;//生产时间 时间戳

    @JsonProperty("ProductionBatch")
    private String ProductionBatch;//生产批次

    @JsonProperty("ExpirationDate")
    private int ExpirationDate;//保质期

    @JsonProperty("StockDate")
    private long StockDate;//进货日期 时间戳

    @JsonProperty("SupperName")
    private String SupperName;//供货公司名

    @JsonProperty("SupperAddress")
    private String SupperAddress;//供货地址

    @JsonProperty("SupplyName")
    private String SupplyName;//负责人姓名

    @JsonProperty("SupplyPhone")
    private String SupplyPhone;//负责人联系方式

    @JsonProperty("Token")
    private String Token;//token

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

}
