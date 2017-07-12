package com.oxchains.service;

import com.oxchains.bean.dto.ProductDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.Goods;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.Product;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 产品信息Service
 * Created by Luo_xuri on 2017/7/5.
 */
@Service
@Slf4j
public class ProductService extends BaseService {

    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addProduct(Product product) throws Exception {
        String token = product.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        product.setToken(jwt.getId());// store username ,not token
        String txID = chaincodeService.invoke("saveProduct", new String[] { gson.toJson(product) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<Product>> getProductList(String approvalNumber, String productCode,String Token) {
        System.err.println("-->产品批准文号：" + approvalNumber + "\r\n -->产品编码：" + productCode);
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"ApprovalNumber\" : \""+ approvalNumber +"\", \"ProductCode\" : \""+ productCode +"\"}}"});

        System.err.println("-->产品JSON：" + jsonStr);
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        ProductDTO productDTO = simpleGson.fromJson(jsonStr, ProductDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<Product> it = productDTO.getList().iterator(); it.hasNext();) {
            Product Product = it.next();
            Product.setToken(username);
            log.debug("===Product.getToken()==="+Product.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { Product.getToken() });
            log.debug("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.debug("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(productDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        System.err.println("-->产品集合：" + productDTO.getList());
        return RespDTO.success(productDTO.getList());
    }

}
