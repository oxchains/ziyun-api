package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * 产品首营资料
 * Created by root on 17-8-8.
 */
public class SyzlProductGmp extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("No")
    private String No;

    @JsonProperty("SyzlEnterpriseId")
    private String SyzlEnterpriseId;//所属企业ID

    @JsonProperty("ProductFirstInformationExchangeState")
    private String ProductFirstInformationExchangeState;//产品交换状态

    @JsonProperty("ProductFirstInformationSignatureState")
    private String ProductFirstInformationSignatureState;//产品签章状态

    @JsonProperty("ProductName")
    private String ProductName;//产品名称

    @JsonProperty("ApprovalNo")
    private String ApprovalNo;//产品批号

    @JsonProperty("ApprovalUrl")
    private List<String> ApprovalUrl;//产品批号附件

    @JsonProperty("ProductPatentUrl")
    private List<String> ProductPatentUrl;//产品专利附件

    @JsonProperty("ProductTrademarkUrl")
    private List<String> ProductTrademarkUrl;//产品商标附件

    @JsonProperty("SmallestPackageUrl")
    private List<String> SmallestPackageUrl;//最小包装图

    @JsonProperty("DrugDescriptionUrl")
    private List<String> DrugDescriptionUrl;//药品说明书附件

    @JsonProperty("TaxpayerRecordUrl")
    private List<String> TaxpayerRecordUrl;//一般纳税人认定记录附件

    @JsonProperty("FrwtsUrl")
    private List<String> FrwtsUrl;//法人委托书附件

    @JsonProperty("IdCardUrl")
    private List<String> IdCardUrl;//身份证附件

    @JsonProperty("CpscbzUrl")
    private List<String> CpscbzUrl;//产品生产标准附件

    @JsonProperty("GxhtUrl")
    private List<String> GxhtUrl;//购销合同附件

    @JsonProperty("CpbzsmsfjUrl")
    private List<String> CpbzsmsfjUrl;//产品包装说明书附件

    @JsonProperty("SjjybgUrl")
    private List<String> SjjybgUrl;//省级产品检验报告

    @JsonProperty("CpwjwjUrl")
    private List<String> CpwjwjUrl;//产品物价文件

    @JsonProperty("MpcpcjbgUrl")
    private List<String> MpcpcjbgUrl;//每批产品厂检报告

    @JsonProperty("GxyzgzsUrl")
    private List<String> GxyzgzsUrl;//购销员资格证书

    @JsonIgnore
    public List<String> getApprovalUrl() {
        return ApprovalUrl;
    }

    public void setApprovalUrl(List<String> approvalUrl) {
        ApprovalUrl = approvalUrl;
    }

    @JsonIgnore
    public List<String> getProductPatentUrl() {
        return ProductPatentUrl;
    }

    public void setProductPatentUrl(List<String> productPatentUrl) {
        ProductPatentUrl = productPatentUrl;
    }

    @JsonIgnore
    public List<String> getProductTrademarkUrl() {
        return ProductTrademarkUrl;
    }

    public void setProductTrademarkUrl(List<String> productTrademarkUrl) {
        ProductTrademarkUrl = productTrademarkUrl;
    }

    @JsonIgnore
    public List<String> getSmallestPackageUrl() {
        return SmallestPackageUrl;
    }

    public void setSmallestPackageUrl(List<String> smallestPackageUrl) {
        SmallestPackageUrl = smallestPackageUrl;
    }

    @JsonIgnore
    public List<String> getDrugDescriptionUrl() {
        return DrugDescriptionUrl;
    }

    public void setDrugDescriptionUrl(List<String> drugDescriptionUrl) {
        DrugDescriptionUrl = drugDescriptionUrl;
    }

    @JsonIgnore
    public List<String> getTaxpayerRecordUrl() {
        return TaxpayerRecordUrl;
    }

    public void setTaxpayerRecordUrl(List<String> taxpayerRecordUrl) {
        TaxpayerRecordUrl = taxpayerRecordUrl;
    }

    @JsonIgnore
    public List<String> getFrwtsUrl() {
        return FrwtsUrl;
    }

    public void setFrwtsUrl(List<String> frwtsUrl) {
        FrwtsUrl = frwtsUrl;
    }

    @JsonIgnore
    public List<String> getIdCardUrl() {
        return IdCardUrl;
    }

    public void setIdCardUrl(List<String> idCardUrl) {
        IdCardUrl = idCardUrl;
    }

    @JsonIgnore
    public List<String> getCpscbzUrl() {
        return CpscbzUrl;
    }

    public void setCpscbzUrl(List<String> cpscbzUrl) {
        CpscbzUrl = cpscbzUrl;
    }

    @JsonIgnore
    public List<String> getGxhtUrl() {
        return GxhtUrl;
    }

    public void setGxhtUrl(List<String> gxhtUrl) {
        GxhtUrl = gxhtUrl;
    }

    @JsonIgnore
    public List<String> getCpbzsmsfjUrl() {
        return CpbzsmsfjUrl;
    }

    public void setCpbzsmsfjUrl(List<String> cpbzsmsfjUrl) {
        CpbzsmsfjUrl = cpbzsmsfjUrl;
    }

    @JsonIgnore
    public List<String> getSjjybgUrl() {
        return SjjybgUrl;
    }

    public void setSjjybgUrl(List<String> sjjybgUrl) {
        SjjybgUrl = sjjybgUrl;
    }

    @JsonIgnore
    public List<String> getCpwjwjUrl() {
        return CpwjwjUrl;
    }

    public void setCpwjwjUrl(List<String> cpwjwjUrl) {
        CpwjwjUrl = cpwjwjUrl;
    }

    @JsonIgnore
    public List<String> getMpcpcjbgUrl() {
        return MpcpcjbgUrl;
    }

    public void setMpcpcjbgUrl(List<String> mpcpcjbgUrl) {
        MpcpcjbgUrl = mpcpcjbgUrl;
    }

    @JsonIgnore
    public List<String> getGxyzgzsUrl() {
        return GxyzgzsUrl;
    }

    public void setGxyzgzsUrl(List<String> gxyzgzsUrl) {
        GxyzgzsUrl = gxyzgzsUrl;
    }

    @JsonProperty("Token")
    private String Token;//token

    @JsonIgnore
    public String getToken() {
        return Token;
    }


    private String TxId;


    public String getTxId() {
        return TxId;
    }

    public void setTxId(String txId) {
        TxId = txId;
    }

    public void setToken(String token) {
        Token = token;
    }

}
