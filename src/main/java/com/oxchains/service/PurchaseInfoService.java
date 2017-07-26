package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.PurchaseInfoDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.Product;
import com.oxchains.bean.model.ziyun.PurchaseInfo;
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
@Service
@Slf4j
public class PurchaseInfoService extends BaseService  {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addPurchaseInfo(PurchaseInfo purchaseInfo) throws Exception{
        String token = purchaseInfo.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        purchaseInfo.setToken(jwt.getId());// store username ,not token
        String txID = chaincodeService.invoke("savePurchaseInfo", new String[] { gson.toJson(purchaseInfo) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<PurchaseInfo>> queryPurchaseInfoByUniqueCode(String UniqueCode,String Token){
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$all\": [\""+UniqueCode+"\"]},\"Type\" :\"purchase\"}}"});
        log.debug("===jsonStr1==="+jsonStr);
        PurchaseInfoDTO purchaseInfoDTO = simpleGson.fromJson(jsonStr, PurchaseInfoDTO.class);
        if (purchaseInfoDTO.getList().isEmpty()) {
            jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+UniqueCode+"\"}}"});
            log.debug("===jsonStr2==="+jsonStr);//parentcode
            GoodsDTO goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
            if(goodsDTO!=null && goodsDTO.getList().size()>0){
                String parentCode = goodsDTO.getList().get(0).getParentCode();
                if(!StringUtils.isEmpty(parentCode)){
                    jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$all\": [\""+parentCode+"\"]},\"Type\" :\"purchase\"}}"});
                    log.debug("===jsonStr3==="+jsonStr);
                    purchaseInfoDTO = simpleGson.fromJson(jsonStr, PurchaseInfoDTO.class);
                    if (purchaseInfoDTO.getList().isEmpty()) {
                        jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+parentCode+"\"}}"});
                        log.debug("===jsonStr4==="+jsonStr);//parent_parentcode
                        goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
                        if(goodsDTO!=null && goodsDTO.getList().size()>0){
                            String pparentCode = goodsDTO.getList().get(0).getParentCode();
                            if(!StringUtils.isEmpty(pparentCode)){
                                jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$all\": [\""+pparentCode+"\"]},\"Type\" :\"purchase\"}}"});
                                log.debug("===jsonStr5==="+jsonStr);
                                purchaseInfoDTO = simpleGson.fromJson(jsonStr, PurchaseInfoDTO.class);
                            }
                        }
                    }
                }
            }
        }


        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<PurchaseInfo> it = purchaseInfoDTO.getList().iterator(); it.hasNext();) {
            PurchaseInfo PurchaseInfo = it.next();
            log.debug("===PurchaseInfo.getToken()==="+PurchaseInfo.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { PurchaseInfo.getToken() });
            log.debug("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.debug("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(purchaseInfoDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(purchaseInfoDTO.getList());
    }
}
