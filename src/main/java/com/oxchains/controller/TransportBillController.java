package com.oxchains.controller;

import com.oxchains.common.RespDTO;
import com.oxchains.service.TransportBillService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by root on 17-7-3.
 */
@RestController
@RequestMapping("/transportbill")
public class TransportBillController extends BaseController{
    @Resource
    private TransportBillService transportBillService;

}
