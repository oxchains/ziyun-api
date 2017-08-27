package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by root on 17-8-22.
 */
public class ProductProvincialPnspectionReport extends BaseEntity {
    @JsonProperty("ProductProvincialPnspectionReportKey")
    private String ProductProvincialPnspectionReportKey;

    @JsonProperty("ProductProvincialPnspectionReportValue")
    private String ProductProvincialPnspectionReportValue;
    @JsonIgnore
    public String getProductProvincialPnspectionReportKey() {
        return ProductProvincialPnspectionReportKey;
    }

    public void setProductProvincialPnspectionReportKey(String productProvincialPnspectionReportKey) {
        ProductProvincialPnspectionReportKey = productProvincialPnspectionReportKey;
    }
    @JsonIgnore
    public String getProductProvincialPnspectionReportValue() {
        return ProductProvincialPnspectionReportValue;
    }

    public void setProductProvincialPnspectionReportValue(String productProvincialPnspectionReportValue) {
        ProductProvincialPnspectionReportValue = productProvincialPnspectionReportValue;
    }
}
