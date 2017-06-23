package com.oxchains.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.Token;
import com.oxchains.bean.model.ziyun.User;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.DBHelper;
import com.oxchains.util.Md5Utils;
import com.oxchains.util.TokenUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService extends BaseService {
	
	@Resource
	private ChaincodeService chaincodeService;

	public RespDTO<String> addUser(String body){
		User user = new User();
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			user.setUsername(obj.get("username").getAsString());
			user.setPassword(obj.get("password").getAsString());
			user.setRealname(obj.get("realname").getAsString());
		}catch(JsonSyntaxException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		String username = user.getUsername();
		String password = user.getPassword();
		String realname = user.getRealname();
		Connection conn = DBHelper.openCon();
		PreparedStatement pst = null;
		try{
			conn.setAutoCommit(false);
			log.debug(username+"="+password+"="+realname);
			String sql = "";  
			pst = conn.prepareStatement("select count(1) from tab_user where username = ?");
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)>0){
					return RespDTO.fail("操作失败",ConstantsData.RTN_DATA_ALREADY_EXISTS);
				}
			}
			
			sql = "insert into tab_user (username,password,realname,status) values (?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2, Md5Utils.getMD5(password));
			pst.setString(3, realname);
			pst.setString(4, "1");//1=正常;2=审核中;3=冻结
			log.debug("===sql==="+sql);
			int count = pst.executeUpdate();
			if(count>0){
				pst = conn.prepareStatement("select id from tab_user where username = ?");
				pst.setString(1, username);
				rs = pst.executeQuery();
				if(rs.next()){
					user.setId(rs.getString(1));
				}
				
				String txID = chaincodeService.invoke("new", new String[]{username});
				log.debug("===txID==="+txID);
				if(txID == null){
					conn.rollback();
					return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
				}
				conn.commit();
				return RespDTO.success("操作成功", gson.toJson(user));
			}
		}
		catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		finally{
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
		return RespDTO.success("操作成功", gson.toJson(user));
	}
	
	public RespDTO<String> login(String body){
		User user = new User();
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			user.setUsername(obj.get("username").getAsString());
			user.setPassword(obj.get("password").getAsString());
		}catch(JsonSyntaxException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		String username = user.getUsername();
		String password = user.getPassword();
		Connection conn = DBHelper.openCon();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			log.debug(username+"="+password);
			String sql = "";  
			conn.setAutoCommit(false);
			//verify username and password
			String md5pwd = Md5Utils.getMD5(password);
			sql = "select password from tab_user where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			rs = pst.executeQuery();
			if(rs.next()){
				String dbpwd = rs.getString("password");
				log.debug("===dbpwd==="+dbpwd);
				if(!md5pwd.equals(dbpwd)){
					return RespDTO.fail("操作失败",ConstantsData.RTN_USERNAMEORPWD_ERROR);
				} 
			}
			else{
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNREGISTER);
			}
			
			//generate token
			String token = TokenUtils.createToken(username);
			//check token exists
			sql = "select count(1) from tab_token where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			rs = pst.executeQuery();
			if(rs.next() && rs.getInt(1)>0){
				//token already exists, update token
				sql = "update tab_token set token = ? where username = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, token);
				pst.setString(2, username);
				//FIXME check expire or firstly ??? then update ??? 
				if(pst.executeUpdate() > 0){
					log.debug("===update user token successfully===");
				}
			}
			else{
				//insert into token
				sql = "insert into tab_token (username,token) values (?,?)";
				pst = conn.prepareStatement(sql);
				pst.setString(1, username);
				pst.setString(2, token);
				if(pst.executeUpdate() > 0 ){
					log.debug("===insert user token successfully===");
				}
			}
			
			//add login log
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); 
	    	String clientIp = request.getRemoteAddr();
	    	log.debug("====clientIp==="+clientIp);
	    	sql = "insert into tab_log (username, loginip, logintime, token) values (?,?,?,?)";
	    	pst = conn.prepareStatement(sql);
	    	pst.setString(1, username);
	    	pst.setString(2, clientIp);
	    	pst.setTimestamp(3, getCurrentTimeStamp());
	    	pst.setString(4, token);
			pst.executeUpdate();
			conn.commit();
			
			Token tokenEn = new Token();
			tokenEn.setToken(token);
			tokenEn.setExpiresIn(ConstantsData.TOKEN_EXPIRES);
			
			return RespDTO.success("操作成功", gson.toJson(tokenEn));
		}
		catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
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
	
	public RespDTO<String> logout(String body){
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try{
			conn = DBHelper.openCon();
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			String username = obj.get("username").getAsString();
			String token = obj.get("token").getAsString();
			
			conn.setAutoCommit(false);
			
			//compare token with db
			String sql = "select token from tab_token where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			rs = pst.executeQuery();
			if(rs.next()){
				if(!token.equals(rs.getString("token"))){
					return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
				}
			}
			else{
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
			}
			
			//verify token
			//FIXME add other verify ???
			JwtToken jwt = TokenUtils.parseToken(token);
			if(!username.equals(jwt.getId())){
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
			}
			
			//delete token
			sql = "delete from tab_token where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.executeUpdate();
			
			//update logouttime
	    	sql = "update tab_log set logouttime = ? where token = ?";
	    	pst = conn.prepareStatement(sql);
	    	pst.setTimestamp(1, getCurrentTimeStamp());
	    	pst.setString(2, token);
			pst.executeUpdate();
			conn.commit();
		}catch(JsonSyntaxException |NullPointerException e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
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
		return RespDTO.success("操作成功", null);
	}
	
	public RespDTO<String> allow(String body){
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			String username = obj.get("username").getAsString();
			String token = obj.get("token").getAsString();
			JwtToken jwt = TokenUtils.parseToken(token);
			Date expire = jwt.getExpiratioin();
			String authUser = jwt.getId();
			Date now = new Date();
			if(expire.before(now)){//expired
				return RespDTO.fail("操作失败",ConstantsData.RTN_LOGIN_EXPIRED);
			}
			//unlogin
			Connection conn = DBHelper.openCon();
			String sql = "select token from tab_token where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, authUser);
			rs = pst.executeQuery();
			if(rs.next()){
				if(!token.equals(rs.getString("token"))){
					return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
				}
			}
			else{
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
			}
			String txId = chaincodeService.invoke("auth", new String[]{authUser,username});
			log.debug("===txID==="+txId);
			if(txId == null){
				return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
			}
		}
		catch(JsonSyntaxException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
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
		return RespDTO.success("操作成功");
	}
	
	public RespDTO<String> revoke(String body){
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			String username = obj.get("username").getAsString();
			String token = obj.get("token").getAsString();
			JwtToken jwt = TokenUtils.parseToken(token);
			Date expire = jwt.getExpiratioin();
			String authUser = jwt.getId();
			Date now = new Date();
			if(expire.before(now)){//expired
				return RespDTO.fail("操作失败",ConstantsData.RTN_LOGIN_EXPIRED);
			}
			//unlogin
			Connection conn = DBHelper.openCon();
			String sql = "select token from tab_token where username = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, authUser);
			rs = pst.executeQuery();
			if(rs.next()){
				if(!token.equals(rs.getString("token"))){
					return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
				}
			}
			else{
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNLOGIN);
			}
			
			String txId = chaincodeService.invoke("revoke", new String[]{authUser,username});
			log.debug("===txID==="+txId);
			if(txId == null){
				return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
			}
		}
		catch(JsonSyntaxException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
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
		return RespDTO.success("操作成功");
	}
	
	private static java.sql.Timestamp getCurrentTimeStamp() {  
		   
	    java.util.Date today = new java.util.Date();  
	    System.out.println(""+today.getTime());
	    return new java.sql.Timestamp(today.getTime());  
	   
	} 
}
