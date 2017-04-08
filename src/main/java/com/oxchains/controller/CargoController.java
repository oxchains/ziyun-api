package com.oxchains.controller;

import com.oxchains.common.RespDTO;
import com.oxchains.bean.model.ziyun.Cargo;
import com.oxchains.service.CargoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


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
        } catch (Exception e) {
            log.error("install error!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public RespDTO<Cargo> getCargoInfo(@PathVariable String code) {
        try {
            return cargoService.getCargo(code);
        } catch (Exception e) {
            log.error("getCargoInfo erro!", e);
        }
        return RespDTO.fail();
    }

    @RequestMapping(method = RequestMethod.GET)
    public RespDTO<List<Cargo>> query(@RequestParam String NDCNumber, @RequestParam String productionBatch) {
        try {
            return cargoService.getBatchProductInfo(NDCNumber, productionBatch);
        } catch (Exception e) {
            log.error(String.format("query error! NDCNumber: %s, ProductionBatch: %s.", NDCNumber, productionBatch), e);
        }
        return RespDTO.fail();
    }


    @RequestMapping(method = RequestMethod.POST)
    public RespDTO<String> add(@RequestBody String body) {
        try {
            Cargo cargo = gson.fromJson(body, Cargo.class);
            System.out.println(cargo);
            return cargoService.addCargo(cargo);
        } catch (Exception e) {
            log.error("add cargo error!", e);
        }
        return RespDTO.fail();
    }
}