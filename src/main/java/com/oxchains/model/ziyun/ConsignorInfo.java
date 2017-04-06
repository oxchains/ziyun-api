package com.oxchains.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * ConsignorInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:41
 */
@Data
public class ConsignorInfo extends BaseEntity {
    private String CountrySubdivisionCode; //装货地点的国家行政区划代码 GB/T 2260

    private String PersonalIdentityDocument; //选填 个人证件号 如果是个人 必填

    private String Consignor; //选填 发货人 单位或个人
}
