package com.oxchains.service;

import com.oxchains.bean.dto.datav.ValueContent;
import com.oxchains.bean.dto.datav.XY;
import com.oxchains.util.RedisUtils;
import com.oxchains.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
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
    /**
     * 每天凌晨 02:00 执行一次
     */
    @Scheduled(cron = "0 44 11 * * *")
    public void autoCardCalculate() {
        log.info("执行定时任务... " + new Date());
    }

    /**
     * 心跳更新。启动时执行一次，之后每隔1分钟执行一次
     */
    @Scheduled(fixedRate = 1000*60*60*24)
    public void getDataV() {
        log.info("===getDataV schedule==="+new Date());
        try{
            JedisCluster jedisCluster = RedisUtils.getJedisCluster();
            BlockchainInfo blockchainInfo = chaincodeService.queryChain();
            jedisCluster.set("getBlockChain",String.valueOf(blockchainInfo.getHeight()));

            long count = 0L;
            for (int i = 1; i < blockchainInfo.getHeight(); i++) {
                BlockInfo blockInfo = chaincodeService.queryBlock(i);
                count += blockInfo.getBlock().getData().getDataCount();
            }
            jedisCluster.set("getChainTxCount",String.valueOf(count));

            int num = 10;
            List<XY> list = new ArrayList<>(num);

            long startIndex = blockchainInfo.getHeight() - num;
            if (startIndex - 10 < 0) {
                startIndex = 0;
            }
            for (int i = (int) (blockchainInfo.getHeight() - 1); i >= startIndex; i--) {
                BlockInfo blockInfo = chaincodeService.queryBlock(i);
                list.add(new XY(i + "", (long) blockInfo.getBlock().getData().getDataCount()));
            }
            jedisCluster.set("getChainTxNum".getBytes(), SerializeUtil.serializeList(list));

            int num2 = 24;
            List<ValueContent> list2 = new ArrayList<>(num2);

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
                list2.add(new ValueContent("100", content));
            }
            jedisCluster.set("getChainNewBlock".getBytes(),SerializeUtil.serializeList(list2));

        }catch(Exception e){
            log.error("getDataV error",e);
        }

    }
}
