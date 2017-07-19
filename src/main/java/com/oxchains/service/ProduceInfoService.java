package com.oxchains.service;

import com.oxchains.bean.dto.ProduceInfoDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.Goods;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 生产信息Service
 * Created by Luo_xuri on 2017/7/5.
 */
@Service
@Slf4j
public class ProduceInfoService extends BaseService {

    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addProduceInfo(ProduceInfo produceInfo) throws Exception {
        String token = produceInfo.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        produceInfo.setToken(jwt.getId());// store username ,not token
        String txID = chaincodeService.invoke("saveProduceInfo", new String[] { gson.toJson(produceInfo) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }


    public RespDTO<List<ProduceInfo>> getProduceInfoList(String id,String Token) {
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{" {\"selector\":{\"Id\" : \""+id+"\"}}"});
        log.debug("-->生产信息JSON：" + jsonStr);
        if (StringUtils.isEmpty(jsonStr)){
            return RespDTO.fail("没有数据");
        }
        ProduceInfoDTO produceInfoDTO = simpleGson.fromJson(jsonStr, ProduceInfoDTO.class);

        JwtToken jwt = TokenUtils.parseToken(Token);
        String username = jwt.getId();
        for (Iterator<ProduceInfo> it = produceInfoDTO.getList().iterator(); it.hasNext();) {
            ProduceInfo ProduceInfo = it.next();
            log.debug("===ProduceInfo.getToken()==="+ProduceInfo.getToken());
            String jsonAuth = chaincodeService.query("query", new String[] { ProduceInfo.getToken() });
            log.debug("===jsonAuth==="+jsonAuth);
            Auth auth = gson.fromJson(jsonAuth, Auth.class);
            ArrayList<String> authList = auth.getAuthList();
            log.debug("===username==="+username);
            if(!authList.contains(username)){
                log.debug("===remove===");
                it.remove();
            }
        }
        if(produceInfoDTO.getList().isEmpty()){
            return RespDTO.fail("操作失败", ConstantsData.RTN_UNAUTH);
        }


        log.debug("-->生产信息集合：" + produceInfoDTO.getList());
        return RespDTO.success(produceInfoDTO.getList());
    }
}
