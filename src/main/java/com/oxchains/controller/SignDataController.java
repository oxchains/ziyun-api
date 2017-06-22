package com.oxchains.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.bean.dto.datav.SignData;
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
    public RespDTO<String> getClientSign(@RequestBody String body) {
        try {
            JSONObject jsonObject = JSON.parseObject(body);
            String dataHash = jsonObject.getString("data_hash");
            if (StringUtils.isBlank(dataHash)) {
                return RespDTO.fail("参数错误");
            }

            System.out.println("body: -->" + body);
            return signDataService.getClientSign(dataHash);
        } catch (Exception e) {
            log.error("sign error!", e);
        }
        return RespDTO.fail("系统繁忙，请稍后再试！");
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public RespDTO<Boolean> verifySign(@RequestBody String body) {
        try {
            JSONObject jsonObject = JSON.parseObject(body);
            String dataHash = jsonObject.getString("data_hash");
            String signature = jsonObject.getString("signature");
            if (StringUtils.isBlank(dataHash) || StringUtils.isBlank(signature)) {
                return RespDTO.fail("参数错误");
            }
            return signDataService.verifySign(dataHash, signature);
        } catch (Exception e) {
            log.error("verify error!", e);
        }
        return RespDTO.fail("系统繁忙，请稍后再试！");
    }
}
