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
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$all\": [\""+uniqueCode+"\"]},\"Type\" :\"storage\"}}"});
        log.debug("===jsonStr1==="+jsonStr);
        StorageBillDTO storageBillDTO = simpleGson.fromJson(jsonStr, StorageBillDTO.class);
        if (storageBillDTO.getList().isEmpty()) {
            jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+uniqueCode+"\"}}"});
            log.debug("===jsonStr2==="+jsonStr);//parentcode
            GoodsDTO goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
            if(goodsDTO!=null && goodsDTO.getList().size()>0){
                String parentCode = goodsDTO.getList().get(0).getParentCode();
                if(!StringUtils.isEmpty(parentCode)){
                    jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$all\": [\""+parentCode+"\"]},\"Type\" :\"storage\"}}"});
                    log.debug("===jsonStr3==="+jsonStr);
                    storageBillDTO = simpleGson.fromJson(jsonStr, StorageBillDTO.class);
                    if (storageBillDTO.getList().isEmpty()) {
                        jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+parentCode+"\"}}"});
                        log.debug("===jsonStr4==="+jsonStr);//parent_parentcode
                        goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
                        if(goodsDTO!=null && goodsDTO.getList().size()>0){
                            String pparentCode = goodsDTO.getList().get(0).getParentCode();
                            if(!StringUtils.isEmpty(pparentCode)){
                                jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCodes\" : {\"$all\": [\""+pparentCode+"\"]},\"Type\" :\"storage\"}}"});
                                log.debug("===jsonStr5==="+jsonStr);
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
            log.debug("===transitSalesInfo.getToken()==="+storageBill.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { storageBill.getToken() });
            log.debug("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.debug("===username==="+username);
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
