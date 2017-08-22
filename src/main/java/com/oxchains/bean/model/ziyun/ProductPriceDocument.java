package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-8-22.
 */
public class ProductPriceDocument extends BaseEntity {

    @JsonProperty("ProductPriceDocumentKey")
    private String ProductPriceDocumentKey;

    @JsonProperty("ProductPriceDocumentValue")
    private String ProductPriceDocumentValue;
}
