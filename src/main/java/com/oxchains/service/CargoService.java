package com.oxchains.service;

import com.google.gson.reflect.TypeToken;
import com.oxchains.bean.dto.CargoDTO;
import com.oxchains.common.RespDTO;
import com.oxchains.bean.model.ziyun.Cargo;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 货物service
 *
 * @author liuruichao
 * Created on 2017/4/6 14:53
 */
@Service
@Slf4j
public class CargoService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    public boolean instantiateChaincode() {
        try {
            chaincodeService.installChaincode();
        } catch (InvalidArgumentException | ProposalException e) {
            e.printStackTrace();
        }
        try {
            chaincodeService.instantiateChaincode();
        } catch (IOException | ProposalException | InvalidArgumentException | ExecutionException | InterruptedException | ChaincodeEndorsementPolicyParseException | TimeoutException e) {
            e.printStackTrace();
        }
        return true;
    }

    public RespDTO<String> addCargo(Cargo cargo) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String txID = chaincodeService.invoke("saveProductInfo", new String[] { gson.toJson(cargo) });
        return RespDTO.success("提交成功", txID);
    }

    public RespDTO<Cargo> getCargo(String code) throws InvalidArgumentException, ProposalException {
        String jsonStr = chaincodeService.query("getProductInfo", new String[] { code });
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        return RespDTO.success(gson.fromJson(jsonStr, Cargo.class));
    }

    public RespDTO<List<Cargo>> getBatchProductInfo(String ndcNumber, String productionBatch) throws InvalidArgumentException, ProposalException {
        String jsonStr = chaincodeService.query("getBatchProductInfo", new String[] { ndcNumber, productionBatch });
        if (StringUtils.isEmpty(jsonStr)) {
            return RespDTO.fail("没有数据");
        }
        CargoDTO cargoDTO = simpleGson.fromJson(jsonStr, CargoDTO.class);
        return RespDTO.success(cargoDTO.getList());
    }
}