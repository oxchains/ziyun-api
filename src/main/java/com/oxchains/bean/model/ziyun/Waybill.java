package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Waybill
 *
 * @author liuruichao
 * Created on 2017/4/6 16:38
 */
public class Waybill extends BaseEntity {
    @JsonProperty("PermitNumber")
    private String PermitNumber; //选填 无车承运人的道路运输经营许可证编号

    @JsonProperty("UnifiedSocialCreditIdentifier")
    private String UnifiedSocialCreditIdentifier; //选填 无车承运人的统一社会信用代码

    @JsonProperty("Carrier")
    private String Carrier; //承运人 无车承运试点企业名称 固定

    @JsonProperty("BusinessTypeCode")
    private String BusinessTypeCode; //业务类型代码 4.2.2

    @JsonProperty("OriginalDocumentNumber")
    private String OriginalDocumentNumber; //原始单号 上游企业委托人运输单号

    @JsonProperty("ShippingNoteNumber")
    private String ShippingNoteNumber; //托运单号 本电子路单号

    @JsonProperty("ConsignmentDateTime")
    private Long ConsignmentDateTime; //无车承运人系统正式生成运单的日期 时间戳

    @JsonProperty("DespatchActualDateTime")
    private Long DespatchActualDateTime; //货物装车后的发车时间 时间戳

    @JsonProperty("GoodsReceiptDateTime")
    private Long GoodsReceiptDateTime; //货物运到后签收时间 如签收时间无法确定,填货物运到收货地的时间 时间戳

    @JsonProperty("FreeText")
    private String FreeText; //选填 自由文本

    @JsonProperty("ConsignorInfo")
    private ConsignorInfo ConsignorInfo; // 发货方信息

    @JsonProperty("ConsigneeInfo")
    private ConsigneeInfo ConsigneeInfo; // 收货方信息

    @JsonProperty("PriceInfo")
    private PriceInfo PriceInfo; // 费用信息

    @JsonProperty("VehicleInfo")
    private VehicleInfo VehicleInfo; //车辆信息节点

    @JsonProperty("LogisticsTraceList")
    private List<Trace> LogisticsTraceList; // 物流追溯信息

    @JsonProperty("GoodsTraceList")
    private List<GoodsTrace> GoodsTraceList; // 货物追溯信息
}
