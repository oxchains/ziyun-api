package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by root on 17-8-22.
 */
public class YearTaxReport extends BaseEntity {

    @JsonProperty("YearTaxReportKey")
    private String YearTaxReportKey;

    @JsonProperty("YearTaxReportValue")
    private String YearTaxReportValue;
    @JsonIgnore
    public String getYearTaxReportKey() {
        return YearTaxReportKey;
    }

    public void setYearTaxReportKey(String yearTaxReportKey) {
        YearTaxReportKey = yearTaxReportKey;
    }
    @JsonIgnore
    public String getYearTaxReportValue() {
        return YearTaxReportValue;
    }

    public void setYearTaxReportValue(String yearTaxReportValue) {
        YearTaxReportValue = yearTaxReportValue;
    }
}
