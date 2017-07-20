package com.oxchains.service;
import com.oxchains.bean.dto.WaybillDTO;
import com.oxchains.common.ChaincodeResp;
import com.oxchains.common.ConstantsData;
import com.oxchains.common.RespDTO;
import com.oxchains.bean.model.ziyun.Waybill;
import com.oxchains.dao.ChaincodeData;
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
    private ChaincodeData chaincodeData;
    public RespDTO<String> add(Waybill waybill) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String txID = chaincodeData.invoke("saveTransferInfo", new String[]{gson.toJson(waybill)})
                .filter(ChaincodeResp::succeeded)
                .map(ChaincodeResp::getPayload)
                .orElse(null);
        log.debug("===txID===" + txID);
        if (txID == null) {
            return RespDTO.fail("操作失败", ConstantsData.RTN_SERVER_INTERNAL_ERROR);
        }
        return RespDTO.success("操作成功", txID);
    }

    public RespDTO<Waybill> getWaybillDataByDocNumOrNoteNum(String number) throws InvalidArgumentException, ProposalException {
        String jsonStr = chaincodeData.query("getTransferInfoByDocNum", new String[]{number})
                .filter(ChaincodeResp::succeeded)
                .map(ChaincodeResp::getPayload)
                .orElse(null);
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            jsonStr = chaincodeData.query("getTransferInfoByNoteNum", new String[]{number})
            .filter(ChaincodeResp::succeeded)
            .map(ChaincodeResp::getPayload)
            .orElse(null);
        }
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        return RespDTO.success(gson.fromJson(jsonStr, Waybill.class));
    }

    public RespDTO<List<Waybill>> getWaybillByID(String uniqueID) throws InvalidArgumentException, ProposalException {
        String jsonStr = chaincodeData.query("getTransferInfoByUniqueID", new String[]{uniqueID})
                .filter(ChaincodeResp::succeeded)
                .map(ChaincodeResp::getPayload)
                .orElse(null);
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        //System.out.println(jsonStr);
        WaybillDTO waybillDTO = simpleGson.fromJson(jsonStr, WaybillDTO.class);
        return RespDTO.success(waybillDTO.getList());
    }

    public RespDTO<List<Waybill>> getByCodeOrProBatch(String code, String proBatch) {
        String jsonStr = chaincodeData.query("getTransferInfoByBatch", new String[]{code, proBatch})
                .filter(ChaincodeResp::succeeded)
                .map(ChaincodeResp::getPayload)
                .orElse(null);
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        WaybillDTO waybillDTO = simpleGson.fromJson(jsonStr, WaybillDTO.class);
        return RespDTO.success(waybillDTO.getList());
    }
}