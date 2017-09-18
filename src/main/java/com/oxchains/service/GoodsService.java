package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.WaybillDTO;
import com.oxchains.bean.model.ziyun.*;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 17-7-5.
 */
@Slf4j
@Service
public class GoodsService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addGoods(Goods goods) throws Exception{
        String token = goods.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        goods.setToken(jwt.getId());// store username ,not token
        String txID = chaincodeService.invoke("saveGoods", new String[] { gson.toJson(goods) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<Goods>> getGoodsList(String UniqueCode,String Token) throws Exception{
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\n" +
                "    \"UniqueCode\": \""+UniqueCode+"\"\n" +
                "}}"});
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        log.debug("===getGoodsList==="+jsonStr);
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];
        GoodsDTO goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<Goods> it = goodsDTO.getList().iterator(); it.hasNext();) {
            Goods goods = it.next();
            goods.setTxId(txId);
            log.debug("===goods.getToken()==="+goods.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { goods.getToken() });
            log.info("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.info("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(goodsDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(goodsDTO.getList());
    }
}
