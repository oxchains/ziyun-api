package com.oxchains.service;

import com.oxchains.common.RespDTO;
import com.oxchains.model.ziyun.Waybill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * WaybillService
 *
 * @author liuruichao
 * Created on 2017/4/6 16:53
 */
@Service
@Slf4j
public class WaybillService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<String> add(Waybill waybill) {
        try {
            String txID = chaincodeService.invoke("saveTransferInfo", new String[] { gson.toJson(waybill) });
            return RespDTO.success("操作成功", txID);
        } catch (Exception e) {
            log.error("addWaybill error!", e);
        }
        return RespDTO.fail();
    }
}
