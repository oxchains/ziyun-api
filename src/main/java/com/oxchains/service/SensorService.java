package com.oxchains.service;

import com.oxchains.Application;
import com.oxchains.bean.dto.SensorDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.Sensor;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.dao.TabTokenDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SensorService
 *
 * @author liuruichao Created on 2017/4/6 16:54
 */
@Service
@Slf4j
@Transactional
public class SensorService extends BaseService {
	@Resource
	private ChaincodeService chaincodeService;
	
	@Resource
	private TabTokenDao tabTokenDao;

	public RespDTO<String> add(Sensor sensor) {
		try {
			sensor.setToken(Application.userContext().get().getUsername());// store username ,not token
			String txID = null;
			txID = chaincodeService.invoke("saveSensorData", new String[] { gson.toJson(sensor) });
			log.debug("===txID==="+txID);
			if (txID == null) {
				return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
			}
			return RespDTO.success("操作成功", null);
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
	}

	public RespDTO<List<Sensor>> getSensorData(String number, Long startTime, Long endTime) {
		try {
			String jsonStr = chaincodeService.getPayloadAndTxid("getSensorDataBySensorNum",
					new String[] { number, startTime + "", endTime + "" });
			SensorDTO sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);
			if (sensorDTO.getList() == null || sensorDTO.getList().size() <= 0) {
				jsonStr = chaincodeService.getPayloadAndTxid("getSensorDataByEquipmentNum",
						new String[] { number, startTime + "", endTime + "" });
			}
			if (StringUtils.isEmpty(jsonStr)) {
				return RespDTO.fail("没有数据");
			}
			log.debug("===getSensorData==="+jsonStr);
			String txId = jsonStr.split("!#!")[1];
			jsonStr =  jsonStr.split("!#!")[0];
			sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);

			String username = Application.userContext().get().getUsername();
			for (Iterator<Sensor> it= sensorDTO.getList().iterator(); it.hasNext();) {
				Sensor sensor = it.next();
				sensor.setTxId(txId);
				log.debug("===sensor.getToken()==="+sensor.getToken());
				String jsonAuth = chaincodeService.query("query", new String[] { sensor.getToken() });
				log.debug("===jsonAuth==="+jsonAuth);
				Auth auth = gson.fromJson(jsonAuth, Auth.class);
				ArrayList<String> authList = auth.getAuthList();
				log.debug("===username==="+username);
				if(!authList.contains(username)){
					log.debug("===remove===");
					it.remove();
				}
			}
			if(sensorDTO.getList().isEmpty()){
				return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
			}

			return RespDTO.success(sensorDTO.getList());
		} catch (Exception e) {
			log.error(e.getMessage());
			return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
	}

	public RespDTO<List<Sensor>> getSensorData(String number, Long startTime, Long endTime, int pageIndex) {
		try {
			String jsonStr = chaincodeService.query("getSensorDataBySensorNum",
					new String[] { number, startTime + "", endTime + "" });
			SensorDTO sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);
			if (sensorDTO.getList() == null || sensorDTO.getList().size() <= 0) {
				jsonStr = chaincodeService.query("getSensorDataByEquipmentNum",
						new String[] { number, startTime + "", endTime + "" });
			}
			if (StringUtils.isEmpty(jsonStr)) {
				return RespDTO.fail("没有数据");
			}
			log.debug("===getSensorData==="+jsonStr);
			sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);

			String username = Application.userContext().get().getUsername();
			for (Iterator<Sensor> it= sensorDTO.getList().iterator(); it.hasNext();) {
				Sensor sensor = it.next();
				log.debug("===sensor.getToken()==="+sensor.getToken());
				String jsonAuth = chaincodeService.query("query", new String[] { sensor.getToken() });
				log.debug("===jsonAuth==="+jsonAuth);
				Auth auth = gson.fromJson(jsonAuth, Auth.class);
				ArrayList<String> authList = auth.getAuthList();
				log.debug("===username==="+username);
				if(!authList.contains(username)){
					log.debug("===remove===");
					it.remove();
				}
			}
			if(sensorDTO.getList().isEmpty()){
				return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
			}
			return RespDTO.success(sensorDTO.getList());
		} catch (Exception e) {
			log.error(e.getMessage());
			return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
	}
}
