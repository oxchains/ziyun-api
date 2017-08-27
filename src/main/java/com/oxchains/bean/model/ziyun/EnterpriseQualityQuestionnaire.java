package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by root on 17-8-22.
 */
public class EnterpriseQualityQuestionnaire extends BaseEntity {

    @JsonProperty("EnterpriseQualityQuestionnaireKey")
    private String EnterpriseQualityQuestionnaireKey;

    @JsonProperty("EnterpriseQualityQuestionnaireValue")
    private String EnterpriseQualityQuestionnaireValue;

    @JsonIgnore
    public String getEnterpriseQualityQuestionnaireKey() {
        return EnterpriseQualityQuestionnaireKey;
    }

    public void setEnterpriseQualityQuestionnaireKey(String enterpriseQualityQuestionnaireKey) {
        EnterpriseQualityQuestionnaireKey = enterpriseQualityQuestionnaireKey;
    }

    @JsonIgnore
    public String getEnterpriseQualityQuestionnaireValue() {
        return EnterpriseQualityQuestionnaireValue;
    }

    public void setEnterpriseQualityQuestionnaireValue(String enterpriseQualityQuestionnaireValue) {
        EnterpriseQualityQuestionnaireValue = enterpriseQualityQuestionnaireValue;
    }
}
