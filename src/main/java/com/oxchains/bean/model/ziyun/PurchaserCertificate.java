package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

/**
 * Created by root on 17-8-22.
 */
public class PurchaserCertificate extends BaseEntity {

    @JsonProperty("PurchaserCertificateKey")
    private String PurchaserCertificateKey;

    @JsonProperty("PurchaserCertificateValue")
    private String PurchaserCertificateValue;

}
