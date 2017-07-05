package com.oxchains.service;

import com.oxchains.bean.dto.GoodsDTO;
import com.oxchains.bean.dto.WaybillDTO;
import com.oxchains.bean.model.ziyun.Goods;
import com.oxchains.bean.model.ziyun.Waybill;
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
@Slf4j
@Service
public class GoodsService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> addGoods(Goods goods) throws Exception{
        String txID = chaincodeService.invoke("addGoods", new String[] { gson.toJson(goods) });
        log.debug("===txID==="+txID);
        if(txID == null){
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功");
    }

    public RespDTO<List<Goods>> getGoodsList(String ProductCode, String UniqueCode, String CommodityCode, String ProductionBatch) throws Exception{
        String jsonStr = chaincodeService.query("getGoodsList", new String[]{ProductCode, UniqueCode,CommodityCode,ProductionBatch});
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        GoodsDTO goodsDTO = simpleGson.fromJson(jsonStr, GoodsDTO.class);
        return RespDTO.success(goodsDTO.getList());
    }
}
