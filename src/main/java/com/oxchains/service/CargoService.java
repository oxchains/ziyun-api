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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

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

    private AtomicInteger total = new AtomicInteger(0);

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

    public RespDTO<String> addCargo(Cargo cargo) throws InterruptedException, InvalidArgumentException, TimeoutException, ProposalException, ExecutionException, BrokenBarrierException {
        String str = "{\n" +
                "\"GoodsType\": \"drug\", \n" +
                "\"DrugElectronicSupervisionCode\": \"0123456789\", \n" +
                "\"DrugInformation\": {\n" +
                "\"DrugName\": \"活血风湿膏\", \n" +
                "\"ApprovalNumber\": \"ZC20150005\", \n" +
                "\"Size\": \"10cmX15cm\", \n" +
                "\"Form\": \"贴膏剂\", \n" +
                "\"Manufacturer\": \"得生制药股份有限公司\", \n" +
                "\"NDCNumber\": \"86978998000169\", \n" +
                "\"NDCNumberRemark\": \"无\", \n" +
                "\"MedicineInstruction\": \"~~\" \n" +
                "},\n" +
                "\"ProduceInformation\": {\n" +
                "\"Address\": \"台南市永康区环工路42号\", \n" +
                "\"ProductionBatch\": \"abc123456\", \n" +
                "\"ProductionTime\": 1490155871000, \n" +
                "\"ValidDate\": 1553212800000 \n" +
                "}\n" +
                "}";
        String json = str.replaceAll(" |\n|\t", "");

        int num = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(num + 1);

        String txID = null;
        long start = System.currentTimeMillis();
        for (int i = 0 ; i < num; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    try {
                        long start1 = System.currentTimeMillis();
                        String txID2 = chaincodeService.invoke("saveProductInfo", new String[]{json});
                        long end1 = System.currentTimeMillis();
                        System.out.println("peer time: " + (end1 - start1));
                    } catch (InvalidArgumentException | ProposalException | InterruptedException | ExecutionException | TimeoutException ignored) {
                    }
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        cyclicBarrier.await();
        long end = System.currentTimeMillis();

        return RespDTO.success("提交成功", (end - start) + "");
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