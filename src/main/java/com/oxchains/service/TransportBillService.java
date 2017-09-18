package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.TransitSalesInfoDTO;
import com.oxchains.bean.dto.TransportBillDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.TransitSalesInfo;
import com.oxchains.bean.model.ziyun.TransportBill;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 17-8-23.
 */
@Slf4j
@Service
public class TransportBillService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addTransportBill(TransportBill transportBill) throws Exception{
        String txID = chaincodeService.invoke("saveTransportBill", new String[] { gson.toJson(transportBill) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<TransportBill>> getTransportBillList( String uniqueCode,  String Token){
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$eq\": [\""+uniqueCode+"\"]}}}"});
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        log.debug("===jsonStr1==="+jsonStr);
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];
        TransportBillDTO transportBillDTO = simpleGson.fromJson(jsonStr, TransportBillDTO.class);
        if (transportBillDTO.getList().isEmpty()) {
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
                    transportBillDTO = simpleGson.fromJson(jsonStr, TransportBillDTO.class);
                    if (transportBillDTO.getList().isEmpty()) {
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
                                transportBillDTO = simpleGson.fromJson(jsonStr, TransportBillDTO.class);
                            }
                        }
                    }
                }
            }
        }

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<TransportBill> it = transportBillDTO.getList().iterator(); it.hasNext();) {
            TransportBill transportBill = it.next();
            transportBill.setTxId(txId);
            log.debug("===transitSalesInfo.getToken()==="+transportBill.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { transportBill.getToken() });
            log.info("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.info("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(transportBillDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(transportBillDTO.getList());
    }
}
