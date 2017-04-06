package com.oxchains.controller;

import com.oxchains.common.RespDTO;
import com.oxchains.model.ziyun.Sensor;
import com.oxchains.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SensorController
 *
 * @author liuruichao
 * Created on 2017/4/6 16:52
 */
@RestController
@RequestMapping("/sensor")
@Slf4j
public class SensorController extends BaseController {
    @Resource
    private SensorService sensorService;

    @RequestMapping(method = RequestMethod.POST)
    public RespDTO<String> add(@RequestBody String body) {
        Sensor sensor = gson.fromJson(body, Sensor.class);
        return sensorService.add(sensor);
    }
}
