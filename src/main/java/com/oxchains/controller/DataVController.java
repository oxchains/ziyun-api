package com.oxchains.controller;

import com.oxchains.service.ChaincodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * DataVController
 *
 * @author liuruichao
 * Created on 2017/4/18 12:36
 */
@RestController
@RequestMapping("/datav")
@Slf4j
public class DataVController extends BaseController {
    @Resource
    private ChaincodeService chaincodeService;

    @RequestMapping(method = RequestMethod.GET)
    public String test() {
        try {
            chaincodeService.queryBlockInfo();
        } catch (Exception e) {
            log.error("test error!", e);
        }
        return "success";
    }
}
