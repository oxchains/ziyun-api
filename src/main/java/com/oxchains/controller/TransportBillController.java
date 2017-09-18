package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.TransportBill;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.TransportBillService;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/transportbill")
public class TransportBillController extends BaseController{
    @Resource
    private ChaincodeService chaincodeService;

    @Resource
    private TransportBillService transportBillService;

    @PostMapping
    public RespDTO<String> addTransportBill(@RequestBody String body,@RequestParam String Token){
        try {
            log.info("===addTransportBill==="+body);
            TransportBill transportBill = gson.fromJson(body, TransportBill.class);
            JwtToken jwt = TokenUtils.parseToken(Token);
            transportBill.setToken(jwt.getId());// store username ,not token
            return transportBillService.addTransportBill(transportBill);
        }
        catch(JsonSyntaxException e){
            log.error("addTransportBill error: ",e);
            return RespDTO.fail("操作失败", ConstantsData.RTN_INVALID_ARGS);
        }
        catch (Exception e) {
            log.error("addTransportBill error: ",e);
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/{UniqueCode}")
    public RespDTO<List<TransportBill>> getTransportBillList(@PathVariable String UniqueCode, @RequestParam String Token){
        try {
            log.info("===getTransportBillList===");
            return transportBillService.getTransportBillList(UniqueCode,Token);
        }
        catch (Exception e) {
            log.error("getTransportBillList error: ",e);
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
