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

    public RespDTO<List<Product>> getProductList(String Id, String Token) {
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"Id\" : \""+ Id +"\"}}"});
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];
        ProductDTO productDTO = simpleGson.fromJson(jsonStr, ProductDTO.class);
        ProductDTO result = new ProductDTO();

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<Product> it = productDTO.getList().iterator(); it.hasNext();) {
            Product Product = it.next();
            Product.setTxId(txId);
            log.debug("===Product.getToken()==="+Product.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { Product.getToken() });
            log.info("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.info("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(productDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }
        return RespDTO.success(productDTO.getList());
    }

}
