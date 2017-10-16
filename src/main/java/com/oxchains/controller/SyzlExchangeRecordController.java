package com.oxchains.controller;

import com.oxchains.bean.model.ziyun.SyzlExchangeRecord;
import com.oxchains.common.RespDTO;
import com.oxchains.service.SyzlExchangeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by root on 17-10-10.
 */
@RestController
@RequestMapping("/syzlExchangeRecord")
@Slf4j
public class SyzlExchangeRecordController extends BaseController{

    @Resource
    private SyzlExchangeRecordService syzlExchangeRecordService;

    @PostMapping
    public RespDTO<String> addSyzlExchangeRecord(@RequestBody String body, @RequestParam String Token) {
        try {
            log.info("===addSyzlExchangeRecord==="+body);
            SyzlExchangeRecord syzlExchangeRecord = gson.fromJson(body,SyzlExchangeRecord.class);
            syzlExchangeRecord.setToken(Token);
            return syzlExchangeRecordService.addSyzlExchangeRecord(syzlExchangeRecord);
        } catch (Exception e) {
            log.error("add syzlExchangeRecord error: ", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/{SendEnterpriseName}", method = RequestMethod.GET)
    public RespDTO<List<SyzlExchangeRecord>> getSyzlExchangeRecord(@PathVariable String SendEnterpriseName, @RequestParam String Token) {
        try {
            log.info("===getSyzlExchangeRecord===");
            return syzlExchangeRecordService.getSyzlExchangeRecord(SendEnterpriseName, Token);
        } catch (Exception e) {
            log.error("getSyzlExchangeRecord error: ", e);
        }
        return RespDTO.fail();
    }
}
