package com.oxchains.service;

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
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"UniqueCode\" : \""+ uniqueCode +"\"}}"});
        log.debug("===jsonStr==="+jsonStr);
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        TransitSalesInfoDTO transitSalesInfoDTO = simpleGson.fromJson(jsonStr, TransitSalesInfoDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<TransitSalesInfo> it = transitSalesInfoDTO.getList().iterator(); it.hasNext();) {
            TransitSalesInfo transitSalesInfo = it.next();
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
