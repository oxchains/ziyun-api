package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.SalesInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.SalesInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/salesinfo")
public class SalesInfoController extends BaseController {
    @Resource
    private SalesInfoService salesInfoService;

    @PostMapping
    public RespDTO<String> addSalesInfo(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addSalesInfo==="+body);
            SalesInfo salesInfo = gson.fromJson(body, SalesInfo.class);
            salesInfo.setToken(Token);
            return salesInfoService.addSalesInfo(salesInfo);
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

    @GetMapping(value = "/{No}/{PurchaseId}/{GoodsId}/{ProductionBatch}")
    public RespDTO<List<SalesInfo>> querySalesInfoList(@PathVariable String No,@PathVariable String PurchaseId,@PathVariable String GoodsId,@PathVariable String ProductionBatch, @RequestParam String Token){
        try {
            log.debug("===querySalesInfoList===");
            return salesInfoService.querySalesInfoList(No,PurchaseId,GoodsId,ProductionBatch,Token);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
