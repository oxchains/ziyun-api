package com.oxchains.controller;

import com.oxchains.common.RespDTO;
import com.oxchains.model.ziyun.Cargo;
import com.oxchains.service.CargoService;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


/**
 * DrugController
 *
 * @author liuruichao
 * Created on 2017/4/6 11:42
 */
@RestController
@RequestMapping("/cargo")
@Slf4j
public class CargoController extends BaseController {
    @Resource
    private CargoService cargoService;

    @RequestMapping("/install")
    public RespDTO<Boolean> install() {
        try {
            return RespDTO.success(cargoService.instantiateChaincode());
        } catch (ProposalException | InvalidArgumentException | InterruptedException | TimeoutException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
        return RespDTO.fail();
    }

    @RequestMapping(method = RequestMethod.GET)
    public RespDTO<String> query(@RequestParam String code) {
        try {
            return cargoService.getCargo(code);
        } catch (InvalidArgumentException | ProposalException e) {
            e.printStackTrace();
        }
        return RespDTO.fail();
    }


    @RequestMapping(method = RequestMethod.POST)
    public RespDTO<String> add(@RequestBody String body) {
        try {
            Cargo cargo = gson.fromJson(body, Cargo.class);
            return cargoService.addCargo(cargo);
        } catch (Exception e) {
            log.error("add cargo error!", e);
        }
        return RespDTO.fail("系统繁忙，请稍后再试!");
    }
}