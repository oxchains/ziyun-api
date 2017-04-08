package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * PriceInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:43
 */
public class PriceInfo extends BaseEntity {
    @JsonProperty("TotalMonetaryAmount")
    private Float TotalMonetaryAmount; //货币总金额

    @JsonProperty("Remark")
    private String Remark; //选填 备注
}
