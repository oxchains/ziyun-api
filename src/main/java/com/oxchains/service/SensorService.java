package com.oxchains.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oxchains.bean.dto.SensorDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.Sensor;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.DBHelper;
import com.oxchains.util.TokenUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * SensorService
 *
 * @author liuruichao Created on 2017/4/6 16:54
 */
@Service
@Slf4j
public class SensorService extends BaseService {
	@Resource
	private ChaincodeService chaincodeService;

	public RespDTO<String> add(Sensor sensor) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String token = sensor.getToken();
			JwtToken jwt = TokenUtils.parseToken(token);
			Date expire = jwt.getExpiratioin();
			Date now = new Date();
			if (expire.before(now)) {// expired
				return RespDTO.fail("操作失败", ConstantsData.RTN_LOGIN_EXPIRED);
			}
			//unlogin
			Connection conn = DBHelper.openCon();
			String sql = "select token from tab_token where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, jwt.getId());
			rs = pst.executeQuery();
			if(rs.next()){
				if(!token.equals(rs.getString("token"))){
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
		} catch (InvalidArgumentException | ProposalException | InterruptedException | ExecutionException
				| TimeoutException e) {
			log.error(e.getMessage());
			return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		catch(SQLException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		finally{
			if(rs != null){
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					log.error(e.getMessage());
					return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
				}
			}
			if(pst!=null){
				try {
					pst.close();
					pst = null;
				} catch (SQLException e) {
					log.error(e.getMessage());
					return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
				}
			}
			DBHelper.close();
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
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			JwtToken jwt = TokenUtils.parseToken(token);
			Date expire = jwt.getExpiratioin();
			Date now = new Date();
			if (expire.before(now)) {// expired
				return RespDTO.fail("操作失败", ConstantsData.RTN_LOGIN_EXPIRED);
			}
			//unlogin
			Connection conn = DBHelper.openCon();
			String sql = "select token from tab_token where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, jwt.getId());
			rs = pst.executeQuery();
			if(rs.next()){
				if(!token.equals(rs.getString("token"))){
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
		finally{
			if(rs != null){
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					log.error(e.getMessage());
					return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
				}
			}
			if(pst!=null){
				try {
					pst.close();
					pst = null;
				} catch (SQLException e) {
					log.error(e.getMessage());
					return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
				}
			}
			DBHelper.close();
		}
	}
}
