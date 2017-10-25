package com.oxchains.service;

import com.oxchains.bean.dto.datav.ValueContent;
import com.oxchains.bean.dto.datav.XY;
import com.oxchains.bean.model.DataV;
import com.oxchains.util.RedisUtils;
import com.oxchains.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ScheduledTaskManager {
    @Resource
    private ChaincodeService chaincodeService;

    @Value("${schedule.enabled}")
    private String scheduled;

    /**
     * 启动时执行一次，之后每隔10hour执行一次
     */
    //@Scheduled(fixedRate = 1000*60*60*10)
    //每天凌晨两点执行定时任务，docker与host相差8个小时
    @Scheduled(cron = "0 0 18 * * *")
    public void getDataV() {
        if("false".equals(scheduled)){
            return;
        }
        log.info("===getDataV schedule==="+new Date());
        try{
            DataV.BlockChainInfo.Builder builder = DataV.BlockChainInfo.newBuilder();

            JedisCluster jedisCluster = RedisUtils.getJedisCluster();
            BlockchainInfo blockchainInfo = chaincodeService.queryChain();
            builder.setHeight(blockchainInfo.getHeight());

            long count = 0L;
            for (int i = 1; i < blockchainInfo.getHeight(); i++) {
                BlockInfo blockInfo = chaincodeService.queryBlock(i);
                count += blockInfo.getBlock().getData().getDataCount();
            }
            builder.setTxcount(count);

            int num = 10;
            long startIndex = blockchainInfo.getHeight() - num;
            if (startIndex - 10 < 0) {
                startIndex = 0;
            }
            for (int i = (int) (blockchainInfo.getHeight() - 1); i >= startIndex; i--) {
                BlockInfo blockInfo = chaincodeService.queryBlock(i);
                DataV.XY.Builder xy = DataV.XY.newBuilder();
                xy.setX(i+"");
                xy.setY((long) blockInfo.getBlock().getData().getDataCount());
                builder.addXy(xy);
            }

            int num2 = 24;
            long startIndex2 = blockchainInfo.getHeight() - num2;
            if (startIndex2 - 10 < 0) {
                startIndex2 = 0;
            }
            String contentTmpl = "区块号：#{blockNum}；状态哈希值：#{currentHash}上一区块哈希：#{previousHash}";
            for (int i = (int) (blockchainInfo.getHeight() - 1); i >= startIndex2; i--) {
                BlockInfo blockInfo = chaincodeService.queryBlock(i);
                String content = contentTmpl.replace("#{blockNum}", blockInfo.getBlockNumber() + "")
                        .replace("#{currentHash}", Hex.encodeHexString(blockInfo.getBlock().getHeader().getDataHash().toByteArray()))
                        .replace("#{previousHash}", Hex.encodeHexString(blockInfo.getPreviousHash()));
                DataV.ValueContent.Builder valueContent = DataV.ValueContent.newBuilder();
                valueContent.setContent(content);
                valueContent.setValue("100");
                builder.addValuecontent(valueContent);
            }

            jedisCluster.set("chianinfo".getBytes(),builder.build().toByteArray());

        }catch(Exception e){
            log.error("getDataV error",e);
        }

    }
}
