package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by root on 17-8-22.
 */
public class PurchaserCertificate extends BaseEntity {

    @JsonProperty("PurchaserCertificateKey")
    private String PurchaserCertificateKey;

    @JsonProperty("PurchaserCertificateValue")
    private String PurchaserCertificateValue;
    @JsonIgnore
    public String getPurchaserCertificateKey() {
        return PurchaserCertificateKey;
    }

    public void setPurchaserCertificateKey(String purchaserCertificateKey) {
        PurchaserCertificateKey = purchaserCertificateKey;
    }
    @JsonIgnore
    public String getPurchaserCertificateValue() {
        return PurchaserCertificateValue;
    }

    public void setPurchaserCertificateValue(String purchaserCertificateValue) {
        PurchaserCertificateValue = purchaserCertificateValue;
    }
}
