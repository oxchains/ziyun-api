package com.oxchains.service;

import com.oxchains.common.RespDTO;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Luo_xuri on 2017/6/20.
 */
@Service
@Slf4j
public class SignDataService extends BaseService {

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

    /** 返回-> 客户端上传的签名文件的hash值的签名之后的hash值 */
    public RespDTO<String> getClientSign(String dataHash) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException {
        String signature = chaincodeService.invoke("sign", new String[]{dataHash, "sanxi", System.currentTimeMillis()+""},null);
        // response.getProposalResponse().getResponse().getPayload().toStringUtf8()
        return RespDTO.success("操作成功", signature);
    }

    /** 将客户端上传的data_hash和签名之后的signature上传给chaincode，然后由chaincode进行验证并返回结果 */
    public RespDTO<Boolean> verifySign(String dataHash, String signature) {
        String  verify = chaincodeService.query("verify", new String[]{dataHash, signature});
        return RespDTO.success(verify.equals("1"));
    }



}
