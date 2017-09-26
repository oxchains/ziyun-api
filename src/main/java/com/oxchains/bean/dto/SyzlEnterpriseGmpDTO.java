package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.SyzlEnterpriseGmp;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Created by root on 17-8-22.
 */
@Data
public class SyzlEnterpriseGmpDTO extends BaseEntity {
    private List<SyzlEnterpriseGmp> list;
}
