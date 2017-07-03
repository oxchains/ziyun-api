package com.oxchains.scheduler;

import com.oxchains.grpc.HelloClient;
import com.oxchains.service.ChaincodeService;
import com.oxchains.service.DataVService;
import com.oxchains.service.influxDB.InfluxService;
import com.oxchains.util.EmailUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClientBuilder;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    @Autowired
    private EmailUtil mailService;

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
        int port = Integer.parseInt(ORDERER_URL.substring(ORDERER_URL.lastIndexOf(":")+1));
        String host = ORDERER_URL.substring(7,ORDERER_URL.lastIndexOf(":"));
        boolean status = new HelloClient(host, port).greet("");
        if(status)
            influxService.write(Point.measurement("status").tag("host", "orderer").addField("value", 1).build());
        else
            influxService.write(Point.measurement("status").tag("host", "orderer").addField("value", 2).build());
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

    @Scheduled(fixedDelayString = "${SCHEDULED_TASKS_DELAY:600000}")
//    每十分钟扫描下 最近的服务器状态 如果宕机发出邮件
    public void mailAdvice() {
        boolean isDown = false;
        Object status;
        String[] sqls = {
                "select last(value) from status where host = 'ca0' and time > now()-600s",
                "select last(value) from status where host = 'orderer' and time > now()-600s",
                "select last(value) from status where host = 'peer0' and time > now()-600s",
                "select last(value) from status where host = 'peer1' and time > now()-600s"
        };
        String msg = "";
        for(String sql : sqls){
            status = influxService.query(sql).get().getResults().get(0).getSeries().get(0).getValues().get(0).get(1);
            if(status == null || (int)status == 2){
                isDown = true;
                msg += sql.substring(sql.indexOf("'")+1, sql.lastIndexOf("'"))+ " ";
            }
        }
        if(isDown)
            mailService.sendSimpleMail("monitor@oxchains.com","紫云区块链系统","系统服务器"+msg+"宕机提醒");
    }
}


