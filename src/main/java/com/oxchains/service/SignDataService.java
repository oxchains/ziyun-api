package com.oxchains.service;

import com.oxchains.bean.model.ziyun.SignData;
import com.oxchains.common.RespDTO;
import com.oxchains.repository.VerifySignRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.util.SynchronizedSymbolTable;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
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
    @Resource
    private VerifySignRepository verifySignRepository;

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
    public RespDTO<String> getClientSign(SignData signData) throws Exception {
        String dataHash = signData.getDataHash();
        String nonce = System.currentTimeMillis() + "";

        SignData sqlOne = verifySignRepository.findByDataHash(dataHash);
        if(sqlOne != null){
            String sqlNonce = sqlOne.getNonce();
            String chainSignature = chaincodeService.query("getSignature", new String[]{dataHash, "sanxi", sqlNonce});

            if(StringUtils.isBlank(chainSignature)) {
                String signature = chaincodeService.invoke("sign", new String[]{dataHash, "sanxi", nonce},null);
                return RespDTO.success("操作成功", signature);
            }
            return RespDTO.success("操作成功", chainSignature);
        }else {
            String signature = chaincodeService.invoke("sign", new String[]{dataHash, "sanxi", nonce},null);
            signData.setNonce(nonce);
            verifySignRepository.save(signData);
            return RespDTO.success("操作成功", signature);
        }

    }

    /** 将客户端上传的data_hash和签名之后的signature上传给chaincode，然后由chaincode进行验证并返回结果 */
    public RespDTO<Boolean> verifySign(SignData signData) throws Exception {
        String dataHash = signData.getDataHash();
        String signature = signData.getSignature();
        String verify = chaincodeService.query("verify", new String[]{dataHash, signature});
        return RespDTO.success(verify.equals("1") ? "操作成功" : "操作失败", verify.equals("1"));
    }

}
