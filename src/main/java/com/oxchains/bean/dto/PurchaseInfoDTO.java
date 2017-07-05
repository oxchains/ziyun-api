package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.PurchaseInfo;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Created by root on 17-7-5.
 */
@Data
public class PurchaseInfoDTO extends BaseEntity {
    private List<PurchaseInfo> list;
}
