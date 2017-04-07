package com.oxchains.bean.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * GoodsTrace
 *
 * @author liuruichao
 * Created on 2017/4/6 16:50
 */
@Data
public class GoodsTrace extends BaseEntity {
    private String UniqueID; //唯一ID

    private String CommodityCode; //商品编码

    private String ProductionBatch; //生产批次
}
