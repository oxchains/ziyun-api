package com.oxchains.bean.dto;

import com.oxchains.bean.model.ziyun.Sensor;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * SensorDTO
 *
 * @author liuruichao
 * Created on 2017/4/7 14:21
 */
@Data
public class SensorDTO extends BaseEntity {
    private List<Sensor> list;
}