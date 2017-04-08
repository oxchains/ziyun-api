package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Trace
 *
 * @author liuruichao
 * Created on 2017/4/6 16:50
 */
public class Trace extends BaseEntity {
    @JsonProperty("TraceName")
    private String TraceName; //跟踪节点

    @JsonProperty("TraceTime")
    private Long TraceTime; //跟踪时间 时间戳
}
