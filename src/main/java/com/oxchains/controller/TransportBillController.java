package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.TransportBill;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/transportbill")
public class TransportBillController extends BaseController{
    @Resource
    private ChaincodeService chaincodeService;

    @PostMapping
    public RespDTO<String> addTransportBill(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addTransportBill==="+body);
            TransportBill transportBill = gson.fromJson(body, TransportBill.class);
            JwtToken jwt = TokenUtils.parseToken(Token);
            transportBill.setToken(jwt.getId());// store username ,not token
            String txID = chaincodeService.invoke("addTransportBill", new String[] { gson.toJson(transportBill) });
            log.debug("===txID==="+txID);
            if(txID == null){
                return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
            }
            return RespDTO.success("操作成功");
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
}
