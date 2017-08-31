package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.TabUser;
import com.oxchains.common.RespDTO;
import com.oxchains.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.google.common.net.HttpHeaders.CONTENT_DISPOSITION;
import static java.net.URLConnection.guessContentTypeFromName;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@RestController
@RequestMapping("/user")
@Slf4j
@EnableJpaRepositories
public class UserController extends BaseController{
	@Resource
	private UserService userService;

    @Value("${file.upload.dir}")
    private String upload;
	
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
	 public RespDTO<String> logout() {
        try {
            return userService.logout();
        } catch (Exception e) {
            log.error("user logout error!", e);
        }
        return RespDTO.fail();
	 }
	 
	 @RequestMapping(value = "/auth/allow", method = RequestMethod.POST)
	 public RespDTO<String> allow(@RequestBody String body) {
        try {
        	System.out.println("body==="+body);
            return userService.allow(body);
        } catch (Exception e) {
            log.error("user auth allow error!", e);
        }
        return RespDTO.fail();
	 }
	 
	 @RequestMapping(value = "/auth/revoke", method = RequestMethod.POST)
	 public RespDTO<String> revoke(@RequestBody String body) {
        try {
        	System.out.println("body==="+body);
            return userService.revoke(body);
        } catch (Exception e) {
            log.error("user auth revoke error!", e);
        }
        return RespDTO.fail();
	 }

    @RequestMapping(value = "/auth/query", method = RequestMethod.GET)
    public RespDTO<String> query() {
        try {
            return userService.query();
        } catch (Exception e) {
            log.error("user auth allow error!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/queryuser", method = RequestMethod.GET)
    public RespDTO<List<TabUser>> queryuser() {
        try {
            return userService.queryuser();
        } catch (Exception e) {
            log.error("user auth allow error!", e);
        }
        return RespDTO.fail();
    }


    @GetMapping("/{uuid}/downloadfile")
    public void downloadFileByFilename(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response) {
        File applicationFile = new File(upload + "/" + uuid);
        if (applicationFile.exists()) {
            try {
                Path filePath = applicationFile.toPath();
                response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + applicationFile.getName());
                response.setContentType(guessContentTypeFromName(applicationFile.getName()));
                response.setContentLengthLong(applicationFile.length());
                System.out.println("length==="+applicationFile.length());
                Files.copy(filePath, response.getOutputStream());
            } catch (Exception e) {
                log.warn("failed to downloadfile {}: {}", uuid, e.getMessage());
            }
        } else {
            fileNotFound(response);
        }
    }
    private void fileNotFound(HttpServletResponse response) {
        try {
            response.setStatus(SC_NOT_FOUND);
            response.getWriter().write("file not found");
        } catch (Exception e) {
            log.warn("failed to write response");
        }
    }
}
