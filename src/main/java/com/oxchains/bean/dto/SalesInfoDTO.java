package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.SalesInfo;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Created by root on 17-7-5.
 */
@Data
public class SalesInfoDTO extends BaseEntity {
    private List<SalesInfo> list;
}
