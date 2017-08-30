package com.oxchains.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.oxchains.Application;
import com.oxchains.auth.JwtService;
import com.oxchains.bean.model.ziyun.TabLog;
import com.oxchains.bean.model.ziyun.TabToken;
import com.oxchains.bean.model.ziyun.TabUser;
import com.oxchains.bean.model.ziyun.Token;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.dao.TabLogDao;
import com.oxchains.dao.TabTokenDao;
import com.oxchains.dao.TabUserDao;
import com.oxchains.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@Transactional
public class UserService extends BaseService {
	
	@Resource
	private ChaincodeService chaincodeService;
	
	@Resource
	private TabUserDao tabUserDao;
	
	@Resource
	private TabTokenDao tabTokenDao;
	
	@Resource
	private TabLogDao tabLogDao;

	@Resource
	private JwtService jwtService;

	public RespDTO<String> addUser(String body){
		TabUser user = new TabUser();
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			user.setUsername(obj.get("Username").getAsString());
			user.setPassword(Md5Utils.getMD5(obj.get("Password").getAsString()));
			user.setRealname(obj.get("Realname").getAsString());
			user.setStatus("1");
			
			String username = user.getUsername();
			String password = user.getPassword();
			String realname = user.getRealname();
			log.debug(username+"="+password+"="+realname);
			TabUser userEntity = tabUserDao.findByUsername(username);
			if(userEntity != null && StringUtils.isNotEmpty(userEntity.getUsername())){
				return RespDTO.fail("操作失败",ConstantsData.RTN_DATA_ALREADY_EXISTS);
			}
			
			user = tabUserDao.save(user);
			
			if(user != null && user.getId()>0){
				log.debug("===userid==="+user.getId());
				String txID = chaincodeService.invoke("add", new String[]{username});
				log.debug("===txID==="+txID);
				if(txID == null){
					return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
				}
				return RespDTO.success("操作成功", gson.toJson(user));
			}
		}catch(JsonSyntaxException e){
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
		return RespDTO.success("操作成功", gson.toJson(user));
	}
	
	public RespDTO<String> login(String body){
		TabUser user = new TabUser();
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			user.setUsername(obj.get("Username").getAsString());
			user.setPassword(obj.get("Password").getAsString());
		}catch(JsonSyntaxException | NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		String username = user.getUsername();
		String password = user.getPassword();
		try{
			log.debug(username+"="+password);
			//verify username and password
			String md5pwd = Md5Utils.getMD5(password);
			TabUser userEntity = tabUserDao.findByUsername(username);
			if(userEntity != null){
				if(!md5pwd.equals(userEntity.getPassword())){
					return RespDTO.fail("操作失败",ConstantsData.RTN_USERNAMEORPWD_ERROR);
				}
			}
			else{
				return RespDTO.fail("操作失败",ConstantsData.RTN_UNREGISTER);
			}
			
			//generate token
			//String token = TokenUtils.createToken(username);
			String token = jwtService.generate(user);

			//check token exists
			TabToken tabtoken = tabTokenDao.findByUsername(username);
			if(tabtoken != null){
				tabtoken.setToken(token);
				tabTokenDao.save(tabtoken);
			}
			else{
				tabtoken = new TabToken();
				tabtoken.setUsername(username);
				tabtoken.setToken(token);
				tabTokenDao.save(tabtoken);
			}
			
			//add login log
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); 
	    	String clientIp = request.getRemoteAddr();
	    	log.debug("====clientIp==="+clientIp);
	    	
	    	TabLog tabLog = new TabLog();
	    	tabLog.setLoginip(clientIp);
	    	tabLog.setLogintime(getCurrentTimeStamp());
	    	tabLog.setToken(token);
	    	tabLog.setUsername(username); 
	    	tabLogDao.save(tabLog);
	    	
			Token tokenEn = new Token();
			tokenEn.setToken(token);
			tokenEn.setExpiresIn(ConstantsData.TOKEN_EXPIRES);
			
			return RespDTO.success("操作成功", gson.toJson(tokenEn));
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
	}
	
	public RespDTO<String> logout(){
		try{
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String authorization = request.getHeader("Authorization");
			if (authorization != null && authorization.startsWith("Bearer ")) {
				jwtService.parse(authorization.replaceAll("Bearer ", "")).ifPresent(
						//update logouttime
						jwtAuthentication -> {
							System.out.println("===jwt==="+jwtAuthentication.getCredentials().toString());
							tabLogDao.updateLogouttime(jwtAuthentication.getCredentials().toString());
						}
				);
			}
		}catch(JsonSyntaxException |NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		return RespDTO.success("操作成功", null);
	}
	
	public RespDTO<String> allow(String body){
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			String username = obj.get("Username").getAsString();

			String authUser = Application.userContext().get().getUsername();

			String txId = chaincodeService.invoke("auth", new String[]{authUser,username});
			log.debug("===txID==="+txId);
			if(txId == null){
				return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
			}
		}
		catch(JsonSyntaxException |NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		return RespDTO.success("操作成功");
	}

	public RespDTO<List<TabUser>> queryuser(){
		List<TabUser> tabUserList = new ArrayList<>();
		try{
			Iterator<TabUser> tabUserIterator = tabUserDao.findAll().iterator();

			while (tabUserIterator.hasNext()){
				tabUserList.add(tabUserIterator.next());
			}
		}
		catch(JsonSyntaxException | NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		return RespDTO.success("操作成功",tabUserList);
	}

	public RespDTO<String> query(){
		String jsonAuth = "";
		try{
			String authUser = Application.userContext().get().getUsername();

			jsonAuth = chaincodeService.query("query", new String[] { authUser });
			log.debug("===jsonAuth==="+jsonAuth);
		}
		catch(JsonSyntaxException | NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		return RespDTO.success("操作成功",jsonAuth);
	}
	
	public RespDTO<String> revoke(String body){
		try{
			JsonObject obj = gson.fromJson(body, JsonObject.class);
			String username = obj.get("Username").getAsString();

			String authUser = Application.userContext().get().getUsername();
			
			String txId = chaincodeService.invoke("revoke", new String[]{authUser,username});
			log.debug("===txID==="+txId);
			if(txId == null){
				return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
			}
		}
		catch(JsonSyntaxException | NullPointerException e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_INVALID_ARGS);
		}
		catch(Exception e){
			log.error(e.getMessage());
			return RespDTO.fail("操作失败",ConstantsData.RTN_SERVER_INTERNAL_ERROR);
		}
		return RespDTO.success("操作成功");
	}
	
	private static java.sql.Timestamp getCurrentTimeStamp() {  
		   
	    java.util.Date today = new java.util.Date();  
	    System.out.println(""+today.getTime());
	    return new java.sql.Timestamp(today.getTime());  
	   
	} 
}
