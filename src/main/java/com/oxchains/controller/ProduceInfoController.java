package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.common.ChaincodeResp;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.dao.ChaincodeData;
import com.oxchains.service.ProduceInfoService;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.util.Resources_de;

import javax.annotation.Resource;
import java.util.List;

import static com.oxchains.common.RespDTO.fail;
import static com.oxchains.common.RespDTO.success;

/**
 * 生产信息Controller
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/produceinfo")
public class ProduceInfoController extends BaseController{

    @Resource
    private ProduceInfoService produceInfoService;

    @PostMapping
    public RespDTO<String> addProduceInfo(@RequestBody String body, @RequestParam String Token){
        try {
            log.debug("===addProduceInfo==="+body);
            if (StringUtils.isBlank(body)) {
                return RespDTO.fail("参数错误");
            }
            ProduceInfo produceInfo = gson.fromJson(body, ProduceInfo.class);
            produceInfo.setToken(Token);
            return produceInfoService.addProduceInfo(produceInfo);
        } catch(JsonSyntaxException e){
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_INVALID_ARGS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }

    @GetMapping("/{id}")
    public RespDTO<List<ProduceInfo>> getProduceInfo(@PathVariable String id, @RequestParam String Token) {
        log.debug("-->生产ID：" + id);
        try {
            return produceInfoService.getProduceInfoList(id,Token);
        } catch (Exception e) {
            log.error("getProduceInfo error!", e);
            return fail("操作失败");
        }
    }
}
