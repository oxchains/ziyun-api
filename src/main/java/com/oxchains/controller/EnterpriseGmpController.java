package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.EnterpriseGmp;
import com.oxchains.common.RespDTO;
import com.oxchains.service.EnterpriseGmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-8-8.
 */
@RestController
@RequestMapping("/enterpriseGmp")
@Slf4j
public class EnterpriseGmpController extends BaseController {

    @Resource
    private EnterpriseGmpService enterpriseGmpService;

    @PostMapping
    public RespDTO<String> addEnterprise(@RequestBody String body,@RequestParam String Token) {
        try {
            log.debug("===addEnterprise==="+body);
            EnterpriseGmp enterpriseGmp = gson.fromJson(body,EnterpriseGmp.class);
            enterpriseGmp.setToken(Token);
            return enterpriseGmpService.addEnterpriseGmp(enterpriseGmp);
        } catch (Exception e) {
            log.error("addEnterprise error!", e);
        }
        return RespDTO.fail();
    }


    @GetMapping("/{EnterpriseName}/{EnterpriseType}")
    public RespDTO<List<EnterpriseGmp>> getEnterpriseGmpByEnterpriseNameAndType(@PathVariable String EnterpriseName,@PathVariable String EnterpriseType,@RequestParam String Token){
        try {
            return enterpriseGmpService.getEnterpriseGmpByEnterpriseNameAndType(EnterpriseName,EnterpriseType,Token);
        }catch (Exception e) {
            log.error("query error!", e);
        }
        return RespDTO.fail();
    }
}
