package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 产品首营资料
 * Created by root on 17-8-8.
 */
public class ProductGmp extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//企业id

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

    @JsonProperty("ApprovalUrl")
    private String ApprovalUrl;//产品生产批号附件地址

    @JsonProperty("ApprovalNo")
    private String ApprovalNo;//产品生产批号编号

    @JsonProperty("ProductCode")
    private String ProductCode;//产品编码

    @JsonProperty("ProductPatentCertificateUrl")
    private String ProductPatentCertificateUrl;//产品专利证书

    @JsonProperty("ProductTrademarkDocumentsUrl")
    private String ProductTrademarkDocumentsUrl;//产品商标证书

    @JsonProperty("ProductName")
    private String ProductName;//产品名称

    @JsonProperty("ProductMiniPackageUrl")
    private String ProductMiniPackageUrl;//产品最小包装图片地址

    @JsonProperty("DrugInstructionsUrl")
    private String DrugInstructionsUrl;//药品说明书复印件

    @JsonProperty("GeneralTaxpayerRecordsUrl")
    private String GeneralTaxpayerRecordsUrl;//一般纳税人认定记录附件

    @JsonProperty("LegalPowerOfAttorneyUrl")
    private String LegalPowerOfAttorneyUrl;//法人委托书附件

    @JsonProperty("IdCardUrl")
    private String IdCardUrl;//身份证复印件

    @JsonProperty("ProudctProduceStandardUrl")
    private String ProudctProduceStandardUrl;//产品生产标准附件

    @JsonProperty("PurchaseAndSaleContractUrl")
    private String PurchaseAndSaleContractUrl;//购销合同附件

    @JsonProperty("ProductPackageAndManualUrl")
    private String ProductPackageAndManualUrl;//产品内外包装和说明书

    @JsonProperty("ProductProvincialPnspectionReport")
    private List<ProductProvincialPnspectionReport> ProductProvincialPnspectionReport;// 省级产品检验报告

    @JsonProperty("ProductPriceDocument")
    private List<ProductPriceDocument> ProductPriceDocument;// 产品物价文件

    @JsonProperty("ProductFactoryInspectionReport")
    private List<ProductFactoryInspectionReport> ProductFactoryInspectionReport;//厂检验报告

    @JsonProperty("PurchaserCertificate")
    private List<PurchaserCertificate> PurchaserCertificate;// 购销员资格证书

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
