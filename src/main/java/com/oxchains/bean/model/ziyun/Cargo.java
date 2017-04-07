package com.oxchains.bean.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * 货物信息
 *
 * @author liuruichao
 * Created on 2017/4/6 12:10
 */
@Data
public class Cargo extends BaseEntity {
    private String GoodsType; //货物类型

    private String DrugElectronicSupervisionCode; //药品电子监管码

    private DrugInformation DrugInformation;

    private ProduceInformation ProduceInformation;
}
