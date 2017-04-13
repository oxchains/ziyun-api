package com.oxchains.controller;

import com.oxchains.common.RespDTO;
import com.oxchains.bean.model.ziyun.Waybill;
import com.oxchains.service.WaybillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * WaybillController
 *
 * @author liuruichao
 * Created on 2017/4/6 16:52
 */
@RestController
@RequestMapping("/waybill")
@Slf4j
public class WaybillController extends BaseController {
    @Resource
    private WaybillService waybillService;

    @RequestMapping(method = RequestMethod.POST)
    public RespDTO<String> add(@RequestBody String body) {
        try {
            Waybill waybill = gson.fromJson(body, Waybill.class);
            return waybillService.add(waybill);
        } catch (Exception e) {
            log.error("add waybill error!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/docNumOrNoteNum", method = RequestMethod.GET)
    public RespDTO<Waybill> queryDocNumOrNoteNum(@RequestParam String number) {
        try {
            return waybillService.getWaybillDataByDocNumOrNoteNum(number);
        } catch (Exception e) {
            log.error("query error!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/codeAndProBatch", method = RequestMethod.GET)
    public RespDTO<List<Waybill>> queryCodeAndProBatch(@RequestParam String commoditycode, @RequestParam String productionBatch) {
        try {
            return waybillService.getByCodeOrProBatch(commoditycode, productionBatch);
        } catch (Exception e) {
            log.error("query error!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/{uniqueID}", method = RequestMethod.GET)
    public RespDTO<List<Waybill>> queryUnique(@PathVariable String uniqueID) {
        try {
            return waybillService.getWaybillByID(uniqueID);
        } catch (Exception e) {
            log.error("query uniqueID error!", e);
        }
        return RespDTO.fail();
    }
}
