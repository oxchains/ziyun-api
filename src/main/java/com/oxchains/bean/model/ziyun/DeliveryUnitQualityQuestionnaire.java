package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by root on 17-8-22.
 */
public class DeliveryUnitQualityQuestionnaire extends BaseEntity{

    @JsonProperty("DeliveryUnitQualityQuestionnaireKey")
    private String DeliveryUnitQualityQuestionnaireKey;

    @JsonProperty("DeliveryUnitQualityQuestionnaireValue")
    private String DeliveryUnitQualityQuestionnaireValue;
    @JsonIgnore
    public String getDeliveryUnitQualityQuestionnaireKey() {
        return DeliveryUnitQualityQuestionnaireKey;
    }

    public void setDeliveryUnitQualityQuestionnaireKey(String deliveryUnitQualityQuestionnaireKey) {
        DeliveryUnitQualityQuestionnaireKey = deliveryUnitQualityQuestionnaireKey;
    }
    @JsonIgnore
    public String getDeliveryUnitQualityQuestionnaireValue() {
        return DeliveryUnitQualityQuestionnaireValue;
    }

    public void setDeliveryUnitQualityQuestionnaireValue(String deliveryUnitQualityQuestionnaireValue) {
        DeliveryUnitQualityQuestionnaireValue = deliveryUnitQualityQuestionnaireValue;
    }
}
