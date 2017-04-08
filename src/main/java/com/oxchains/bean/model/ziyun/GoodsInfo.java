package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * GoodsInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:47
 */
public class GoodsInfo extends BaseEntity {
    @JsonProperty("DescriptionOfGoods")
    private String DescriptionOfGoods; //货物名称

    @JsonProperty("CargoTypeClassificationCode")
    private String CargoTypeClassificationCode; //货物类型分类代码 4.2.5

    @JsonProperty("GoodsItemGrossWeight")
    private Double GoodsItemGrossWeight; //货物项毛重 (kg)

    @JsonProperty("Cube")
    private Double Cube; //选填 体积 (立方米)

    @JsonProperty("TotalNumberOfPackages")
    private Integer TotalNumberOfPackages; //选填 总件数
}
