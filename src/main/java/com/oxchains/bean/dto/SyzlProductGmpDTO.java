package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.SyzlProductGmp;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Created by root on 17-9-26.
 */
@Data
public class SyzlProductGmpDTO extends BaseEntity {
    private List<SyzlProductGmp> list;
}
