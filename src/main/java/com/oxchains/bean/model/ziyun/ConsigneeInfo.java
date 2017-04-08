package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * ConsigneeInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:42
 */
public class ConsigneeInfo extends BaseEntity {
    @JsonProperty("CountrySubdivisionCode")
    private String CountrySubdivisionCode; //收货地点的国家行政区划代码 GB/T 2260

    @JsonProperty("Consignee")
    private String Consignee; //选填 收货人

    @JsonProperty("GoodsReceiptPlace")
    private String GoodsReceiptPlace; //选填 收货地点
}
