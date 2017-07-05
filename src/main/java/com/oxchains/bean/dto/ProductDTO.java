package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.Product;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 产品信息DTO
 * Created by Luo_xuri on 2017/7/5.
 */
@Data
public class ProductDTO extends BaseEntity {

    private List<Product> list;
}
