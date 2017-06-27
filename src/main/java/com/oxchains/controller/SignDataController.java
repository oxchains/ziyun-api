package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.SignData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.SignDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by Luo_xuri on 2017/6/20.
 */
@RestController
@Slf4j
public class SignDataController extends BaseController {

    @Resource
    private SignDataService signDataService;

    @RequestMapping("/sign/install")
    public RespDTO<Boolean> install() {
        try {
            return RespDTO.success(signDataService.instantiateChaincode());
        } catch (Exception e) {
            log.error("install error!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ResponseBody
    public RespDTO<String> getClientSign(@RequestBody SignData signData) {
        try {
            if (StringUtils.isBlank(signData.toString())) {
                return RespDTO.fail("参数错误");
            }
            System.out.println("data_hash: -->" + signData.getDataHash() + "\r\n signature: -->" + signData.getSignature());
            return signDataService.getClientSign(signData);
        } catch (Exception e) {
            log.error("sign error!", e);
        }
        return RespDTO.fail("系统繁忙，请稍后再试！");
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public RespDTO<Boolean> verifySign(@RequestBody SignData signData) {
        try {
            if (StringUtils.isBlank(signData.toString())) {
                return RespDTO.fail("参数错误");
            }
            System.out.println(" data_hash: -->" + signData.getDataHash() + "\r\n signature: -->" + signData.getSignature());
            return signDataService.verifySign(signData);
        } catch (Exception e) {
            log.error("verify error!", e);
        }
        return RespDTO.fail("系统繁忙，请稍后再试！");
    }
}
