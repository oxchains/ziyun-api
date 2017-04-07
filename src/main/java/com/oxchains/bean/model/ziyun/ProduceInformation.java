package com.oxchains.bean.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * ProduceInformation
 *
 * @author liuruichao
 * Created on 2017/4/6 13:38
 */
@Data
public class ProduceInformation extends BaseEntity {
    private String Address; //生产地址

    private String ProductionBatch; //生产批次

    private Long ProductionTime; //生产时间时间戳

    private Long ValidDate; //有效期时间戳
}
