package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * 食品信息
 */
public class FoodInformation extends BaseEntity {
    @JsonProperty("id")
    private int id;

    @JsonProperty("FoodName")
    private String FoodName; //食品名称

    @JsonProperty("Manufacturer")
    private String Manufacturer; //生产厂家

}
