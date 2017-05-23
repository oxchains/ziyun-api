package com.oxchains.controller;

import com.oxchains.service.ChaincodeService;
import com.oxchains.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Set;

/**
 * ChannelController
 *
 * @author liuruichao
 * Created on 2017/5/4 13:21
 */
@RestController
@RequestMapping("/channel")
@Slf4j
public class ChannelController extends BaseController {
    @Resource
    private ChannelService channelService;

    @Resource
    private ChaincodeService chaincodeService;

    /*@GetMapping(value = "/create/{channelName}")
    public String createChannel(@PathVariable String channelName) throws TransactionException, InvalidArgumentException {
        channelService.createChannel(channelName);
        return channelName;
    }*/

    @GetMapping(value = "/list")
    public Set<String> channelList() throws IOException, InvalidArgumentException {
        try {
            return chaincodeService.getChannels();
        } catch (ProposalException e) {
            log.error("channelList error!", e);
        }
        return null;
    }
}
