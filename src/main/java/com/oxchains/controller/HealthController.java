package com.oxchains.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/healthz")
@Slf4j
public class HealthController extends BaseController{
	
    @RequestMapping(method = RequestMethod.GET)
    public String test() {
        try {
        	log.debug("===healthz===ok");
            return "OK";
        } catch (Exception e) {
            log.error("healthz error!", e);
        }
        return "success";
    }
}
