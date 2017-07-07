package com.oxchains.service;

import com.oxchains.bean.dto.PurchaseInfoDTO;
import com.oxchains.bean.dto.SalesInfoDTO;
import com.oxchains.bean.model.ziyun.PurchaseInfo;
import com.oxchains.bean.model.ziyun.SalesInfo;
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
public class SalesInfoService extends BaseService  {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addSalesInfo(SalesInfo salesInfo) throws Exception{
        String txID = chaincodeService.invoke("saveSalesInfo", new String[] { gson.toJson(salesInfo) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<SalesInfo>> querySalesInfoList(String No,String PurchaseId,String GoodsId,String ProductionBatch ){
        String jsonStr = chaincodeService.query("searchByQuery", new String[]{
                "{\"selector\":{\"No\" : \""+No+"\",\"PurchaseId\" : \"" + PurchaseId + "\"," +
                        "\"GoodsId\" : \"" + GoodsId + "\",\"ProductBatch\" : \"" + ProductionBatch + "\"}}"});
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        SalesInfoDTO salesInfoDTO = simpleGson.fromJson(jsonStr, SalesInfoDTO.class);
        return RespDTO.success(salesInfoDTO.getList());
    }
}
