package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-8-22.
 */
public class YearTaxReport extends BaseEntity {

    @JsonProperty("YearTaxReportKey")
    private String YearTaxReportKey;

    @JsonProperty("YearTaxReportValue")
    private String YearTaxReportValue;
}
