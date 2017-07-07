package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * Created by root on 17-7-3.
 * 货物
 */
public class Goods extends BaseEntity {
    @JsonProperty("id")
    private String id;

    @JsonProperty("Type")
    private String Type;//food, medicine

    @JsonProperty("GoodsType")
    private String GoodsType;//货物类型

    @JsonProperty("ParentCode")
    private String ParentCode;//父编码

    @JsonProperty("ProduceInfoId")
    private String ProduceInfoId;//生产过程ID

    @JsonProperty("ProductId")
    private String ProductId;//产品ID

    @JsonProperty("ProductCode")
    private String ProductCode;//产品编码

    @JsonProperty("UniqueCode")
    private String UniqueCode;//唯一编码

    @JsonProperty("CommodityCode")
    private String CommodityCode;//商品码

    @JsonProperty("ProductionBatch")
    private String ProductionBatch;//生产批次

    @JsonProperty("DrugInformationList")
    private List<DrugInformation> DrugInformationList;//药品信息

    @JsonProperty("FoodInformationList")
    private List<FoodInformation> FoodInformationList;//食品信息

    @JsonProperty("ProduceInformationList")
    private List<ProduceInformation> ProduceInformationList;//生产信息

}
