package com.oxchains.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * PriceInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:43
 */
@Data
public class PriceInfo extends BaseEntity {
    private Float TotalMonetaryAmount; //货币总金额
    private String Remark; //选填 备注
}
