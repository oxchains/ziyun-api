package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.EnterpriseGmp;
import com.oxchains.bean.model.ziyun.SyzlEnterpriseGmp;
import com.oxchains.common.RespDTO;
import com.oxchains.service.SyzlEnterpriseGmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-9-26.
 */
@RestController
@RequestMapping("/syzlEnterpriseGmp")
@Slf4j
public class SyzlEnterpriseGmpController extends BaseController{
    @Resource
    private SyzlEnterpriseGmpService syzlEnterpriseGmpService;

    @PostMapping
    public RespDTO<String> addSyzlEnterprise(@RequestBody String body, @RequestParam String Token) {
        try {
            log.info("===addSyzlEnterprise==="+body);
            SyzlEnterpriseGmp syzlEnterpriseGmp = gson.fromJson(body,SyzlEnterpriseGmp.class);
            syzlEnterpriseGmp.setToken(Token);
            return syzlEnterpriseGmpService.addSyzlEnterpriseGmp(syzlEnterpriseGmp);
        } catch (Exception e) {
            log.error("addEnterprise error: ", e);
        }
        return RespDTO.fail();
    }


    @GetMapping("/{EnterpriseName}/{EnterpriseType}")
    public RespDTO<List<SyzlEnterpriseGmp>> getSyzlEnterpriseGmpByEnterpriseNameAndType(@PathVariable String EnterpriseName, @PathVariable String EnterpriseType, @RequestParam String Token){
        try {
            log.info("===getSyzlEnterpriseGmpByEnterpriseNameAndType===");
            return syzlEnterpriseGmpService.getSyzlEnterpriseGmpByEnterpriseNameAndType(EnterpriseName,EnterpriseType,Token);
        }catch (Exception e) {
            log.error("getSyzlEnterpriseGmpByEnterpriseNameAndType error: ", e);
        }
        return RespDTO.fail();
    }
}
