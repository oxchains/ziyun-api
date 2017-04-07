package com.oxchains.service;

import com.oxchains.bean.dto.WaybillDTO;
import com.oxchains.common.RespDTO;
import com.oxchains.bean.model.ziyun.Waybill;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

    public RespDTO<String> add(Waybill waybill) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String txID = chaincodeService.invoke("saveTransferInfo", new String[]{gson.toJson(waybill)});
        return RespDTO.success("操作成功", txID);
    }

    public RespDTO<Waybill> getWaybillDataByDocNumOrNoteNum(String number) throws InvalidArgumentException, ProposalException {
        String jsonStr = chaincodeService.query("getTransferInfoByDocNum", new String[]{number});
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            jsonStr = chaincodeService.query("getTransferInfoByNoteNum", new String[]{number});
        }
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        return RespDTO.success(gson.fromJson(jsonStr, Waybill.class));
    }

    public RespDTO<List<Waybill>> getWaybillByID(String uniqueID) throws InvalidArgumentException, ProposalException {
        String jsonStr = chaincodeService.query("getTransferInfoByUniqueID", new String[]{uniqueID});
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        WaybillDTO waybillDTO = simpleGson.fromJson(jsonStr, WaybillDTO.class);
        return RespDTO.success(waybillDTO.getList());
    }
}
