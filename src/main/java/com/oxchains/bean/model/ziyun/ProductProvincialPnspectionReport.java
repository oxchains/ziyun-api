package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-8-22.
 */
public class ProductProvincialPnspectionReport extends BaseEntity {
    @JsonProperty("ProductProvincialPnspectionReportKey")
    private String ProductProvincialPnspectionReportKey;

    @JsonProperty("ProductProvincialPnspectionReportValue")
    private String ProductProvincialPnspectionReportValue;
}
