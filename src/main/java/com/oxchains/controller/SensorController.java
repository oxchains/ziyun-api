package com.oxchains.controller;

import com.oxchains.common.RespDTO;
import com.oxchains.bean.model.ziyun.Sensor;
import com.oxchains.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * SensorController
 *
 * @author liuruichao Created on 2017/4/6 16:52
 */
@RestController
@RequestMapping("/sensor")
@Slf4j
public class SensorController extends BaseController {
	@Resource
	private SensorService sensorService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public RespDTO<String> add(@RequestBody String body) {
		try {
			log.debug("===body==="+body);
			Sensor sensor = gson.fromJson(body, Sensor.class);
			return sensorService.add(sensor);
		} catch (Exception e) {
			log.error("add sensor error!", e);
		}
		return RespDTO.fail();
	}

	@RequestMapping(value = "/{number}/{starttime}/{endtime}/{pageindex}", method = RequestMethod.GET)
	public RespDTO<List<Sensor>> querySensorBySnumOrEnum(@PathVariable String number, @PathVariable long starttime,
			@PathVariable long endtime, @PathVariable int pageindex, @RequestParam String token) {
		try {
			return sensorService.getSensorData(number, starttime, endtime, pageindex, token);
		} catch (Exception e) {
			log.error("getSensorDataBySensorNum error!", e);
		}
		return RespDTO.fail();
	}

	@RequestMapping(value = "/sensorNumOrEquipmentNum", method = RequestMethod.GET)
	public RespDTO<List<Sensor>> listSensorNumOrEquipmentNum(@RequestParam String number, @RequestParam Long startTime,
			@RequestParam Long endTime) {
		try {
			return sensorService.getSensorData(number, startTime, endTime);
		} catch (Exception e) {
			log.error("getSensorDataBySensorNum error!", e);
		}
		return RespDTO.fail();
	}
}
