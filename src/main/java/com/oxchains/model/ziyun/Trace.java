package com.oxchains.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Trace
 *
 * @author liuruichao
 * Created on 2017/4/6 16:50
 */
@Data
public class Trace extends BaseEntity {
    private String TraceName; //跟踪节点

    private Long TraceTime; //跟踪时间 时间戳
}
