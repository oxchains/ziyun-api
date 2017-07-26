package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.Goods;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.TransitSalesInfo;
import com.oxchains.bean.model.ziyun.TransportBill;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.TransitSalesInfoService;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-7-24.
 */
@Slf4j
@RestController
@RequestMapping("/transitsalesinfo")
public class TransitSalesInfoController extends BaseController{
    @Resource
    private TransitSalesInfoService transitSalesInfoService;

    @PostMapping
    public RespDTO<String> addTransitSalesInfo(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addTransitSalesInfo==="+body);
            TransitSalesInfo transitSalesInfo = gson.fromJson(body, TransitSalesInfo.class);
            JwtToken jwt = TokenUtils.parseToken(Token);
            transitSalesInfo.setToken(jwt.getId());// store username ,not token
            return transitSalesInfoService.addTransitSalesInfo(transitSalesInfo);
        }
        catch(JsonSyntaxException e){
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_INVALID_ARGS);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/{UniqueCode}")
    public RespDTO<List<TransitSalesInfo>> getTransitSalesInfoList(@PathVariable String UniqueCode, @RequestParam String Token){
        try {
            log.debug("===getTransitSalesInfoList===");
            return transitSalesInfoService.getTransitSalesInfoList(UniqueCode,Token);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
