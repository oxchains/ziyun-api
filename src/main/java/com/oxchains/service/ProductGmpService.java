package com.oxchains.service;

import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.ProductGmp;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by root on 17-8-8.
 */
@Service
@Slf4j
public class ProductGmpService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addProductGmp(ProductGmp productGmp) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String token = productGmp.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        productGmp.setToken(jwt.getId());// store username ,not token
        String txID = chaincodeService.invoke("saveProductGmp", new String[] { gson.toJson(productGmp) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<ProductGmp>> getProductGmpByProducName(String ProducName,String Token){
        String result = chaincodeService.getPayloadAndTxid("getProductGmpByProducName", new String[]{ProducName});

        log.debug("===getProductGmpByProducName===" + result);
        if (StringUtils.isEmpty(result)) {
            return RespDTO.fail("没有数据");
        }
        String jsonStr = result.split("!#!")[0];
        String txId = result.split("!#!")[1];
        ProductGmp productGmp = simpleGson.fromJson(jsonStr, ProductGmp.class);
        productGmp.setTxId(txId);
        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();

        log.debug("===ProductGmp.getToken()==="+productGmp.getToken());
        String jsonAuth = chaincodeService.query("query", new String[] { productGmp.getToken() });
        log.debug("===jsonAuth==="+jsonAuth);
        Auth auth = gson.fromJson(jsonAuth, Auth.class);
        ArrayList<String> authList = auth.getAuthList();
        log.debug("===username==="+username);
        if(!authList.contains(username)){
            log.debug("===remove===");
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(Arrays.asList(productGmp));
    }
}
