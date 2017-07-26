package com.oxchains.service;

import com.google.common.cache.*;
import com.google.protobuf.InvalidProtocolBufferException;
import com.oxchains.bean.dto.datav.NameValue;
import com.oxchains.bean.dto.datav.ValueContent;
import com.oxchains.bean.dto.datav.XY;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * DataVService
 *
 * @author liuruichao
 * Created on 2017/4/18 17:31
 */
@Service
@Slf4j
public class DataVService extends BaseService {
    @Resource
    private ChaincodeService chaincodeService;

    private LoadingCache<String, BlockchainInfo> cache = CacheBuilder
            .newBuilder()
            .maximumSize(10)
            .refreshAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, BlockchainInfo>() {
                @Override
                public BlockchainInfo load(String key) throws Exception {
                    BlockchainInfo blockchainInfo = chaincodeService.queryChain();
                    return blockchainInfo;
                }
            });

    private LoadingCache<String, Long> cache2 = CacheBuilder
            .newBuilder()
            .maximumSize(10)
            .refreshAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, Long>() {
                @Override
                public Long load(String key) throws Exception {
                    BlockchainInfo blockchainInfo = getBlockChain();
                    long count = 0L;
                    for (int i = 1; i < blockchainInfo.getHeight(); i++) {
                        BlockInfo blockInfo = chaincodeService.queryBlock(i);
                        count += blockInfo.getBlock().getData().getDataCount();
                    }
                    return count;
                }
            });

    private LoadingCache<String, List<XY>> cache3 = CacheBuilder
            .newBuilder()
            .maximumSize(10)
            .refreshAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, List<XY>>() {
                @Override
                public List<XY> load(String key) throws Exception {
                    int num = 10;
                    List<XY> list = new ArrayList<>(num);

                    BlockchainInfo blockchainInfo = getBlockChain();
                    long startIndex = blockchainInfo.getHeight() - num;
                    if (startIndex - 10 < 0) {
                        startIndex = 0;
                    }
                    for (int i = (int) (blockchainInfo.getHeight() - 1); i >= startIndex; i--) {
                        BlockInfo blockInfo = chaincodeService.queryBlock(i);
                        list.add(new XY(i + "", (long) blockInfo.getBlock().getData().getDataCount()));
                    }
                    return list;
                }
            });

    private LoadingCache<String, List<ValueContent>> cache4 = CacheBuilder
            .newBuilder()
            .maximumSize(10)
            .refreshAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, List<ValueContent>>() {
                @Override
                public List<ValueContent> load(String key) throws Exception {
                    int num = 24;
                    List<ValueContent> list = new ArrayList<>(num);

                    BlockchainInfo blockchainInfo = getBlockChain();
                    long startIndex = blockchainInfo.getHeight() - num;
                    if (startIndex - 10 < 0) {
                        startIndex = 0;
                    }
                    String contentTmpl = "区块号：#{blockNum}；状态哈希值：#{currentHash}上一区块哈希：#{previousHash}";
                    for (int i = (int) (blockchainInfo.getHeight() - 1); i >= startIndex; i--) {
                        BlockInfo blockInfo = chaincodeService.queryBlock(i);

                        String content = contentTmpl.replace("#{blockNum}", blockInfo.getBlockNumber() + "")
                                .replace("#{currentHash}", Hex.encodeHexString(blockInfo.getBlock().getHeader().getDataHash().toByteArray()))
                                .replace("#{previousHash}", Hex.encodeHexString(blockInfo.getPreviousHash()));
                        list.add(new ValueContent("100", content));
                    }
                    return list;
                }
            });

    public NameValue<Long> getChainHeight() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        BlockchainInfo blockchainInfo = getBlockChain();
        return new NameValue<>("", blockchainInfo.getHeight());
    }

    public NameValue<Long> getChainTxCount() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getChainTxCount";
        Long result = cache2.get(key);

        return new NameValue<>("", result);
    }

    /**
     * 查询最近十天的交易量
     * @return
     */
    public List<XY> getChainTxNum() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getChainTxNum";
        return cache3.get(key);
    }

    public List<ValueContent> getChainNewBlock() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getChainNewBlock";

        return cache4.get(key);
    }

    private BlockchainInfo getBlockChain() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getBlockChain";
        return cache.get(key);
    }
}