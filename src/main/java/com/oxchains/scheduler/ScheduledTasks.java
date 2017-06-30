package com.oxchains.scheduler;

import com.oxchains.service.ChaincodeService;
import com.oxchains.service.DataVService;
import com.oxchains.service.influxDB.InfluxService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CompletableFuture;

@Component
public class ScheduledTasks {

    @Autowired
    DataVService dataVService;
    @Autowired
    ChaincodeService chaincodeService;

    @Value("${chaincode.peer.address.list}")
    private String PEER_LIST;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private InfluxService influxService;

    @Value("${chaincode.ca.url}")
    private String CA_URL;

    @Value("${chaincode.orderer.url}")
    private String ORDERER_URL;

    private static int ordererStatus = 1; //1--up 2--down

    public static void setOrderStatus(int status) {
        ScheduledTasks.ordererStatus = status;
    }

    @Scheduled(fixedDelayString = "${SCHEDULED_TASKS_DELAY:2000}")
    public void chainHeightAndTxCount() {
        try {
            long height = dataVService.getChainHeight().getValue();
            log.info("height:" + height);
            Double storedHeight = 0.0;
            try{
                storedHeight = (Double) influxService.query("select last(value) from height").get().getResults().get(0).getSeries().get(0).getValues().get(0).get(1);
            }catch(Exception e){}
            long blocNumBeg = storedHeight.longValue();//blockNum-0对应height-1
            long heightTxCount;
            for(;blocNumBeg < height; blocNumBeg++){
                heightTxCount = dataVService.getChainTxCountByBlockNum(blocNumBeg).getValue();
                log.info("heightTxCount:"+heightTxCount);
                influxService.write(Point.measurement("txCount").tag("blockNum", String.valueOf(blocNumBeg)).addField("value", heightTxCount).build());
            }
            influxService.write(Point.measurement("height").addField("value", height).build());
//            influxService.write(Point.measurement("txCount"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(fixedDelayString = "${SCHEDULED_TASKS_DELAY:10000}")
    public void tenNewestBlocks() {
        try {
            long height = dataVService.getChainHeight().getValue();

            long blocNumBeg = height>9?height-10:0;
            long heightTxCount;
            influxService.query("drop measurement newBlocks");
            for(;blocNumBeg < height; blocNumBeg++){
                heightTxCount = dataVService.getChainTxCountByBlockNum(blocNumBeg).getValue();
                influxService.write(Point.measurement("newBlocks").tag("blockNum", String.valueOf(blocNumBeg)).addField("value", String.valueOf(heightTxCount)).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Scheduled(fixedDelayString = "${SCHEDULED_TASKS_DELAY:10000}")
    public void peerStatusCheck() {
        String[] peerAddressList = PEER_LIST.split(",");
        int index = 0;
        int status;
        for (String address : peerAddressList) {
            String[] params = address.split("@");
            status = chaincodeService.checkPeerStatus(index)?1:2;
            influxService.write(Point.measurement("status").tag("host", params[0]).addField("value", status).build());
            index++;
        }
    }

    @Scheduled(fixedDelayString = "${SCHEDULED_TASKS_DELAY:10000}")
    public void orderStatusCheck() {
        try {
            final int[] status = {1};
            int port = Integer.parseInt(ORDERER_URL.substring(ORDERER_URL.lastIndexOf(":")+1));
            CompletableFuture<BlockEvent.TransactionEvent> completableFuture = chaincodeService.invokeNonExistFunc();
            completableFuture.exceptionally(throwable -> {
                        System.out.println(throwable.getMessage());
                        if (throwable.getMessage().contains("failed to place transaction")){
                            log.info("----------2 failed to place transaction");
                            influxService.write(Point.measurement("status").tag("host", "orderer").addField("value", 2).build());
                        }
                        else {
                            //如果peer宕机会导致背书失效 不会提交orderer从而无法检验orderer是否宕机,由此结合端口占用情况判断,如果没有占用 证明系统宕机
                            try {
                                //TODO 有时候端口会available ...
                                ServerSocket server = new ServerSocket(port);
                                log.info("-------------2 The port is available.");
                                status[0] = 2;
                            } catch (IOException e) {
                                log.info("-------------1 The port is occupied.");
                                status[0] = 1;
                            } finally {
                                influxService.write(Point.measurement("status").tag("host", "orderer").addField("value", status[0]).build());
                            }
                        }
                        return null;
                    });
            if (!completableFuture.isCompletedExceptionally()){
                //如果正常结束 状态1
                influxService.write(Point.measurement("status").tag("host", "orderer").addField("value", 1).build());
                log.info("----------1 is not CompletedExceptionally");
            }
        } catch (ProposalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelayString = "${SCHEDULED_TASKS_DELAY:2000}")
    public void caStatusCheck() {
        //通过ca的rest接口 检验服务是否正常
        HttpPost httpPost = new HttpPost(CA_URL);
        HttpResponse response = null;
        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        HttpClient client = httpClientBuilder.build();
        final HttpClientContext context = HttpClientContext.create();
        try {
            response = client.execute(httpPost, context);
            int status = response.getStatusLine().getStatusCode();
            influxService.write(Point.measurement("status").tag("host", "ca0").addField("value", 1).build());
        } catch (IOException e) {
            log.error(e.getMessage());
            influxService.write(Point.measurement("status").tag("host", "ca0").addField("value", 2).build());
        }
    }
}
