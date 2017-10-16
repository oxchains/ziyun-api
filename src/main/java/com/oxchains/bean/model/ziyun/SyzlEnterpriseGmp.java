package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 企业首营资料
 * Created by root on 17-8-8.
 */
public class SyzlEnterpriseGmp extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("No")
    private String No;//编号

    @JsonProperty("ReturnReason")
    private String ReturnReason;//退回原因

    @JsonProperty("EnterpriseType")
    private String EnterpriseType;//企业类型

    @JsonProperty("SyzlEnterpriseId")
    private String SyzlEnterpriseId;//企业ID

    @JsonProperty("EnterpriseName")
    private String EnterpriseName;//企业名字

    @JsonProperty("YyzzUrl")
    private List<String> YyzzUrl;//营业执照

    @JsonProperty("YyzzExchangeState")
    private String YyzzExchangeState;//营业执照交换状态

    @JsonProperty("YyzzSignatureState")
    private String YyzzSignatureState;//营业执照签章状态

    @JsonProperty("DistributionAgreementUrl")
    private List<String> DistributionAgreementUrl;//配送协议

    @JsonProperty("DistributionAgreementExchangeState")
    private String DistributionAgreementExchangeState;//配送协议交换状态

    @JsonProperty("DistributionAgreementSignatureState")
    private String DistributionAgreementSignatureState;//配送协议签章状态

    @JsonProperty("InvoiceUrl")
    private List<String> InvoiceUrl;//发票

    @JsonProperty("InvoiceExchangeState")
    private String InvoiceExchangeState;//发票交换状态

    @JsonProperty("InvoiceSignatureState")
    private String InvoiceSignatureState;//发票签章状态

    @JsonProperty("YpjyxkzUrl")
    private List<String> YpjyxkzUrl;//药品经营许可证

    @JsonProperty("YpjyxkzExchangeState")
    private String YpjyxkzExchangeState;//药品经营许可证交换状态

    @JsonProperty("YpjyxkzSignatureState")
    private String YpjyxkzSignatureState;//药品经营许可证签章状态

    @JsonProperty("SignStatus")
    private String SignStatus;//企业首营资料签收状态

    @JsonProperty("QybghztzsUrl")
    private List<String> QybghztzsUrl;//企业变更核准通知书

    @JsonProperty("QybghztzsExchangeState")
    private String QybghztzsExchangeState;//企业变更核准通知书交换状态

    @JsonProperty("QybghztzsSignatureState")
    private String QybghztzsSignatureState;//企业变更核准通知书签章状态

    @JsonProperty("GsnjUrl")
    private List<String> GsnjUrl;//工商年检

    @JsonProperty("GsnjExchangeState")
    private String GsnjExchangeState;//工商年检交换状态

    @JsonProperty("GsnjSignatureState")
    private String GsnjSignatureState;//工商年检签章状态

    @JsonProperty("YhkhxkzUrl")
    private List<String> YhkhxkzUrl;//银行开户许可证

    @JsonProperty("YhkhxkzExchangeState")
    private String YhkhxkzExchangeState;//银行开户许可证交换状态

    @JsonProperty("YhkhxkzSignatureState")
    private String YhkhxkzSignatureState;//银行开户许可证签章状态

    @JsonProperty("ZzszyfpybUrl")
    private List<String> ZzszyfpybUrl;// 增值税专用发票样张

    @JsonProperty("ZzszyfpybExchangeState")
    private String ZzszyfpybExchangeState;// 增值税专用发票样张交换状态

    @JsonProperty("ZzszyfpybSignatureState")
    private String ZzszyfpybSignatureState;//增值税专用发票样张签章状态

    @JsonProperty("KpInfoUrl")
    private List<String> KpInfoUrl;//开票信息

    @JsonProperty("KpInfoExchangeState")
    private String KpInfoExchangeState;//开票信息交换状态

    @JsonProperty("KpInfoSignatureState")
    private String KpInfoSignatureState;//开票信息签章状态

    @JsonProperty("YzymbaUrl")
    private List<String> YzymbaUrl;//印章/印模备案

    @JsonProperty("YzymbaExchangeState")
    private String YzymbaExchangeState;//印章/印模备案交换状态

    @JsonProperty("YzymbaSignatureState")
    private String YzymbaSignatureState;//印章/印模备案签章状态

    @JsonProperty("BlankSalesContractUrl")
    private List<String> BlankSalesContractUrl;//空白销售合同

    @JsonProperty("BlankSalesContractExchangeState")
    private String BlankSalesContractExchangeState;//印章/印模备案交换状态

    @JsonProperty("BlankSalesContractSignatureState")
    private String BlankSalesContractSignatureState;//印章/印模备案签章状态

    @JsonProperty("ShtxdyzUrl")
    private List<String> ShtxdyzUrl;//随货同行单样张

    @JsonProperty("ShtxdyzExchangeState")
    private String ShtxdyzExchangeState;//随货同行单样张交换状态

    @JsonProperty("ShtxdyzSignatureState")
    private String ShtxdyzSignatureState;//随货同行单样张签章状态

    @JsonProperty("QysyndbgUrl")
    private List<String> QysyndbgUrl;//企业上一年度报告

    @JsonProperty("QysyndbgExchangeState")
    private String QysyndbgExchangeState;//企业上一年度报告交换状态

    @JsonProperty("QysyndbgSignatureState")
    private String QysyndbgSignatureState;//企业上一年度报告签章状态

    @JsonProperty("ZltxdcbUrl")
    private List<String> ZltxdcbUrl;//质量体系调查表

    @JsonProperty("ZltxdcbExchangeState")
    private String ZltxdcbExchangeState;//质量体系调查表交换状态

    @JsonProperty("ZltxdcbSignatureState")
    private String ZltxdcbSignatureState;//质量体系调查表签章状态

    @JsonProperty("HgghfdabUrl")
    private List<String> HgghfdabUrl;//合格供货方档案表

    @JsonProperty("HgghfdabExchangeState")
    private String HgghfdabExchangeState;//合格供货方档案表交换状态

    @JsonProperty("HgghfdabSignatureState")
    private String HgghfdabSignatureState;//合格供货方档案表签章状态

    @JsonProperty("YljxjyxkzUrl")
    private List<String> YljxjyxkzUrl;//医疗机械经营许可证

    @JsonProperty("YljxjyxkzExchangeState")
    private String YljxjyxkzExchangeState;//医疗机械经营许可证交换状态

    @JsonProperty("YljxjyxkzSignatureState")
    private String YljxjyxkzSignatureState;//医疗机械经营许可证签章状态

    @JsonProperty("YpjyzlglgfrzsUrl")
    private List<String> YpjyzlglgfrzsUrl;//GSP（药品经营质量管理规范认证书）

    @JsonProperty("YpjyzlglgfrzsExchangeState")
    private String YpjyzlglgfrzsExchangeState;//GSP（药品经营质量管理规范认证书）交换状态

    @JsonProperty("YpjyzlglgfrzsSignatureState")
    private String YpjyzlglgfrzsSignatureState;//GSP（药品经营质量管理规范认证书）签章状态

    @JsonProperty("SalesIdCardUrl")
    private List<String> SalesIdCardUrl;// 销售身份证复印件

    @JsonProperty("SalesIdCardExchangeState")
    private String SalesIdCardExchangeState;// 销售身份证复印件交换状态

    @JsonProperty("SalesIdCardSignatureState")
    private String SalesIdCardSignatureState;//销售身份证复印件签章状态

    @JsonProperty("FrwtsyjUrl")
    private List<String> FrwtsyjUrl;//法人委托书原件

    @JsonProperty("FrwtsyjExchangeState")
    private String FrwtsyjExchangeState;//法人委托书原件交换状态

    @JsonProperty("FrwtsyjSignatureState")
    private String FrwtsyjSignatureState;//法人委托书原件签章状态

    @JsonProperty("EducationProveUrl")
    private List<String> EducationProveUrl;//学历证明

    @JsonProperty("EducationProveExchangeState")
    private String EducationProveExchangeState;//学历证明交换状态

    @JsonProperty("EducationProveSignatureState")
    private String EducationProveSignatureState;//学历证明签章状态

    @JsonProperty("YljgzyxkzUrl")
    private List<String> YljgzyxkzUrl;//医疗机构执业许可证

    @JsonProperty("YljgzyxkzExchangeState")
    private String YljgzyxkzExchangeState;//医疗机构执业许可证交换状态

    @JsonProperty("YljgzyxkzSignatureState")
    private String YljgzyxkzSignatureState;//医疗机构执业许可证签章状态

    @JsonProperty("ZyyszgzUrl")
    private List<String> ZyyszgzUrl;//执业医师资格证

    @JsonProperty("ZyyszgzExchangeState")
    private String ZyyszgzExchangeState;//执业医师资格证交换状态

    @JsonProperty("ZyyszgzSignatureState")
    private String ZyyszgzSignatureState;//执业医师资格证签章状态

    @JsonProperty("YpsczlglgfrzsUrl")
    private List<String> YpsczlglgfrzsUrl;//GMP（药品生产质量管理规范认证书）

    @JsonProperty("YpsczlglgfrzsExchangeState")
    private String YpsczlglgfrzsExchangeState;//GMP（药品生产质量管理规范认证书）交换状态

    @JsonProperty("YpsczlglgfrzsSignatureState")
    private String YpsczlglgfrzsSignatureState;//GMP（药品生产质量管理规范认证书）签章状态

    @JsonProperty("YljxscxkbabUrl")
    private List<String> YljxscxkbabUrl;//医疗机械生产许可备案表（一类）

    @JsonProperty("YljxscxkbabExchangeState")
    private String YljxscxkbabExchangeState;//医疗机械生产许可备案表（一类）交换状态

    @JsonProperty("YljxscxkbabSignatureState")
    private String YljxscxkbabSignatureState;//医疗机械生产许可备案表（一类）签章状态

    @JsonProperty("YljxscxkzUrl")
    private List<String> YljxscxkzUrl;//医疗机械生产许可证（二三类）

    @JsonProperty("YljxscxkzExchangeState")
    private String YljxscxkzExchangeState;//医疗机械生产许可证（二三类）交换状态

    @JsonProperty("YljxscxkzSignatureState")
    private String YljxscxkzSignatureState;//医疗机械生产许可证（二三类）签章状态

    @JsonProperty("YpzcpjUrl")
    private List<String> YpzcpjUrl;//药品注册批件

    @JsonProperty("YpzcpjExchangeState")
    private String YpzcpjExchangeState;//药品注册批件交换状态

    @JsonProperty("YpzcpjSignatureState")
    private String YpzcpjSignatureState;//药品注册批件签章状态

    @JsonProperty("YpzcpjNextUrl")
    private List<String> YpzcpjNextUrl;//药品再注册批件

    @JsonProperty("YpzcpjNextExchangeState")
    private String YpzcpjNextExchangeState;//药品再注册批件交换状态

    @JsonProperty("YpzcpjNextSignatureState")
    private String YpzcpjNextSignatureState;//药品再注册批件签章状态

    @JsonProperty("YpbcpjUrl")
    private List<String> YpbcpjUrl;// 药品补充批件

    @JsonProperty("YpbcpjExchangeState")
    private String YpbcpjExchangeState;// 药品补充批件交换状态

    @JsonProperty("YpbcpjSignatureState")
    private String YpbcpjSignatureState;//药品补充批件签章状态

    @JsonProperty("XyzsUrl")
    private List<String> XyzsUrl;//新药证书

    @JsonProperty("XyzsExchangeState")
    private String XyzsExchangeState;//新药证书交换状态

    @JsonProperty("XyzsSignatureState")
    private String XyzsSignatureState;//新药证书签章状态

    @JsonProperty("CpzlbzUrl")
    private List<String> CpzlbzUrl;//产品质量标准

    @JsonProperty("CpzlbzExchangeState")
    private String CpzlbzExchangeState;//产品质量标准交换状态

    @JsonProperty("CpzlbzSignatureState")
    private String CpzlbzSignatureState;//产品质量标准签章状态

    @JsonProperty("ZlzsUrl")
    private List<String> ZlzsUrl;//专利证书

    @JsonProperty("ZlzsExchangeState")
    private String ZlzsExchangeState;//专利证书交换状态

    @JsonProperty("ZlzsSignatureState")
    private String ZlzsSignatureState;//专利证书签章状态

    @JsonProperty("JkypzczUrl")
    private List<String> JkypzczUrl;//进口药品注册证

    @JsonProperty("JkypzczExchangeState")
    private String JkypzczExchangeState;//进口药品注册证交换状态

    @JsonProperty("JkypzczSignatureState")
    private String JkypzczSignatureState;//进口药品注册证签章状态

    @JsonProperty("PriceApprovalUrl")
    private List<String> PriceApprovalUrl;//价格批文

    @JsonProperty("PriceApprovalExchangeState")
    private String PriceApprovalExchangeState;//价格批文交换状态

    @JsonProperty("PriceApprovalSignatureState")
    private String PriceApprovalSignatureState;//价格批文签章状态

    @JsonProperty("YpjybgUrl")
    private List<String> YpjybgUrl;//药品检验报告

    @JsonProperty("YpjybgExchangeState")
    private String YpjybgExchangeState;//药品检验报告交换状态

    @JsonProperty("YpjybgSignatureState")
    private String YpjybgSignatureState;//药品检验报告签章状态

    @JsonProperty("BoxUrl")
    private List<String> BoxUrl;//包装盒

    @JsonProperty("BoxExchangeState")
    private String BoxExchangeState;//包装盒交换状态

    @JsonProperty("BoxSignatureState")
    private String BoxSignatureState;//包装盒签章状态

    @JsonProperty("LabelUrl")
    private List<String> LabelUrl;// 标签

    @JsonProperty("LabelExchangeState")
    private String LabelExchangeState;// 标签交换状态

    @JsonProperty("LabelSignatureState")
    private String LabelSignatureState;//标签签章状态

    @JsonProperty("DescriptionBookUrl")
    private List<String> DescriptionBookUrl;//说明书

    @JsonProperty("DescriptionBookExchangeState")
    private String DescriptionBookExchangeState;//说明书交换状态

    @JsonProperty("DescriptionBookSignatureState")
    private String DescriptionBookSignatureState;//说明书签章状态

    @JsonProperty("YpjgjgzdbaUrl")
    private List<String> YpjgjgzdbaUrl;// 有药监局盖章的备案（包装、说明书、注射液标签等）

    @JsonProperty("YpjgjgzdbaExchangeState")
    private String YpjgjgzdbaExchangeState;// 有药监局盖章的备案（包装、说明书、注射液标签等）交换状态

    @JsonProperty("YpjgjgzdbaSignatureState")
    private String YpjgjgzdbaSignatureState;//有药监局盖章的备案（包装、说明书、注射液标签等）签章状态

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
    public List<String> getYyzzUrl() {
        return YyzzUrl;
    }

    public void setYyzzUrl(List<String> yyzzUrl) {
        YyzzUrl = yyzzUrl;
    }

    @JsonIgnore
    public List<String> getDistributionAgreementUrl() {
        return DistributionAgreementUrl;
    }

    public void setDistributionAgreementUrl(List<String> distributionAgreementUrl) {
        DistributionAgreementUrl = distributionAgreementUrl;
    }

    @JsonIgnore
    public List<String> getInvoiceUrl() {
        return InvoiceUrl;
    }

    public void setInvoiceUrl(List<String> invoiceUrl) {
        InvoiceUrl = invoiceUrl;
    }

    @JsonIgnore
    public List<String> getYpjyxkzUrl() {
        return YpjyxkzUrl;
    }

    public void setYpjyxkzUrl(List<String> ypjyxkzUrl) {
        YpjyxkzUrl = ypjyxkzUrl;
    }

    @JsonIgnore
    public List<String> getQybghztzsUrl() {
        return QybghztzsUrl;
    }

    public void setQybghztzsUrl(List<String> qybghztzsUrl) {
        QybghztzsUrl = qybghztzsUrl;
    }

    @JsonIgnore
    public List<String> getGsnjUrl() {
        return GsnjUrl;
    }

    public void setGsnjUrl(List<String> gsnjUrl) {
        GsnjUrl = gsnjUrl;
    }

    @JsonIgnore
    public List<String> getYhkhxkzUrl() {
        return YhkhxkzUrl;
    }

    public void setYhkhxkzUrl(List<String> yhkhxkzUrl) {
        YhkhxkzUrl = yhkhxkzUrl;
    }

    @JsonIgnore
    public List<String> getZzszyfpybUrl() {
        return ZzszyfpybUrl;
    }

    public void setZzszyfpybUrl(List<String> zzszyfpybUrl) {
        ZzszyfpybUrl = zzszyfpybUrl;
    }

    @JsonIgnore
    public List<String> getKpInfoUrl() {
        return KpInfoUrl;
    }

    public void setKpInfoUrl(List<String> kpInfoUrl) {
        KpInfoUrl = kpInfoUrl;
    }

    @JsonIgnore
    public List<String> getYzymbaUrl() {
        return YzymbaUrl;
    }

    public void setYzymbaUrl(List<String> yzymbaUrl) {
        YzymbaUrl = yzymbaUrl;
    }

    @JsonIgnore
    public List<String> getBlankSalesContractUrl() {
        return BlankSalesContractUrl;
    }

    public void setBlankSalesContractUrl(List<String> blankSalesContractUrl) {
        BlankSalesContractUrl = blankSalesContractUrl;
    }

    @JsonIgnore
    public List<String> getShtxdyzUrl() {
        return ShtxdyzUrl;
    }

    public void setShtxdyzUrl(List<String> shtxdyzUrl) {
        ShtxdyzUrl = shtxdyzUrl;
    }

    @JsonIgnore
    public List<String> getQysyndbgUrl() {
        return QysyndbgUrl;
    }

    public void setQysyndbgUrl(List<String> qysyndbgUrl) {
        QysyndbgUrl = qysyndbgUrl;
    }

    @JsonIgnore
    public List<String> getZltxdcbUrl() {
        return ZltxdcbUrl;
    }

    public void setZltxdcbUrl(List<String> zltxdcbUrl) {
        ZltxdcbUrl = zltxdcbUrl;
    }

    @JsonIgnore
    public List<String> getHgghfdabUrl() {
        return HgghfdabUrl;
    }

    public void setHgghfdabUrl(List<String> hgghfdabUrl) {
        HgghfdabUrl = hgghfdabUrl;
    }

    @JsonIgnore
    public List<String> getYljxjyxkzUrl() {
        return YljxjyxkzUrl;
    }

    public void setYljxjyxkzUrl(List<String> yljxjyxkzUrl) {
        YljxjyxkzUrl = yljxjyxkzUrl;
    }

    @JsonIgnore
    public List<String> getYpjyzlglgfrzsUrl() {
        return YpjyzlglgfrzsUrl;
    }

    public void setYpjyzlglgfrzsUrl(List<String> ypjyzlglgfrzsUrl) {
        YpjyzlglgfrzsUrl = ypjyzlglgfrzsUrl;
    }

    @JsonIgnore
    public List<String> getSalesIdCardUrl() {
        return SalesIdCardUrl;
    }

    public void setSalesIdCardUrl(List<String> salesIdCardUrl) {
        SalesIdCardUrl = salesIdCardUrl;
    }

    @JsonIgnore
    public List<String> getFrwtsyjUrl() {
        return FrwtsyjUrl;
    }

    public void setFrwtsyjUrl(List<String> frwtsyjUrl) {
        FrwtsyjUrl = frwtsyjUrl;
    }

    @JsonIgnore
    public List<String> getEducationProveUrl() {
        return EducationProveUrl;
    }

    public void setEducationProveUrl(List<String> educationProveUrl) {
        EducationProveUrl = educationProveUrl;
    }

    @JsonIgnore
    public List<String> getYljgzyxkzUrl() {
        return YljgzyxkzUrl;
    }

    public void setYljgzyxkzUrl(List<String> yljgzyxkzUrl) {
        YljgzyxkzUrl = yljgzyxkzUrl;
    }

    @JsonIgnore
    public List<String> getZyyszgzUrl() {
        return ZyyszgzUrl;
    }

    public void setZyyszgzUrl(List<String> zyyszgzUrl) {
        ZyyszgzUrl = zyyszgzUrl;
    }

    @JsonIgnore
    public List<String> getYpsczlglgfrzsUrl() {
        return YpsczlglgfrzsUrl;
    }

    public void setYpsczlglgfrzsUrl(List<String> ypsczlglgfrzsUrl) {
        YpsczlglgfrzsUrl = ypsczlglgfrzsUrl;
    }

    @JsonIgnore
    public List<String> getYljxscxkbabUrl() {
        return YljxscxkbabUrl;
    }

    public void setYljxscxkbabUrl(List<String> yljxscxkbabUrl) {
        YljxscxkbabUrl = yljxscxkbabUrl;
    }

    @JsonIgnore
    public List<String> getYljxscxkzUrl() {
        return YljxscxkzUrl;
    }

    public void setYljxscxkzUrl(List<String> yljxscxkzUrl) {
        YljxscxkzUrl = yljxscxkzUrl;
    }

    @JsonIgnore
    public List<String> getYpzcpjUrl() {
        return YpzcpjUrl;
    }

    public void setYpzcpjUrl(List<String> ypzcpjUrl) {
        YpzcpjUrl = ypzcpjUrl;
    }

    @JsonIgnore
    public List<String> getYpzcpjNextUrl() {
        return YpzcpjNextUrl;
    }

    public void setYpzcpjNextUrl(List<String> ypzcpjNextUrl) {
        YpzcpjNextUrl = ypzcpjNextUrl;
    }

    @JsonIgnore
    public List<String> getYpbcpjUrl() {
        return YpbcpjUrl;
    }

    public void setYpbcpjUrl(List<String> ypbcpjUrl) {
        YpbcpjUrl = ypbcpjUrl;
    }

    @JsonIgnore
    public List<String> getXyzsUrl() {
        return XyzsUrl;
    }

    public void setXyzsUrl(List<String> xyzsUrl) {
        XyzsUrl = xyzsUrl;
    }

    @JsonIgnore
    public List<String> getCpzlbzUrl() {
        return CpzlbzUrl;
    }

    public void setCpzlbzUrl(List<String> cpzlbzUrl) {
        CpzlbzUrl = cpzlbzUrl;
    }

    @JsonIgnore
    public List<String> getZlzsUrl() {
        return ZlzsUrl;
    }

    public void setZlzsUrl(List<String> zlzsUrl) {
        ZlzsUrl = zlzsUrl;
    }

    @JsonIgnore
    public List<String> getJkypzczUrl() {
        return JkypzczUrl;
    }

    public void setJkypzczUrl(List<String> jkypzczUrl) {
        JkypzczUrl = jkypzczUrl;
    }

    @JsonIgnore
    public List<String> getPriceApprovalUrl() {
        return PriceApprovalUrl;
    }

    public void setPriceApprovalUrl(List<String> priceApprovalUrl) {
        PriceApprovalUrl = priceApprovalUrl;
    }

    @JsonIgnore
    public List<String> getYpjybgUrl() {
        return YpjybgUrl;
    }

    public void setYpjybgUrl(List<String> ypjybgUrl) {
        YpjybgUrl = ypjybgUrl;
    }

    @JsonIgnore
    public List<String> getBoxUrl() {
        return BoxUrl;
    }

    public void setBoxUrl(List<String> boxUrl) {
        BoxUrl = boxUrl;
    }

    @JsonIgnore
    public List<String> getLabelUrl() {
        return LabelUrl;
    }

    public void setLabelUrl(List<String> labelUrl) {
        LabelUrl = labelUrl;
    }

    @JsonIgnore
    public List<String> getDescriptionBookUrl() {
        return DescriptionBookUrl;
    }

    public void setDescriptionBookUrl(List<String> descriptionBookUrl) {
        DescriptionBookUrl = descriptionBookUrl;
    }

    @JsonIgnore
    public List<String> getYpjgjgzdbaUrl() {
        return YpjgjgzdbaUrl;
    }

    public void setYpjgjgzdbaUrl(List<String> ypjgjgzdbaUrl) {
        YpjgjgzdbaUrl = ypjgjgzdbaUrl;
    }
}
