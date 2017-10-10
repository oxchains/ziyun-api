package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * Created by root on 17-10-10.
 */
public class SyzlExchangeRecord extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("No")
    private String No;//编号

    @JsonProperty("EnterpriseFirstInformation")
    private SyzlEnterpriseGmp EnterpriseFirstInformation;//企业首营资料信息

    @JsonProperty("ProductFirstInformationList")
    private List<SyzlProductGmp> ProductFirstInformationList;//产品首营资料

    @JsonProperty("SendEnterpriseId")
    private String SendEnterpriseId;//发送企业ID

    @JsonProperty("SendEnterpriseName")
    private String SendEnterpriseName;//发送企业名称

    @JsonProperty("SendEnterpriseType")
    private String SendEnterpriseType;//发送企业类型

    @JsonProperty("ReceiveEnterpriseId")
    private String ReceiveEnterpriseId;//接收企业ID

    @JsonProperty("ReceiveEnterpriseName")
    private String ReceiveEnterpriseName;//接收企业名称

    @JsonProperty("ReceiveEnterpriseType")
    private String ReceiveEnterpriseType;//接收企业类型

    @JsonProperty("EntrustBookUrl")
    private List<String> EntrustBookUrl;//委托书

    @JsonProperty("EntrustBookEndTime")
    private long EntrustBookEndTime;//委托书截止时间

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

    @JsonIgnore
    public SyzlEnterpriseGmp getEnterpriseFirstInformation() {
        return EnterpriseFirstInformation;
    }

    @JsonIgnore
    public List<SyzlProductGmp> getProductFirstInformationList() {
        return ProductFirstInformationList;
    }

    @JsonIgnore
    public List<String> getEntrustBookUrl() {
        return EntrustBookUrl;
    }

    public void setEntrustBookUrl(List<String> entrustBookUrl) {
        EntrustBookUrl = entrustBookUrl;
    }
}
