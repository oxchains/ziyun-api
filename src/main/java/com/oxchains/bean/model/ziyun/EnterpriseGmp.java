package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 企业首营资料
 * Created by root on 17-8-8.
 */
public class EnterpriseGmp extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//企业id

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

    @JsonProperty("EnterpriseType")
    private String EnterpriseType;//企业类型

    @JsonProperty("EnterpriseLicenseUrl")
    private String EnterpriseLicenseUrl;//营业执照附件

    @JsonProperty("EnterpriseLicenseNo")
    private String EnterpriseLicenseNo;//营业执照编号

    @JsonProperty("TaxRegistrationCertificateUrl")
    private String TaxRegistrationCertificateUrl;//税务登记证

    @JsonProperty("TaxRegistrationCode")
    private String TaxRegistrationCode;//税务登记证编号

    @JsonProperty("OrganizationCodeCertificateUrl")
    private String OrganizationCodeCertificateUrl;//组织机构代码证

    @JsonProperty("OrganizationCode")
    private String OrganizationCode;//组织机构代码编号

    @JsonProperty("QualityAssuranceUrl")
    private String QualityAssuranceUrl;//质量保证协议书

    @JsonProperty("DrugProductionLicenseUrl")
    private String DrugProductionLicenseUrl;//药品生产许可证附件

    @JsonProperty("DrugProductionLicenseNo")
    private String DrugProductionLicenseNo;//药品生产许可证编号

    @JsonProperty("GoodManufacturPracticesUrl")
    private String GoodManufacturPracticesUrl;//药品生产质量管理规范附件

    @JsonProperty("DrugOperatingLicenseUrl")
    private String DrugOperatingLicenseUrl;//药品经营许可证附件

    @JsonProperty("DrugOperatingLicenseNo")
    private String DrugOperatingLicenseNo;//药品经营许可证编号

    @JsonProperty("GoodSupplyingPracticesUrl")
    private String GoodSupplyingPracticesUrl;//药品经营质量管理规范附件

    @JsonProperty("OpeningPermitNo")
    private String OpeningPermitNo;//开户许可证编号

    @JsonProperty("OpeningPermitUrl")
    private String OpeningPermitUrl;//开户许可证附件

    @JsonProperty("OpenBank")
    private String OpenBank;//开户行

    @JsonProperty("BankAccountNumber")
    private String BankAccountNumber;//开户行账号

    @JsonProperty("BillingUnit")
    private String BillingUnit;//开票单位

    @JsonProperty("TaxpayerIdentificationNumber")
    private String TaxpayerIdentificationNumber;//纳税人识别号

    @JsonProperty("EnterprisePhone")
    private String EnterprisePhone;//企业电话

    @JsonProperty("EnterpriseAdress")
    private String EnterpriseAdress;//企业地址

    @JsonProperty("YearTaxReport")
    private List<Map<String,String>> YearTaxReport;// 年纳税报表

    @JsonProperty("EnterpriseQualityQuestionnaire")
    private List<Map<String,String>> EnterpriseQualityQuestionnaire;// 企业质量情况调查表

    @JsonProperty("DeliveryUnitQualityQuestionnaire")
    private List<Map<String,String>> DeliveryUnitQualityQuestionnaire;//企业质量情况调查表

    @JsonProperty("Token")
    private String Token;//token

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    private String TxId;

    public void setTxId(String txId) {
        TxId = txId;
    }

    public String getTxId() {
        return TxId;
    }

    @JsonIgnore
    public String getEnterpriseLicenseUrl() {
        return EnterpriseLicenseUrl;
    }

    public void setEnterpriseLicenseUrl(String enterpriseLicenseUrl) {
        EnterpriseLicenseUrl = enterpriseLicenseUrl;
    }
    @JsonIgnore
    public String getTaxRegistrationCertificateUrl() {
        return TaxRegistrationCertificateUrl;
    }

    public void setTaxRegistrationCertificateUrl(String taxRegistrationCertificateUrl) {
        TaxRegistrationCertificateUrl = taxRegistrationCertificateUrl;
    }
    @JsonIgnore
    public String getOrganizationCodeCertificateUrl() {
        return OrganizationCodeCertificateUrl;
    }

    public void setOrganizationCodeCertificateUrl(String organizationCodeCertificateUrl) {
        OrganizationCodeCertificateUrl = organizationCodeCertificateUrl;
    }
    @JsonIgnore
    public String getQualityAssuranceUrl() {
        return QualityAssuranceUrl;
    }

    public void setQualityAssuranceUrl(String qualityAssuranceUrl) {
        QualityAssuranceUrl = qualityAssuranceUrl;
    }
    @JsonIgnore
    public String getDrugProductionLicenseUrl() {
        return DrugProductionLicenseUrl;
    }

    public void setDrugProductionLicenseUrl(String drugProductionLicenseUrl) {
        DrugProductionLicenseUrl = drugProductionLicenseUrl;
    }
    @JsonIgnore
    public String getGoodManufacturPracticesUrl() {
        return GoodManufacturPracticesUrl;
    }

    public void setGoodManufacturPracticesUrl(String goodManufacturPracticesUrl) {
        GoodManufacturPracticesUrl = goodManufacturPracticesUrl;
    }
    @JsonIgnore
    public String getDrugOperatingLicenseUrl() {
        return DrugOperatingLicenseUrl;
    }

    public void setDrugOperatingLicenseUrl(String drugOperatingLicenseUrl) {
        DrugOperatingLicenseUrl = drugOperatingLicenseUrl;
    }
    @JsonIgnore
    public String getGoodSupplyingPracticesUrl() {
        return GoodSupplyingPracticesUrl;
    }

    public void setGoodSupplyingPracticesUrl(String goodSupplyingPracticesUrl) {
        GoodSupplyingPracticesUrl = goodSupplyingPracticesUrl;
    }
    @JsonIgnore
    public String getOpeningPermitUrl() {
        return OpeningPermitUrl;
    }

    public void setOpeningPermitUrl(String openingPermitUrl) {
        OpeningPermitUrl = openingPermitUrl;
    }

    @JsonIgnore
    public List<Map<String,String>> getYearTaxReport() {
        return YearTaxReport;
    }
    @JsonIgnore
    public List<Map<String,String>> getEnterpriseQualityQuestionnaire() {
        return EnterpriseQualityQuestionnaire;
    }
    @JsonIgnore
    public List<Map<String,String>> getDeliveryUnitQualityQuestionnaire() {
        return DeliveryUnitQualityQuestionnaire;
    }
}
