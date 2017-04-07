package com.oxchains.bean.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * ConsigneeInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:42
 */
@Data
public class ConsigneeInfo extends BaseEntity {
    private String CountrySubdivisionCode; //收货地点的国家行政区划代码 GB/T 2260

    private String Consignee; //选填 收货人

    private String GoodsReceiptPlace; //选填 收货地点
}
