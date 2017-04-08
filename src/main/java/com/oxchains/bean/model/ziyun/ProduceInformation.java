package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * ProduceInformation
 *
 * @author liuruichao
 * Created on 2017/4/6 13:38
 */
public class ProduceInformation extends BaseEntity {
    @JsonProperty("Address")
    private String Address; //生产地址

    @JsonProperty("ProductionBatch")
    private String ProductionBatch; //生产批次

    @JsonProperty("ProductionTime")
    private Long ProductionTime; //生产时间时间戳

    @JsonProperty("ValidDate")
    private Long ValidDate; //有效期时间戳
}
