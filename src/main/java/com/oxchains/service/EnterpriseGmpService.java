package com.oxchains.service;

import com.oxchains.bean.dto.EnterpriseGmpDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.EnterpriseGmp;
import com.oxchains.bean.model.ziyun.JwtToken;
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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by root on 17-8-8.
 */
@Service
@Slf4j
public class EnterpriseGmpService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addEnterpriseGmp(EnterpriseGmp enterpriseGmp) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String token = enterpriseGmp.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        enterpriseGmp.setToken(jwt.getId());
        String txID = chaincodeService.invoke("saveEnterpriseGmp", new String[] { gson.toJson(enterpriseGmp) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<EnterpriseGmp>> getEnterpriseGmpByEnterpriseNameAndType(String EnterpriseName, String EnterpriseType, String Token){
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\n" +
                "    \"EnterpriseName\": \""+EnterpriseName+"\"\n" + " ,   \"EnterpriseType\": \""+EnterpriseType+ "\"}}"});
        log.debug("===getProductGmpByProducName===" + jsonStr);
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        EnterpriseGmpDTO enterpriseGmpDTO = simpleGson.fromJson(jsonStr, EnterpriseGmpDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<EnterpriseGmp> it = enterpriseGmpDTO.getList().iterator(); it.hasNext();) {
            EnterpriseGmp EnterpriseGmp = it.next();
            log.debug("===EnterpriseGmp.getToken()==="+EnterpriseGmp.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { EnterpriseGmp.getToken() });
            log.debug("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.debug("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(enterpriseGmpDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }
        return RespDTO.success(enterpriseGmpDTO.getList());
    }
}
