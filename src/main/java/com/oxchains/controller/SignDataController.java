package com.oxchains.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.bean.dto.datav.SignData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.SignDataService;
import lombok.extern.slf4j.Slf4j;
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
    public RespDTO<String> getClientSign(@RequestBody String body) throws Exception {
        SignData data = gson.fromJson(body, SignData.class);
        JSONObject jsonObject = JSON.parseObject(body);
        String data_hash = jsonObject.getString("data_hash");

        System.out.println("body: -->"+body);
        return signDataService.getClientSign(data_hash);
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public RespDTO<String> verifySign(@RequestBody String body) throws Exception {
        JSONObject jsonObject = JSON.parseObject(body);
        String dataHash = jsonObject.getString("data_hash");
        String signature = jsonObject.getString("signature");
        return signDataService.verifySign(dataHash, signature);
    }
}
