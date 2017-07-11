package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.StorageBill;
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
@RequestMapping("/storagebill")
public class StorageBillController extends BaseController{
    @Resource
    private ChaincodeService chaincodeService;

    @PostMapping
    public RespDTO<String> addStorageBill(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addStorageBill==="+body);
            StorageBill storageBill = gson.fromJson(body, StorageBill.class);
            JwtToken jwt = TokenUtils.parseToken(Token);
            storageBill.setToken(jwt.getId());// store username ,not token
            String txID = chaincodeService.invoke("addStorageBill", new String[] { gson.toJson(storageBill) });
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
