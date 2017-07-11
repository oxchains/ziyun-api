package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.model.ziyun.Goods;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {
    @Resource
    private GoodsService goodsService;

    @PostMapping
    public RespDTO<String> addGoods(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addGoods==="+body);
            Goods goods = gson.fromJson(body, Goods.class);
            goods.setToken(Token);
            return goodsService.addGoods(goods);
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

    @GetMapping(value = "/{ProductCode}/{UniqueCode}/{CommodityCode}/{ProductionBatch}")
    public RespDTO<List<Goods>> queryGoodsList(@PathVariable String ProductCode, @PathVariable String UniqueCode,
                                               @PathVariable String CommodityCode, @PathVariable String ProductionBatch, @RequestParam String Token){
        try {
            log.debug("===queryGoods===");
            return goodsService.getGoodsList(ProductCode,UniqueCode,CommodityCode,ProductionBatch,Token);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
