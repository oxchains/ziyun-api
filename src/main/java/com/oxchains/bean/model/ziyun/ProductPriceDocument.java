package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by root on 17-8-22.
 */
public class ProductPriceDocument extends BaseEntity {

    @JsonProperty("ProductPriceDocumentKey")
    private String ProductPriceDocumentKey;

    @JsonProperty("ProductPriceDocumentValue")
    private String ProductPriceDocumentValue;

    @JsonIgnore
    public String getProductPriceDocumentKey() {
        return ProductPriceDocumentKey;
    }
    @JsonIgnore
    public String getProductPriceDocumentValue() {
        return ProductPriceDocumentValue;
    }

    public void setProductPriceDocumentKey(String productPriceDocumentKey) {
        ProductPriceDocumentKey = productPriceDocumentKey;
    }

    public void setProductPriceDocumentValue(String productPriceDocumentValue) {
        ProductPriceDocumentValue = productPriceDocumentValue;
    }
}
