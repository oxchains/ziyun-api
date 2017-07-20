package com.oxchains.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.oxchains.common.ChaincodeResp;
import com.oxchains.dao.ChaincodeData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oxchains.bean.dto.SensorDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.Sensor;
import com.oxchains.bean.model.ziyun.TabToken;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.dao.TabTokenDao;
import com.oxchains.util.TokenUtils;

import lombok.extern.slf4j.Slf4j;

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
	private ChaincodeData chaincodeData;
	
	@Resource
	private TabTokenDao tabTokenDao;

	public RespDTO<String> add(Sensor sensor) {
		try {
			String token = sensor.getToken();
			JwtToken jwt = TokenUtils.parseToken(token);
			sensor.setToken(jwt.getId());// store username ,not token
			String txID = null;
			txID = chaincodeData.invoke("saveSensorData", new String[] { gson.toJson(sensor) })
			.filter(ChaincodeResp::succeeded)
			.map(ChaincodeResp::getPayload)
			.orElse(null);
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

	public RespDTO<List<Sensor>> getSensorData(String number, Long startTime, Long endTime,String Token) {
		try {
			String jsonStr = chaincodeData.query("getSensorDataBySensorNum",
					new String[] { number, startTime + "", endTime + "" })
					.filter(ChaincodeResp::succeeded).map(ChaincodeResp::getPayload)
					.orElse(null);
			SensorDTO sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);
			if (sensorDTO.getList() == null || sensorDTO.getList().size() <= 0) {
				jsonStr = chaincodeData.query("getSensorDataByEquipmentNum",
						new String[] { number, startTime + "", endTime + "" })
				.filter(ChaincodeResp::succeeded)
				.map(ChaincodeResp::getPayload)
				.orElse(null);
			}
			if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
				return RespDTO.fail("没有数据");
			}
			sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);

			JwtToken jwt = TokenUtils.parseToken(Token);
			String username = jwt.getId();
			for (Iterator<Sensor> it= sensorDTO.getList().iterator(); it.hasNext();) {
				Sensor sensor = it.next();
				log.debug("===sensor.getToken()==="+sensor.getToken());
				String jsonAuth = chaincodeData.query("query", new String[] { sensor.getToken() })
						.filter(ChaincodeResp::succeeded)
						.map(ChaincodeResp::getPayload)
						.orElse(null);
				if (StringUtils.isEmpty(jsonAuth) || "null".equals(jsonAuth)) {
					return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
				}
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

	public RespDTO<List<Sensor>> getSensorData(String number, Long startTime, Long endTime, int pageIndex,
			String Token) {
		try {
			JwtToken jwt = TokenUtils.parseToken(Token);

			String jsonStr = chaincodeData.query("getSensorDataBySensorNum",
					new String[] { number, startTime + "", endTime + "" })
					.filter(ChaincodeResp::succeeded)
					.map(ChaincodeResp::getPayload)
					.orElse(null);
			SensorDTO sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);
			if (sensorDTO.getList() == null || sensorDTO.getList().size() <= 0) {
				jsonStr = chaincodeData.query("getSensorDataByEquipmentNum",
						new String[] { number, startTime + "", endTime + "" })
				.filter(ChaincodeResp::succeeded)
				.map(ChaincodeResp::getPayload)
				.orElse(null);
			}
			if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
				return RespDTO.fail("没有数据");
			}
			log.debug("===getSensorData==="+jsonStr);
			sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);

			String username = jwt.getId();
			for (Iterator<Sensor> it= sensorDTO.getList().iterator(); it.hasNext();) {
				Sensor sensor = it.next();
				log.debug("===sensor.getToken()==="+sensor.getToken());
				String jsonAuth = chaincodeData.query("query", new String[] { sensor.getToken() })
						.filter(ChaincodeResp::succeeded)
						.map(ChaincodeResp::getPayload)
						.orElse(null);
				if (StringUtils.isEmpty(jsonAuth) || "null".equals(jsonAuth)) {
					return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
				}
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
