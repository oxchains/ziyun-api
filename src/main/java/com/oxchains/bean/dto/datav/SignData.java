package com.oxchains.bean.dto.datav;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Created by Luo_xuri on 2017/6/21.
 */
@Data
public class SignData extends BaseEntity {

    @JsonProperty("data_hash")
    private String dataHash; //

    @JsonProperty("signature")
    private String signature; //


}
