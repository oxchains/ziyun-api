package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * GoodsTrace
 *
 * @author liuruichao
 * Created on 2017/4/6 16:50
 */
public class GoodsTrace extends BaseEntity {
    @JsonProperty("UniqueID")
    private String UniqueID; //唯一ID

    @JsonProperty("CommodityCode")
    private String CommodityCode; //商品编码

    @JsonProperty("ProductionBatch")
    private String ProductionBatch; //生产批次
}
