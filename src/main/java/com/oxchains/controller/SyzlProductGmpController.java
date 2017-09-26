package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.ProductGmp;
import com.oxchains.bean.model.ziyun.SyzlProductGmp;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ProductGmpService;
import com.oxchains.service.SyzlProductGmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-8-8.
 */
@RestController
@RequestMapping("/syzlProductGmp")
@Slf4j
public class SyzlProductGmpController extends BaseController{
    @Resource
    private SyzlProductGmpService syzlProductGmpService;

    @PostMapping
    public RespDTO<String> addSyzlProductGmp(@RequestBody String body,@RequestParam String Token) {
        try {
            log.info("===addSyzlProductGmp==="+body);
            SyzlProductGmp syzlProductGmp = gson.fromJson(body, SyzlProductGmp.class);
            syzlProductGmp.setToken(Token);
            return syzlProductGmpService.addSyzlProductGmp(syzlProductGmp);
        } catch (Exception e) {
            log.error("addSyzlProductGmp error: ", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/{ProductName}", method = RequestMethod.GET)
    public RespDTO<List<SyzlProductGmp>> getSyzlProductGmpByProducName(@PathVariable String ProductName, @RequestParam String Token){
        try {
            log.info("===getSyzlProductGmpByProducName===");
            return syzlProductGmpService.getSyzlProductGmpByProductName(ProductName,Token);
        }catch (Exception e) {
            log.error("getSyzlProductGmpByProducName error: ", e);
        }
        return RespDTO.fail();
    }
}
