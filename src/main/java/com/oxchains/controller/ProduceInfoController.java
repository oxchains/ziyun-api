package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.ProduceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产信息Controller
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/produceinfo")
public class ProduceInfoController extends BaseController{
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private ProduceInfoService produceInfoService;

    @PostMapping
    public RespDTO<String> addProduceInfo(@RequestBody String body){
        try {
            log.debug("===addProduceInfo==="+body);
            ProduceInfo produceInfo = gson.fromJson(body, ProduceInfo.class);
            String txID = chaincodeService.invoke("addProduceInfo", new String[] { gson.toJson(produceInfo) });
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

    @GetMapping("/{id}")
    public RespDTO<List<ProduceInfo>> getProduceInfo(@PathVariable String id) {
        System.err.println("-->生产ID：" + id);
        try {
            return produceInfoService.getProduceInfoList(id);
        } catch (Exception e) {
            log.error("getProduceInfo error!", e);
            return RespDTO.fail("操作失败");
        }
    }
}
