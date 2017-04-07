package com.oxchains.bean.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * GoodsInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:47
 */
@Data
public class GoodsInfo extends BaseEntity {
    private String DescriptionOfGoods; //货物名称

    private String CargoTypeClassificationCode; //货物类型分类代码 4.2.5

    private Double GoodsItemGrossWeight; //货物项毛重 (kg)

    private Double Cube; //选填 体积 (立方米)

    private Integer TotalNumberOfPackages; //选填 总件数
}
