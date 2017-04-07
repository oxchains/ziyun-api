package com.oxchains.service;

import com.google.gson.reflect.TypeToken;
import com.oxchains.bean.dto.SensorDTO;
import com.oxchains.common.RespDTO;
import com.oxchains.bean.model.ziyun.Sensor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

    public RespDTO<String> add(Sensor sensor) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String txID = chaincodeService.invoke("saveSensorData", new String[]{gson.toJson(sensor)});
        return RespDTO.success("操作成功", txID);
    }

    public RespDTO<List<Sensor>> getSensorData(String number, Long startTime, Long endTime) throws InvalidArgumentException, ProposalException {
        String jsonStr = chaincodeService.query("getSensorDataBySensorNum", new String[] { number, startTime + "", endTime + ""});
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            jsonStr = chaincodeService.query("getSensorDataByEquipmentNum", new String[] { number, startTime + "", endTime + ""});
        }
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        SensorDTO sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);
        return RespDTO.success(sensorDTO.getList());
    }
}
