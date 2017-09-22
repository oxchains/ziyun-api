package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.StorageBillDTO;
import com.oxchains.bean.dto.TransitSalesInfoDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.StorageBill;
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
 * Created by root on 17-7-26.
 */
@Service
@Slf4j
public class StorageBillService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addStorageBill(StorageBill storageBill) throws Exception {
        String txID = chaincodeService.invoke("saveStorageBill", new String[] { gson.toJson(storageBill) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<StorageBill>> getStorageBillList(String uniqueCode, String Token) {
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByView", new String[]{"{\"designDocName\":\"myview\",\"viewName\":\"view-storage\",\"key\":\""+uniqueCode+"\"}"});
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        log.debug("===jsonStr1==="+jsonStr);
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];
        StorageBillDTO storageBillDTO = simpleGson.fromJson(jsonStr, StorageBillDTO.class);
        if (storageBillDTO.getList().isEmpty()) {
            jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+uniqueCode+"\"}}"});
            if (StringUtils.isEmpty(jsonStr)) {
                return RespDTO.fail("没有数据");
            }
            log.debug("===jsonStr2==="+jsonStr);//parentcode
            jsonStr =  jsonStr.split("!#!")[0];
            txId = jsonStr.split("!#!")[1];
            GoodsDTO goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
            if(goodsDTO!=null && goodsDTO.getList().size()>0){
                String parentCode = goodsDTO.getList().get(0).getParentCode();
                if(!StringUtils.isEmpty(parentCode)){
                    jsonStr = chaincodeService.getPayloadAndTxid("searchByView", new String[]{"{\"designDocName\":\"myview\",\"viewName\":\"view-storage\",\"key\":\""+parentCode+"\"}"});
                    if (StringUtils.isEmpty(jsonStr)) {
                        return RespDTO.fail("没有数据");
                    }
                    log.debug("===jsonStr3==="+jsonStr);
                    jsonStr =  jsonStr.split("!#!")[0];
                    txId = jsonStr.split("!#!")[1];
                    storageBillDTO = simpleGson.fromJson(jsonStr, StorageBillDTO.class);
                    if (storageBillDTO.getList().isEmpty()) {
                        jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+parentCode+"\"}}"});
                        if (StringUtils.isEmpty(jsonStr)) {
                            return RespDTO.fail("没有数据");
                        }
                        log.debug("===jsonStr4==="+jsonStr);//parent_parentcode
                        jsonStr =  jsonStr.split("!#!")[0];
                        txId = jsonStr.split("!#!")[1];
                        goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
                        if(goodsDTO!=null && goodsDTO.getList().size()>0){
                            String pparentCode = goodsDTO.getList().get(0).getParentCode();
                            if(!StringUtils.isEmpty(pparentCode)){
                                jsonStr = chaincodeService.getPayloadAndTxid("searchByView", new String[]{"{\"designDocName\":\"myview\",\"viewName\":\"view-storage\",\"key\":\""+pparentCode+"\"}"});
                                if (StringUtils.isEmpty(jsonStr)) {
                                    return RespDTO.fail("没有数据");
                                }
                                log.debug("===jsonStr5==="+jsonStr);
                                jsonStr =  jsonStr.split("!#!")[0];
                                txId = jsonStr.split("!#!")[1];
                                storageBillDTO = simpleGson.fromJson(jsonStr, StorageBillDTO.class);
                            }
                        }
                    }
                }
            }
        }

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<StorageBill> it = storageBillDTO.getList().iterator(); it.hasNext();) {
            StorageBill storageBill  = it.next();
            storageBill.setTxId(txId);
            log.debug("===transitSalesInfo.getToken()==="+storageBill.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { storageBill.getToken() });
            log.info("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.info("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(storageBillDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }

        return RespDTO.success(storageBillDTO.getList());
    }
}
