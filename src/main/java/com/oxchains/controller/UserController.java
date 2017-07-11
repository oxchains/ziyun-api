package com.oxchains.controller;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import com.oxchains.common.RespDTO;
import com.oxchains.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
@EnableJpaRepositories
public class UserController extends BaseController{
	@Resource
	private UserService userService;
	
	 @RequestMapping(value = "/register", method = RequestMethod.POST)
	 public RespDTO<String> register(@RequestBody String body ) {
        try {
        	System.out.println("body==="+body);
            return userService.addUser(body);
        } catch (Exception e) {
            log.error("user register error!", e);
        }
        return RespDTO.fail();
	 }
	 
	 @RequestMapping(value = "/login", method = RequestMethod.POST)
	 public RespDTO<String> login(@RequestBody String body) {
        try {
        	System.out.println("body==="+body);
            return userService.login(body);
        } catch (Exception e) {
            log.error("user login error!", e);
        }
        return RespDTO.fail();
	 }
	 
	 @RequestMapping(value = "/logout", method = RequestMethod.POST)
	 public RespDTO<String> logout(@RequestBody String body,@RequestParam String Token) {
        try {
        	System.out.println("body==="+body);
            return userService.logout(body,Token);
        } catch (Exception e) {
            log.error("user logout error!", e);
        }
        return RespDTO.fail();
	 }
	 
	 @RequestMapping(value = "/auth/allow", method = RequestMethod.POST)
	 public RespDTO<String> allow(@RequestBody String body,@RequestParam String Token) {
        try {
        	System.out.println("body==="+body);
            return userService.allow(body,Token);
        } catch (Exception e) {
            log.error("user auth allow error!", e);
        }
        return RespDTO.fail();
	 }
	 
	 @RequestMapping(value = "/auth/revoke", method = RequestMethod.POST)
	 public RespDTO<String> revoke(@RequestBody String body,@RequestParam String Token) {
        try {
        	System.out.println("body==="+body);
            return userService.revoke(body,Token);
        } catch (Exception e) {
            log.error("user auth revoke error!", e);
        }
        return RespDTO.fail();
	 }
	 
	
}
