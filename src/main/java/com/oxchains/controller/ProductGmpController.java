package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.Product;
import com.oxchains.bean.model.ziyun.ProductGmp;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ProductGmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-8-8.
 */
@RestController
@RequestMapping("/productGmp")
@Slf4j
public class ProductGmpController extends BaseController{
    @Resource
    private ProductGmpService productGmpService;

    @PostMapping
    public RespDTO<String> addProductGmp(@RequestBody String body) {
        try {
            //log.debug("===addProductGmp==="+body);
            System.out.println("===addProductGmp==="+body);
            ProductGmp productGmp = gson.fromJson(body, ProductGmp.class);
            return productGmpService.addProductGmp(productGmp);
        } catch (Exception e) {
            log.error("addProductGmp error!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/{ProducName}", method = RequestMethod.GET)
    public RespDTO<List<ProductGmp>> getProductGmpByProducName(@PathVariable String ProducName){
        try {
            System.out.println("===getProductGmpByProducName===");
            //log.debug("===getProductGmpByProducName==="+ProducName);
            return productGmpService.getProductGmpByProducName(ProducName);
        }catch (Exception e) {
            log.error("query error!", e);
        }
        return RespDTO.fail();
    }
}
