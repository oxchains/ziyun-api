package com.oxchains.controller;

import com.oxchains.common.RespDTO;
import com.oxchains.model.ziyun.Waybill;
import com.oxchains.service.WaybillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
        Waybill waybill = gson.fromJson(body, Waybill.class);
        return waybillService.add(waybill);
    }
}
