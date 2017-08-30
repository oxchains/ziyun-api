package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.Application;
import com.oxchains.bean.model.ziyun.TransitSalesInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.TransitSalesInfoService;
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
    public RespDTO<String> addTransitSalesInfo(@RequestBody String body){
        try {
            log.debug("===addTransitSalesInfo==="+body);
            TransitSalesInfo transitSalesInfo = gson.fromJson(body, TransitSalesInfo.class);
            transitSalesInfo.setToken(Application.userContext().get().getUsername());// store username ,not token
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
    public RespDTO<List<TransitSalesInfo>> getTransitSalesInfoList(@PathVariable String UniqueCode){
        try {
            log.debug("===getTransitSalesInfoList===");
            return transitSalesInfoService.getTransitSalesInfoList(UniqueCode);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
