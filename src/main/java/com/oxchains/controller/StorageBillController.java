package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.Application;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.StorageBill;
import com.oxchains.bean.model.ziyun.TransitSalesInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.StorageBillService;
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
@RequestMapping("/storagebill")
public class StorageBillController extends BaseController{
    @Resource
    private StorageBillService storageBillService;

    @PostMapping
    public RespDTO<String> addStorageBill(@RequestBody String body){
        try {
            log.debug("===addStorageBill==="+body);
            StorageBill storageBill = gson.fromJson(body, StorageBill.class);
            storageBill.setToken(Application.userContext().get().getUsername());// store username ,not token
            return storageBillService.addStorageBill(storageBill);
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
    public RespDTO<List<StorageBill>> getStorageBillList(@PathVariable String UniqueCode){
        try {
            log.debug("===getStorageBillList===");
            return storageBillService.getStorageBillList(UniqueCode);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }
}
