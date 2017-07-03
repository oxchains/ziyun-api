package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by root on 17-7-3.
 * 货运单
 */
public class TransportBill extends BaseEntity {
    @JsonProperty("id")
    private long id; // 用户唯一ID

    @JsonProperty("PermitNumber")
    private String PermitNumber;//无车承运人的道路运输经营许可证编号

    @JsonProperty("UnifiedSocialCreditIdentifier")
    private String UnifiedSocialCreditIdentifier;//无车承运人的统一社会信用代码

    @JsonProperty("Carrier")
    private String Carrier;//无车承运试点企业名称

    @JsonProperty("BusinessTypeCode")
    private String BusinessTypeCode;//业务类型代码

    @JsonProperty("OriginalDocumentNumber")
    private String OriginalDocumentNumber;//上游企业委托人运输单号

    @JsonProperty("ShippingNoteNumber")
    private String ShippingNoteNumber;//本电子路单号

    @JsonProperty("ConsignmentDateTime")
    private long ConsignmentDateTime;//无车承运人系统正式生成运单的日期 时间戳

    @JsonProperty("DespatchActualDateTime")
    private long DespatchActualDateTime;//货物装车后的发车时间 时间戳

    @JsonProperty("GoodsReceiptDateTime")
    private long GoodsReceiptDateTime;//货物运到后签收时间 时间戳

    @JsonProperty("FreeText")
    private String FreeText;//自由文本

    @JsonProperty("ConsignorInfo")
    private ConsignorInfo ConsignorInfo;//发货方信息

    @JsonProperty("ConsigneeInfo")
    private ConsigneeInfo ConsigneeInfo;//收货方信息

    @JsonProperty("PriceInfo")
    private PriceInfo PriceInfo;//	费用信息

    @JsonProperty("VehicleInfo")
    private VehicleInfo VehicleInfo;//	车辆信息节点

    @JsonProperty("LogisticsTraceList")
    private List<Trace> LogisticsTraceList;//物流追溯信息，数组形式

    @JsonProperty("GoodsTraceList")
    private List<GoodsTrace> GoodsTraceList;//货物追溯信息，数组形式

}
