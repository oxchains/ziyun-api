package com.oxchains.controller;

import com.oxchains.bean.dto.datav.NameValue;
import com.oxchains.bean.dto.datav.ValueContent;
import com.oxchains.bean.dto.datav.XY;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.DataVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * DataVController
 *
 * @author liuruichao
 * Created on 2017/4/18 12:36
 */
@RestController
@RequestMapping("/datav")
@Slf4j
public class DataVController extends BaseController {
    @Resource
    private DataVService dataVService;

    @Resource
    private ChaincodeService chaincodeService;

    @RequestMapping(method = RequestMethod.GET)
    public String test() {
        try {
            //chaincodeService.queryChain();
            //System.out.println(chaincodeService.getChannels());
            chaincodeService.getInstalledChaincodes();
        } catch (Exception e) {
            log.error("test error!", e);
        }
        return "success";
    }

    @RequestMapping(value = "/chain/height", method = RequestMethod.GET)
    public List<NameValue<Long>> chainHeight() {
        List<NameValue<Long>> list = new ArrayList<>(1);
        NameValue<Long> nameValue = null;
        try {
            nameValue = dataVService.getChainHeight();
        } catch (Exception e) {
            log.error("chainHeight error!", e);
            nameValue = new NameValue<>("", 1L);
        }
        list.add(nameValue);
        return list;
    }

    @RequestMapping(value = "/chain/txCount", method = RequestMethod.GET)
    public List<NameValue<Long>> chainTxCount() {
        List<NameValue<Long>> list = new ArrayList<>(1);
        NameValue<Long> nameValue = null;
        try {
            nameValue = dataVService.getChainTxCount();
        } catch (Exception e) {
            log.error("chainTxCount error!", e);
            nameValue = new NameValue<>("", 1L);
        }
        list.add(nameValue);
        return list;
    }

    @RequestMapping(value = "/chain/txNum", method = RequestMethod.GET)
    public List<XY> chainTxNum() {
        List<XY> list = null;
        try {
            list = dataVService.getChainTxNum();
        } catch (Exception e) {
            log.error("chainTxNum error!", e);
        }
        return list;
    }

    @RequestMapping(value = "/chain/new", method = RequestMethod.GET)
    public List<ValueContent> chainNewBlock() {
        List<ValueContent> list = null;
        try {
            list = dataVService.getChainNewBlock();
        } catch (Exception e) {
            log.error("chainNewBlock error!", e);
        }
        return list;
    }
}
