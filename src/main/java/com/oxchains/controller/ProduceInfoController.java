package com.oxchains.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.ProduceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.data.jpa.domain.AbstractAuditable_;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
    public RespDTO<String> addProduceInfo(@RequestBody String body,@RequestParam String Token){
        try {
            log.info("===addProduceInfo==="+body);
            if (StringUtils.isBlank(body)) {
                return RespDTO.fail("参数错误");
            }
            ProduceInfo produceInfo = produceInfo = gson.fromJson(body, ProduceInfo.class);
            produceInfo.setToken(Token);
            return produceInfoService.addProduceInfo(produceInfo);
        } catch(JsonSyntaxException e){
            log.error("addProduceInfo error: ", e);
            return RespDTO.fail("操作失败", ConstantsData.RTN_INVALID_ARGS);
        } catch (Exception e) {
            log.error("addProduceInfo error: ", e);
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }

    @GetMapping("/{Id}")
    public RespDTO<List<ProduceInfo>> getProduceInfo(@PathVariable String Id, @RequestParam String Token) {
        log.info("===getProduceInfo===");
        try {
            return produceInfoService.getProduceInfoList(Id,Token);
        } catch (Exception e) {
            log.error("getProduceInfo error: ", e);
            return RespDTO.fail("操作失败");
        }
    }
}
