package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-8-22.
 */
public class EnterpriseQualityQuestionnaire extends BaseEntity {

    @JsonProperty("EnterpriseQualityQuestionnaireKey")
    private String EnterpriseQualityQuestionnaireKey;

    @JsonProperty("EnterpriseQualityQuestionnaireValue")
    private String EnterpriseQualityQuestionnaireValue;
}
