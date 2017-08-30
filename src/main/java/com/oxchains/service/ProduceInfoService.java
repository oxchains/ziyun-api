package com.oxchains.service;

import com.oxchains.Application;
import com.oxchains.bean.dto.ProduceInfoDTO;
import com.oxchains.bean.model.ziyun.Auth;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        produceInfo.setToken(Application.userContext().get().getUsername());// store username ,not token
        String txID = chaincodeService.invoke("saveProduceInfo", new String[] { gson.toJson(produceInfo) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }


    public RespDTO<List<ProduceInfo>> getProduceInfoList(String id) {
        String jsonStr = chaincodeService.getPayloadAndTxid("searchByQuery", new String[]{" {\"selector\":{\"Id\" : \""+id+"\"}}"});
        log.debug("-->生产信息JSON：" + jsonStr);
        if (StringUtils.isEmpty(jsonStr)){
            return RespDTO.fail("没有数据");
        }
        String txId = jsonStr.split("!#!")[1];
        jsonStr =  jsonStr.split("!#!")[0];

        ProduceInfoDTO produceInfoDTO = simpleGson.fromJson(jsonStr, ProduceInfoDTO.class);

        String username = Application.userContext().get().getUsername();
        for (Iterator<ProduceInfo> it = produceInfoDTO.getList().iterator(); it.hasNext();) {
            ProduceInfo ProduceInfo = it.next();
            ProduceInfo.setTxId(txId);
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
