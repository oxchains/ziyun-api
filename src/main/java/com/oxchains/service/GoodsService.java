package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.WaybillDTO;
import com.oxchains.bean.model.ziyun.*;
import com.oxchains.common.ChaincodeResp;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.dao.ChaincodeData;
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
    private ChaincodeData chaincodeData;

    public RespDTO<String> addGoods(Goods goods) throws Exception{
        String token = goods.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        goods.setToken(jwt.getId());// store username ,not token
        String txID = chaincodeData.invoke("saveGoods", new String[] { gson.toJson(goods) }).filter(ChaincodeResp::succeeded).map(ChaincodeResp::getPayload).orElse(null);
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<Goods>> getGoodsList(String ProductCode, String UniqueCode, String CommodityCode, String ProductionBatch,String Token) throws Exception{
        String jsonStr = chaincodeData.query("searchByQuery", new String[]{"{\"selector\":{\n" +
                "    \"CommodityCode\": \""+CommodityCode+"\",\n" +
                "    \"ProductCode\": \""+ProductCode+"\",\n" +
                "    \"ProductionBatch\": \""+ProductionBatch+"\",\n" +
                "    \"UniqueCode\": \""+UniqueCode+"\"\n" +
                "}}"}).filter(ChaincodeResp::succeeded).map(ChaincodeResp::getPayload).orElse(null);
        //String jsonStr = chaincodeService.query();
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        log.debug("===getGoodsList==="+jsonStr);
        GoodsDTO goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<Goods> it = goodsDTO.getList().iterator(); it.hasNext();) {
            Goods goods = it.next();
            log.debug("===goods.getToken()==="+goods.getToken());
            String jsonAuth = chaincodeData.query("query", new String[] { goods.getToken() })
                    .filter(ChaincodeResp::succeeded)
                    .map(ChaincodeResp::getPayload).orElse(null);
            if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
                return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
            }
            log.debug("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.debug("===username==="+username);
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
