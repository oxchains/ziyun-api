package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.Waybill;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * WaybillDTO
 *
 * @author liuruichao
 * Created on 2017/4/7 14:21
 */
@Data
public class WaybillDTO extends BaseEntity {
    private List<Waybill> list;
}
