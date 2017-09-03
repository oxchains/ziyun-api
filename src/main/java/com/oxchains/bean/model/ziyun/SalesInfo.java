package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * Created by root on 17-7-3.
 * 销售信息
 */
public class SalesInfo extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("No")
    private String No;//销售单号

    @JsonProperty("SalesTitle")
    private String SalesTitle;//销售标题

    @JsonProperty("PurchaseId")
    private String PurchaseId;//进货id

    @JsonProperty("ProductAddress")
    private String ProductAddress;//产地

    @JsonProperty("ProductionName")
    private String ProductionName;//产品名称

    @JsonProperty("ProductionSpecification")
    private String ProductionSpecification;//产品规格

    @JsonProperty("CreateSalesEnterpriseId")
    private String CreateSalesEnterpriseId;//生产销售企业id

    @JsonProperty("TranstitSalesEnterpriseId")
    private String TranstitSalesEnterpriseId;//批发销售企业id

    @JsonProperty("SalesCount")
    private int SalesCount;//销售数量

    @JsonProperty("ProductTime")
    private long ProductTime;//生产时间 时间戳

    @JsonProperty("ProductBatch")
    private String ProductBatch;//生产批次

    @JsonProperty("ProductDeadline")
    private int ProductDeadline;//保质期(月)

    @JsonProperty("GoodsOriginalUrl")
    private String GoodsOriginalUrl;//货物图片路径

    @JsonProperty("SalesDate")
    private long SalesDate;//销售日期 时间戳

    @JsonProperty("BuyerName")
    private String BuyerName;//购货人姓名

    @JsonProperty("BuyerAddress")
    private String BuyerAddress;//购货人地址

    @JsonProperty("BuyerTel")
    private String BuyerTel;//购货人联系方式

    @JsonProperty("ResponsibilityName")
    private String ResponsibilityName;//负责人姓名

    @JsonProperty("InspectionCertificateNumber")
    private String InspectionCertificateNumber;//合格证号

    @JsonProperty("ProductionProcessId")
    private String ProductionProcessId;//生产过程id

    @JsonProperty("GoodsId")
    private String GoodsId;//货物id

    @JsonProperty("SalsesId")
    private String SalsesId;//销售人员id

    @JsonProperty("UniqueCodes")
    private List<String> UniqueCodes;//货物唯一码List集合

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//销售企业id

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

    @JsonProperty("SalesInvoiceUrl")
    private String SalesInvoiceUrl;//发票

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
