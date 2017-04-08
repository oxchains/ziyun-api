package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * ConsignorInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:41
 */
public class ConsignorInfo extends BaseEntity {
    @JsonProperty("CountrySubdivisionCode")
    private String CountrySubdivisionCode; //装货地点的国家行政区划代码 GB/T 2260

    @JsonProperty("PersonalIdentityDocument")
    private String PersonalIdentityDocument; //选填 个人证件号 如果是个人 必填

    @JsonProperty("Consignor")
    private String Consignor; //选填 发货人 单位或个人
}
