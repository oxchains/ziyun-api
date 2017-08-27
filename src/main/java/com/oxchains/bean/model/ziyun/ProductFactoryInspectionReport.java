package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by root on 17-8-22.
 */
public class ProductFactoryInspectionReport extends BaseEntity {

    @JsonProperty("ProductFactoryInspectionReportKey")
    private String ProductFactoryInspectionReportKey;

    @JsonProperty("ProductFactoryInspectionReportValue")
    private String ProductFactoryInspectionReportValue;
    @JsonIgnore
    public String getProductFactoryInspectionReportKey() {
        return ProductFactoryInspectionReportKey;
    }
    @JsonIgnore
    public String getProductFactoryInspectionReportValue() {
        return ProductFactoryInspectionReportValue;
    }

    public void setProductFactoryInspectionReportKey(String productFactoryInspectionReportKey) {
        ProductFactoryInspectionReportKey = productFactoryInspectionReportKey;
    }

    public void setProductFactoryInspectionReportValue(String productFactoryInspectionReportValue) {
        ProductFactoryInspectionReportValue = productFactoryInspectionReportValue;
    }
}
