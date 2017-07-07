package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.PurchaseInfoDTO;
import com.oxchains.bean.model.ziyun.PurchaseInfo;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
        String txID = chaincodeService.invoke("savePurchaseInfo", new String[] { gson.toJson(purchaseInfo) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<PurchaseInfo>> queryPurchaseInfoByGoodsId(String GoodsId){
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{"{\"selector\":{\"GoodsId\" : \""+GoodsId+"\"}}"});
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        PurchaseInfoDTO purchaseInfoDTO = simpleGson.fromJson(jsonStr, PurchaseInfoDTO.class);
        return RespDTO.success(purchaseInfoDTO.getList());
    }
}
