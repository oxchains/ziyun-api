package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * Created by root on 17-7-3.
 * 仓储单
 */
public class StorageBill extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("StorageTitle")
    private String StorageTitle;//贮存标题

    @JsonProperty("WarehouseName")
    private String WarehouseName;//仓库名字

    @JsonProperty("SalesId")
    private String SalesId;//生产销售id

    @JsonProperty("TransportId")
    private String TransportId;//运输id

    @JsonProperty("GiverName")
    private String GiverName;//转交人名字

    @JsonProperty("GiverPhone")
    private String GiverPhone;//转交人电话

    @JsonProperty("RecipientName")
    private String RecipientName;//接收人名字

    @JsonProperty("RecipientPhone")
    private String RecipientPhone;//接收人电话

    @JsonProperty("StartTime")
    private long StartTime;//存储开始时间 时间戳

    @JsonProperty("EndTime")
    private long EndTime;//存储结束时间 时间戳

    @JsonProperty("StorageAddress")
    private String StorageAddress;//存储地址

    @JsonProperty("HandoverInfo")
    private String HandoverInfo;//双方交接情况

    @JsonProperty("Type")
    private String Type;

    @JsonProperty("UniqueCodes")
    private List<String> UniqueCodes;

    @JsonProperty("CreateTime")
    private long CreateTime;

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//批发零售企业ID

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

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
