package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-7-3.
 * 生产信息
 */
public class ProduceInfo extends BaseEntity {
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("ProductionProcessName")
    private String ProductionProcessName;//产品名称

    @JsonProperty("GoodsCount")
    private int GoodsCount;//生产数量

    @JsonProperty("LastCount")
    private int LastCount;//剩余数量

    @JsonProperty("ProductId")
    private String ProductId;//产品id

    @JsonProperty("EnterpriseId")
    private String EnterpriseId;//生产企业id

    @JsonProperty("ProductionTime")
    private long ProductionTime;//生产时间 时间戳

    @JsonProperty("InStorageTime")
    private long InStorageTime;//入库时间 时间戳

    @JsonProperty("OutStorageTime")
    private long OutStorageTime;//出库时间 时间戳

    @JsonProperty("EnvironmentalMonitoring")
    private String EnvironmentalMonitoring;//环境监测

    @JsonProperty("ProductionParameters")
    private String ProductionParameters;//生产过程工艺参数

    @JsonProperty("QualitySafety")
    private String QualitySafety;//质量安全

    @JsonProperty("BatchNumber")
    private String BatchNumber;//产品检验批号

    @JsonProperty("CheckDate")
    private long CheckDate;//检验日期 时间戳

    @JsonProperty("CheckWay")
    private String CheckWay;//产品检验方法

    @JsonProperty("CheckResult")
    private String CheckResult;//产品检验结果

    @JsonProperty("InspectorName")
    private String InspectorName;//检验人名字

    @JsonProperty("Token")
    private String Token;//token

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

}
