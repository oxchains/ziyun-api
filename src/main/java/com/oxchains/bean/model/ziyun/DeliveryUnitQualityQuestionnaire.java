package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-8-22.
 */
public class DeliveryUnitQualityQuestionnaire extends BaseEntity{

    @JsonProperty("DeliveryUnitQualityQuestionnaireKey")
    private String DeliveryUnitQualityQuestionnaireKey;

    @JsonProperty("DeliveryUnitQualityQuestionnaireValue")
    private String DeliveryUnitQualityQuestionnaireValue;
}
