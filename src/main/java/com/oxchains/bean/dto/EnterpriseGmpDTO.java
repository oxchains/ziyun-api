package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.EnterpriseGmp;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Created by root on 17-8-22.
 */
@Data
public class EnterpriseGmpDTO extends BaseEntity {
    private List<EnterpriseGmp> list;
}
