package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.TransportBill;
import com.oxchains.common.ChaincodeResp;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.dao.ChaincodeData;
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
    private ChaincodeData chaincodeData;

    @PostMapping
    public RespDTO<String> addTransportBill(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addTransportBill==="+body);
            TransportBill transportBill = gson.fromJson(body, TransportBill.class);
            JwtToken jwt = TokenUtils.parseToken(Token);
            transportBill.setToken(jwt.getId());// store username ,not token
            String txID = chaincodeData.invoke("saveTransportBill", new String[] { gson.toJson(transportBill) })
                    .filter(ChaincodeResp::succeeded)
                    .map(ChaincodeResp::getPayload)
                    .orElse(null);
            log.debug("===txID==="+txID);
            if(txID == null){
                return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
            }
            return RespDTO.success("操作成功");
        }catch(JsonSyntaxException e){
            log.error("addTransportBill json error", e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_INVALID_ARGS);
        }catch (Exception e) {
            log.error("addTransportBill error", e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
