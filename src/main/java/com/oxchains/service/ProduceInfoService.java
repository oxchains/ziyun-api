package com.oxchains.service;

import com.oxchains.bean.dto.ProduceInfoDTO;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.common.RespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生产信息Service
 * Created by Luo_xuri on 2017/7/5.
 */
@Service
@Slf4j
public class ProduceInfoService extends BaseService {

    @Resource
    private ChaincodeService chaincodeService;

    public RespDTO<List<ProduceInfo>> getProduceInfoList(String id) {
        String jsonStr = chaincodeService.query("getProduceInfoList", new String[]{id});
        System.err.println("-->生产信息JSON：" + jsonStr);
        if (StringUtils.isEmpty(jsonStr)){
            return RespDTO.fail("没有数据");
        }
        ProduceInfoDTO produceInfoDTO = simpleGson.fromJson(jsonStr, ProduceInfoDTO.class);
        System.err.println("-->生产信息集合：" + produceInfoDTO.getList());
        return RespDTO.success(produceInfoDTO.getList());
    }
}
