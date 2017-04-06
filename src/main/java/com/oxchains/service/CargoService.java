package com.oxchains.service;

import com.oxchains.common.RespDTO;
import com.oxchains.model.ziyun.Cargo;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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

    public boolean instantiateChaincode() throws ProposalException, InvalidArgumentException, InterruptedException, ExecutionException, TimeoutException, IOException {
        chaincodeService.installChaincode();
        chaincodeService.instantiateChaincode();
        return true;
    }

    public RespDTO<String> addCargo(Cargo cargo) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String txID = chaincodeService.invoke("saveProductInfo", new String[] { gson.toJson(cargo) });
        return RespDTO.success("提交成功", txID);
    }

    public RespDTO<String> getCargo(String code) throws InvalidArgumentException, ProposalException {
        return RespDTO.success("提交成功", chaincodeService.query("getProductInfo", new String[] { code }));
    }
}