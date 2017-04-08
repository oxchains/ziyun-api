package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * 货物信息
 *
 * @author liuruichao
 * Created on 2017/4/6 12:10
 */
public class Cargo extends BaseEntity {
    @JsonProperty("GoodsType")
    private String GoodsType; //货物类型

    @JsonProperty("DrugElectronicSupervisionCode")
    private String DrugElectronicSupervisionCode; //药品电子监管码

    @JsonProperty("DrugInformation")
    private DrugInformation DrugInformation;

    @JsonProperty("ProduceInformation")
    private ProduceInformation ProduceInformation;
}
