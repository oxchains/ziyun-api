package com.oxchains.controller;

import com.google.gson.JsonSyntaxException;
import com.oxchains.bean.model.ziyun.Product;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品信息Controller
 * Created by root on 17-7-3.
 */
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;

    @PostMapping
    public RespDTO<String> addProduct(@RequestBody String body,@RequestParam String Token){
        try {
            log.debug("===addProduct==="+body);
            if (StringUtils.isBlank(body)) {
                return RespDTO.fail("参数错误");
            }
            Product product = gson.fromJson(body, Product.class);
            product.setToken(Token);
            return productService.addProduct(product);
        } catch(JsonSyntaxException e){
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_INVALID_ARGS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
    }

    @GetMapping("/{ApprovalNumber}/{ProductCode}")
    public RespDTO<List<Product>> queryProductInfo(@PathVariable String ApprovalNumber, @PathVariable String ProductCode, @RequestParam String Token) {
        try {
            return productService.getProductList(ApprovalNumber, ProductCode,Token);
        }catch (Exception e) {
            log.error("query error!", e);
        }
        return RespDTO.fail();
    }

}
