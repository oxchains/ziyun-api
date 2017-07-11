package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.PurchaseInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.PurchaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/purchaseinfo")
public class PurchaseInfoController extends BaseController  {

    @Resource
    private PurchaseInfoService purchaseInfoService;

    @PostMapping
    public RespDTO<String> addPurchaseInfo(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addPurchaseInfo==="+body);
            PurchaseInfo purchaseInfo = gson.fromJson(body, PurchaseInfo.class);
            purchaseInfo.setToken(Token);
            return purchaseInfoService.addPurchaseInfo(purchaseInfo);
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

    @GetMapping(value="/{GoodsId}")
    public RespDTO<List<PurchaseInfo>> queryPurchaseInfoByGoodsId(@PathVariable String GoodsId, @RequestParam String Token){
        try {
            log.debug("===queryPurchaseInfoByGoodsId==="+GoodsId);
            return purchaseInfoService.queryPurchaseInfoByGoodsId(GoodsId,Token);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
