package com.oxchains.service;

import com.oxchains.common.RespDTO;
import com.oxchains.model.ziyun.Sensor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SensorService
 *
 * @author liuruichao
 * Created on 2017/4/6 16:54
 */
@Service
@Slf4j
public class SensorService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> add(Sensor sensor) {
        try {
            String txID = chaincodeService.invoke("saveSensorData", new String[]{gson.toJson(sensor)});
            return RespDTO.success("操作成功", txID);
        } catch (Exception e) {
            log.error("addSensor error!", e);
        }
        return RespDTO.fail();
    }
}
