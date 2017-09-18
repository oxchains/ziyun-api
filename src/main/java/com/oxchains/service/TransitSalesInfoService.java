package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.ProductDTO;
import com.oxchains.bean.dto.TransitSalesInfoDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.Product;
import com.oxchains.bean.model.ziyun.TransitSalesInfo;
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
 * 批发零售信息Service
 */
@Service
@Slf4j
public class TransitSalesInfoService extends BaseService {

    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addTransitSalesInfo(TransitSalesInfo transitSalesInfo) throws Exception {
        String txID = chaincodeService.invoke("saveTransitSalesInfo", new String[] { gson.toJson(transitSalesInfo) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<TransitSalesInfo>> getTransitSalesInfoList(String uniqueCode,String Token) {
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$eq\": [\""+uniqueCode+"\"]},\"Type\" :\"transitSales\"}}"});
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        log.debug("===jsonStr1==="+jsonStr);
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];
        TransitSalesInfoDTO transitSalesInfoDTO = simpleGson.fromJson(jsonStr, TransitSalesInfoDTO.class);
        if (transitSalesInfoDTO.getList().isEmpty()) {
            jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+uniqueCode+"\"}}"});
            if (StringUtils.isEmpty(jsonStr)) {
                return RespDTO.fail("没有数据");
            }
            log.debug("===jsonStr2==="+jsonStr);//parentcode
            txId = jsonStr.split("!#!")[1];
            jsonStr =  jsonStr.split("!#!")[0];
            GoodsDTO goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
            if(goodsDTO!=null && goodsDTO.getList().size()>0){
                String parentCode = goodsDTO.getList().get(0).getParentCode();
                if(!StringUtils.isEmpty(parentCode)){
                    jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$eq\": [\""+parentCode+"\"]},\"Type\" :\"transitSales\"}}"});
                    if (StringUtils.isEmpty(jsonStr)) {
                        return RespDTO.fail("没有数据");
                    }
                    log.debug("===jsonStr3==="+jsonStr);
                    txId = jsonStr.split("!#!")[1];
                    jsonStr =  jsonStr.split("!#!")[0];
                    transitSalesInfoDTO = simpleGson.fromJson(jsonStr, TransitSalesInfoDTO.class);
                    if (transitSalesInfoDTO.getList().isEmpty()) {
                        jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+parentCode+"\"}}"});
                        if (StringUtils.isEmpty(jsonStr)) {
                            return RespDTO.fail("没有数据");
                        }
                        log.debug("===jsonStr4==="+jsonStr);//parent_parentcode
                        txId = jsonStr.split("!#!")[1];
                        jsonStr =  jsonStr.split("!#!")[0];
                        goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
                        if(goodsDTO!=null && goodsDTO.getList().size()>0){
                            String pparentCode = goodsDTO.getList().get(0).getParentCode();
                            if(!StringUtils.isEmpty(pparentCode)){
                                jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$eq\": [\""+pparentCode+"\"]},\"Type\" :\"transitSales\"}}"});
                                if (StringUtils.isEmpty(jsonStr)) {
                                    return RespDTO.fail("没有数据");
                                }
                                log.debug("===jsonStr5==="+jsonStr);
                                txId = jsonStr.split("!#!")[1];
                                jsonStr =  jsonStr.split("!#!")[0];
                                transitSalesInfoDTO = simpleGson.fromJson(jsonStr, TransitSalesInfoDTO.class);
                            }
                        }
                    }
                }
            }
        }

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<TransitSalesInfo> it = transitSalesInfoDTO.getList().iterator(); it.hasNext();) {
            TransitSalesInfo transitSalesInfo = it.next();
            transitSalesInfo.setTxId(txId);
            log.debug("===transitSalesInfo.getToken()==="+transitSalesInfo.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { transitSalesInfo.getToken() });
            log.debug("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.debug("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(transitSalesInfoDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(transitSalesInfoDTO.getList());
    }

}
