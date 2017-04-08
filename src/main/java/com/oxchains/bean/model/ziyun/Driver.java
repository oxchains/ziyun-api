package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Driver
 *
 * @author liuruichao
 * Created on 2017/4/6 16:49
 */
public class Driver extends BaseEntity {
    @JsonProperty("QualificationCertificateNumber")
    private String QualificationCertificateNumber; //选填 驾驶员从业资格证号

    @JsonProperty("NameOfPerson")
    private String NameOfPerson; //选填 姓名

    @JsonProperty("TelephoneNumber")
    private String TelephoneNumber; //选填 电话号码
}
