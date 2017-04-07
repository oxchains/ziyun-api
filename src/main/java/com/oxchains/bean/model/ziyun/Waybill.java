package com.oxchains.bean.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Waybill
 *
 * @author liuruichao
 * Created on 2017/4/6 16:38
 */
@Data
public class Waybill extends BaseEntity {
    private String PermitNumber; //选填 无车承运人的道路运输经营许可证编号

    private String UnifiedSocialCreditIdentifier; //选填 无车承运人的统一社会信用代码

    private String Carrier; //承运人 无车承运试点企业名称 固定

    private String BusinessTypeCode; //业务类型代码 4.2.2

    private String OriginalDocumentNumber; //原始单号 上游企业委托人运输单号

    private String ShippingNoteNumber; //托运单号 本电子路单号

    private Long ConsignmentDateTime; //无车承运人系统正式生成运单的日期 时间戳

    private Long DespatchActualDateTime; //货物装车后的发车时间 时间戳

    private Long GoodsReceiptDateTime; //货物运到后签收时间 如签收时间无法确定,填货物运到收货地的时间 时间戳

    private String FreeText; //选填 自由文本

    private ConsignorInfo ConsignorInfo; // 发货方信息

    private ConsigneeInfo consigneeInfo; // 收货方信息

    private PriceInfo priceInfo; // 费用信息

    private VehicleInfo vehicleInfo; //车辆信息节点

    private List<Trace> LogisticsTraceList; // 物流追溯信息

    private List<GoodsTrace> GoodsTraceList; // 货物追溯信息
}
