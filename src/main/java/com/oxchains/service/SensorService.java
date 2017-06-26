package com.oxchains.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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
	private ChaincodeService chaincodeService;
	
	@Resource
	private TabTokenDao tabTokenDao;

	public RespDTO<String> add(Sensor sensor) {
		try {
			String token = sensor.getToken();
			JwtToken jwt = TokenUtils.parseToken(token);
			Date expire = jwt.getExpiratioin();
			Date now = new Date();
			if (expire.before(now)) {// expired
				return RespDTO.fail("操作失败", ConstantsData.RTN_LOGIN_EXPIRED);
			}
			//unlogin
			TabToken tabToken = tabTokenDao.findByUsername(jwt.getId());
			if(tabToken != null){
				if(!token.equals(tabToken.getToken())){
					return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
				}
			}
			else{
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
			}
			
			sensor.setToken(jwt.getId());// store username ,not token
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
			sensorDTO = simpleGson.fromJson(jsonStr, SensorDTO.class);
			return RespDTO.success(sensorDTO.getList());
		} catch (Exception e) {
			log.error(e.getMessage());
			return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
	}

	public RespDTO<List<Sensor>> getSensorData(String number, Long startTime, Long endTime, int pageIndex,
			String token) {
		try {
			JwtToken jwt = TokenUtils.parseToken(token);
			Date expire = jwt.getExpiratioin();
			Date now = new Date();
			if (expire.before(now)) {// expired
				return RespDTO.fail("操作失败", ConstantsData.RTN_LOGIN_EXPIRED);
			}
			//unlogin
			TabToken tabToken = tabTokenDao.findByUsername(jwt.getId());
			if(tabToken != null){
				if(!token.equals(tabToken.getToken())){
					return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
				}
			}
			else{
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
			}
			
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

			String username = jwt.getId();
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
