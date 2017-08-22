package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-8-22.
 */
public class ProductFactoryInspectionReport extends BaseEntity {

    @JsonProperty("ProductFactoryInspectionReportKey")
    private String ProductFactoryInspectionReportKey;

    @JsonProperty("ProductFactoryInspectionReportValue")
    private String ProductFactoryInspectionReportValue;
}
