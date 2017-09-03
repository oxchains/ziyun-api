package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * 批发零售信息
 * Created by root on 17-7-24.
 */
public class TransitSalesInfo extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("TransitSalesName")
    private String TransitSalesName;//销售标题

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//批发零售企业ID

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

    @JsonProperty("Type")
    private String Type;//类型

    @JsonProperty("TransitSalesType")
    private String TransitSalesType;//销售类型(批发,零售)

    @JsonProperty("SalesDate")
    private long SalesDate;//销售时间 (时间戳)

    @JsonProperty("CreateTime")
    private long CreateTime;//销售单创建时间 时间戳

    @JsonProperty("BuyerName")
    private String BuyerName;//购货人姓名

    @JsonProperty("BuyerAddress")
    private String BuyerAddress;//购货人地址

    @JsonProperty("BuyerTel")
    private String BuyerTel;//购货人联系方式

    @JsonProperty("UniqueCodes")
    private List<String> UniqueCodes;//货物唯一码List集合

    @JsonProperty("TransitSalesState")
    private  int TransitSalesState;// 销售状态

    @JsonProperty("TransitSalesMoney")
    private double TransitSalesMoney;//销售金额

    @JsonProperty("TransitSalesInvoiceUrl")
    private String TransitSalesInvoiceUrl;//发票

    @JsonProperty("BuyEnterpriseId")
    private String BuyEnterpriseId;//购买企业id

    @JsonProperty("BuyEnterpriseName")
    private String BuyEnterpriseName;//购买企业名称

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
