package com.oxchains.service;

import com.google.common.cache.*;
import com.google.protobuf.InvalidProtocolBufferException;
import com.oxchains.bean.dto.datav.NameValue;
import com.oxchains.bean.dto.datav.ValueContent;
import com.oxchains.bean.dto.datav.XY;
import com.oxchains.util.RedisUtils;
import com.oxchains.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

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

    JedisCluster jedisCluster = RedisUtils.getJedisCluster();

    public NameValue<Long> getChainHeight() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        return new NameValue<>("", getBlockChainHeight());
    }

    public NameValue<Long> getChainTxCount() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getChainTxCount";
        Long result = Long.valueOf(jedisCluster.get(key));

        return new NameValue<>("", result);
    }

    /**
     * 查询最近十天的交易量
     * @return
     */
    public List<XY> getChainTxNum() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getChainTxNum";
        return (List<XY>)SerializeUtil.unSerializeList(key.getBytes());
    }

    public List<ValueContent> getChainNewBlock() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getChainNewBlock";
        return (List<ValueContent>)SerializeUtil.unSerializeList(key.getBytes());
    }

    private long getBlockChainHeight() throws InvalidProtocolBufferException, ProposalException, InvalidArgumentException, ExecutionException {
        String key = "getBlockChain";
        return Long.valueOf(jedisCluster.get(key));
    }


}
