package com.oxchains.service;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.common.Configtx;
import org.hyperledger.fabric.protos.common.Policies;
import org.hyperledger.fabric.protos.orderer.Ab;
import org.hyperledger.fabric.protos.orderer.Configuration;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.transaction.ProtoUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * ChannelService
 *
 * @author liuruichao
 * Created on 2017/5/4 13:22
 */
@Service
@Slf4j
public class ChannelService extends BaseService {
    public final int VERSION = 0;
    @Resource
    private ChaincodeService chaincodeService;

    /*public String createChannel(String channelName) {
//        Map<String, MSP> msps = new HashMap<>();
//        msps.put("OrdererMSP",loadMSPConfig("OrdererMSP", "orderer", "ordererOrg1"));
//        msps.put("Org1MSP", loadMSPConfig("Org1MSP", "peer", "peerOrg1"));
//        msps.put("Org2MSP", loadMSPConfig("Org2MSP", "peer", "peerOrg2"));

        String txId = channelName + UUID.randomUUID().toString().replace("-", "");

        Configtx.ConfigUpdateEnvelope configUpdateEnvelope = null;
        try {
            configUpdateEnvelope = buildConfigUpdateEnvelope(channelName);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        byte[] nonce = new byte[24];
        new Random().nextBytes(nonce);
        HFClient hfClient = chaincodeService.getHfClient();
        CryptoSuite crypto = hfClient.getCryptoSuite();
        Enrollment enrollment = hfClient.getUserContext().getEnrollment();
        //TODO txid
        Common.ChannelHeader channelHeader = ProtoUtils.createChannelHeader(Common.HeaderType.CONFIG_UPDATE, txId, channelName, 0, null);
        Common.Header header = null;
        try {
            header = Common.Header.newBuilder().
                    setChannelHeader(channelHeader.toByteString()).
                    setSignatureHeader(ByteString.copyFrom(crypto.sign(enrollment.getKey(), channelHeader.toByteArray()))).build();
        } catch (CryptoException e) {
            e.printStackTrace();
        }
        Common.Payload payload = Common.Payload.newBuilder().setData(configUpdateEnvelope.toByteString()).setHeader(header).build();
        byte[] signature = null;
        try {
            signature = crypto.sign(enrollment.getKey(), payload.toByteArray());
        } catch (CryptoException e) {
            e.printStackTrace();
        }

        //Common.Envelope envelope = Common.Envelope.newBuilder().setPayload(payload.toByteString()).setSignature(ByteString.copyFrom(hfClient.getUserContext().getEnrollment().getCert().getBytes())).build();
        Common.Envelope envelope = Common.Envelope.newBuilder().setPayload(payload.toByteString()).setSignature(ByteString.copyFrom(signature)).build();
        Ab.BroadcastResponse trxResult = null;
        try {
            //chaincodeService.getChain().sendTransaction(e, Lists.newArrayList(chaincodeService.getOrderer()));
            trxResult = chaincodeService.getOrderer().sendTransaction(envelope);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
//        hfClient.
        log.info(String.valueOf(trxResult.getStatusValue()));
        return channelName;
    }*/

    private Configtx.ConfigUpdate buildConfigUpdate(String channelName){
        //configUpdate readSet writeSet
        Configtx.ConfigGroup configGroupR = Configtx.ConfigGroup.newBuilder().setVersion(VERSION).build();

        /*settings : {
            'batch-size' : {'max-message-count' : 10, 'absolute-max-bytes' : '99m',	'preferred-max-bytes' : '512k'},
            'batch-timeout' : '10s',
                    'hashing-algorithm' : 'SHA256',
                    'consensus-type' : 'solo',
                    'creation-policy' : 'AcceptAllPolicy'
        },*/
        Configtx.ConfigGroup.Builder configGroupWBuilder = Configtx.ConfigGroup.newBuilder()
                .putValues("HashingAlgorithm",buildConfigValue("SHA256"))
                .putValues("BlockDataHashingStructure",buildConfigValue("" + (Math.pow(2, 32)-1)))
                //.putGroups("Orderer", buildOrderConfigGroup())
                .putGroups("Application", buildApplicationConfigGroup())
                .putValues("OrdererAddresses", buildConfigValue(org.hyperledger.fabric.protos.common.Configuration.OrdererAddresses.newBuilder().addAddresses("orderer0:7050").build().toByteString()))
                .setModPolicy("Admins")
                .setVersion(VERSION);

        Map<String, String> channelPoliciesMap = new HashMap<>();
        /*policies : {
            Readers : {threshold : 'ANY'},
            Writers : {threshold : 'ANY'},
            Admins  : {threshold : 'ANY'},
            AcceptAllPolicy : {signature : ACCEPT_ALL}
        },*/
        channelPoliciesMap.put("Readers","threshold");//:ANY identities: [] 空数组,principals也是空数组
        channelPoliciesMap.put("Writers","threshold");
        channelPoliciesMap.put("Admins","threshold");
        channelPoliciesMap.put("AcceptAllPolicy","signature");
        //buildPolicies(channelPoliciesMap, configGroupWBuilder);
        //Build configUpdate
        Configtx.ConfigUpdate configUpdate = Configtx.ConfigUpdate.newBuilder().
                setChannelId(channelName).setReadSet(configGroupR).setWriteSet(configGroupWBuilder.build()).build();
        return configUpdate;
    }

    private Configtx.ConfigUpdateEnvelope buildConfigUpdateEnvelope(String channelName) throws InvalidArgumentException {
        Configtx.ConfigUpdate configUpdate = buildConfigUpdate(channelName);
        Configtx.ConfigUpdateEnvelope.Builder configUpdateEnvelopeBuilder =
                Configtx.ConfigUpdateEnvelope.newBuilder();
        configUpdateEnvelopeBuilder.setConfigUpdate(configUpdate.toByteString()).addSignatures(signChannelConfigUpdate(configUpdate));

        return configUpdateEnvelopeBuilder.build();
    }


    /*(private Configtx.ConfigGroup buildOrderConfigGroup() {
        Configtx.ConfigGroup.Builder orderGroupBuilder = Configtx.ConfigGroup.newBuilder();
        orderGroupBuilder.setVersion(VERSION);

        //build configuration beg  ******
        //共识类型
        Configuration.ConsensusType consensusType = Configuration.ConsensusType.newBuilder().setType("solo").build();
        orderGroupBuilder.putValues("ConsensusType", buildConfigValue(consensusType.toByteArray()));

        Configuration.BatchSize batchSize = Configuration.BatchSize.newBuilder()
                .setMaxMessageCount(10)
                .setAbsoluteMaxBytes(99*1024*1024)
                .setPreferredMaxBytes(512*1024).build();
        orderGroupBuilder.putValues("BatchSize", buildConfigValue(batchSize.toByteArray()));

        Configuration.BatchTimeout batchTimeout = Configuration.BatchTimeout.newBuilder().setTimeout("10s").build();
        orderGroupBuilder.putValues("BatchTimeout", buildConfigValue(batchTimeout.toByteArray()));

        Configuration.CreationPolicy creationPolicy = Configuration.CreationPolicy.newBuilder().setPolicy("AcceptAllPolicy").build();
        orderGroupBuilder.putValues("CreationPolicy", buildConfigValue(creationPolicy.toByteArray()));
        //build configuration end  ******

        //build config groups beg  ******
        //TODO 暂时不考虑多groups情况( yaml/json => do )
        //endPoints属性
        String[] peers = {"orderer0:7050"};

        //policies属性
        Map<String, String> orgPoliciesMap = new HashMap<>();
        orgPoliciesMap.put("Readers","signature");//:ACCEPT_ALL identities: [] 空数组,principals也是空数组
        orgPoliciesMap.put("Writers","signature");
        orgPoliciesMap.put("Admins","signature");
        orderGroupBuilder.putGroups("OrdererMSP", buildOrganizationGroup("endPoints", peers, orgPoliciesMap));
        //build config groups end  ******

        Map<String, String> ordererPoliciesMap = new HashMap<>();
        ordererPoliciesMap.put("Readers","threshold");//:ANY identities: [] 空数组,principals也是空数组
        ordererPoliciesMap.put("Writers","threshold");
        ordererPoliciesMap.put("Admins","threshold");
        ordererPoliciesMap.put("AcceptAllPolicy","threshold");
        ordererPoliciesMap.put("BlockValidation","threshold-sub_policy"); //{threshold : 'ANY' , sub_policy : 'Writers'}
        buildPolicies(ordererPoliciesMap, orderGroupBuilder);

        return orderGroupBuilder.build();
    }*/

    private Configtx.ConfigGroup buildApplicationConfigGroup() {
        Configtx.ConfigGroup.Builder peersGroupBuilder = Configtx.ConfigGroup.newBuilder();
        peersGroupBuilder.setVersion(VERSION);

        Map<String, String> orgPoliciesMap = new HashMap<>();
        orgPoliciesMap.put("Readers","signature");//:ACCEPT_ALL identities: [] 空数组,principals也是空数组
        orgPoliciesMap.put("Writers","signature");
        orgPoliciesMap.put("Admins","signature");
        String[] peers01 = {"peer0:7051", "peer1:7056"};
        peersGroupBuilder.putGroups("Org1MSP", buildOrganizationGroup("anchorPeers", peers01, orgPoliciesMap));
        String[] peers23 = {"peer2:8051", "peer3:8056"};
        peersGroupBuilder.putGroups("Org2MSP", buildOrganizationGroup("anchorPeers", peers23, orgPoliciesMap));

        Map<String, String> peersPoliciesMap = new HashMap<>();
        peersPoliciesMap.put("Readers","threshold");//:ANY identities: [] 空数组,principals也是空数组
        peersPoliciesMap.put("Writers","threshold");
        peersPoliciesMap.put("Admins","threshold");
        //buildPolicies(peersPoliciesMap, peersGroupBuilder);

        return peersGroupBuilder.build();
    }

    private Configtx.ConfigGroup buildOrganizationGroup(String type, String[] peers, Map<String, String> policiesMap){
        Configtx.ConfigGroup.Builder configGroupBuilder = Configtx.ConfigGroup.newBuilder();
        //Peers
        switch (type){
            case "endPoints":
                //对ordererAddresses的处理 直接放在了configGroupW中设置
                break;
            case "anchorPeers":
                org.hyperledger.fabric.protos.peer.Configuration.AnchorPeers.Builder peersBuilder = org.hyperledger.fabric.protos.peer.Configuration.AnchorPeers.newBuilder();
                for(String peer : peers){
                    String[] args = peer.split(":");
                    peersBuilder.addAnchorPeers(org.hyperledger.fabric.protos.peer.Configuration.AnchorPeer.newBuilder().setHost(args[0]).setPort(Integer.parseInt(args[1])));
                }
                configGroupBuilder.putValues("AnchorPeers", buildConfigValue(peersBuilder.build().toByteString()));
        }

        /*

        policies : {
						Readers : {signature : ACCEPT_ALL},
						Writers : {signature : ACCEPT_ALL},
						Admins  : {signature : ACCEPT_ALL}
					},

        var ACCEPT_ALL = {
                identities: [],
        policy: {
            '0-of': []
        }
        };*/

        //policies build
        //buildPolicies(policiesMap, configGroupBuilder);

        return configGroupBuilder.build();
    }
    //只针对 signature : ACCEPT_ALL 和 threshold : ANY进行处理
    /*private void buildPolicies(Map<String, String> policiesStringMap, Configtx.ConfigGroup.Builder configGroupBuilder) {

        for(String key : policiesStringMap.keySet()){
            Policies.Policy.Builder policyBuilder = Policies.Policy.newBuilder();
            switch (policiesStringMap.get(key)){
                case "signature":
                    policyBuilder.setType(Policies.Policy.PolicyType.SIGNATURE.getNumber());
//                  signature : ACCEPT_ALL
                    Policies.SignaturePolicy signaturePolicy =
                            Policies.SignaturePolicy.newBuilder()
                                    .setNOutOf(Policies.SignaturePolicy.NOutOf.newBuilder().setN(0).build())
                                    .build();
//                    TODO setIdentities, principals []空数组
                    Policies.SignaturePolicyEnvelope signaturePolicyEnvelope = Policies.SignaturePolicyEnvelope.newBuilder()
                            .setPolicy(signaturePolicy).setVersion(VERSION).build();
                    policyBuilder.setPolicy(signaturePolicyEnvelope.toByteString());

                    break;

                case "threshold"://'ANY'
                    policyBuilder.setType(Policies.Policy.PolicyType.IMPLICIT_META.getNumber())
                            .setPolicy(Policies.ImplicitMetaPolicy.newBuilder()
                                    .setSubPolicy(key)
                                    .setRule(Policies.ImplicitMetaPolicy.Rule.ANY).build().toByteString());
                    break;

                case "threshold-sub_policy":
                    policyBuilder.setType(Policies.Policy.PolicyType.IMPLICIT_META.getNumber())
                            .setPolicy(Policies.ImplicitMetaPolicy.newBuilder()
                                    .setSubPolicy("Writers")
                                    .setRule(Policies.ImplicitMetaPolicy.Rule.ANY).build().toByteString());
                    break;

            }
            Policies.Policy policy = policyBuilder.build();
            Configtx.ConfigPolicy configPolicy = Configtx.ConfigPolicy.newBuilder().setVersion(VERSION).setModPolicy("Admins").setPolicy(policy).build();
            configGroupBuilder.putPolicies(key,configPolicy );
        }

    }*/


    private MSP loadMSPConfig(String name, String type, String org) throws IOException {
        MSP msp = new MSP();
        msp.id = name;
        String rootCertsDir = ChannelService.class.getResource("/").getPath()+"crypto-config/"+ type + "Organizations/"+ org +"/msp/cacerts";
        String adminsDir = ChannelService.class.getResource("/").getPath()+"crypto-config/"+ type + "Organizations/"+ org +"/msp/admincerts";
        msp.rootCerts = readAllFiles(rootCertsDir);
        msp.admins = readAllFiles(adminsDir);
        return msp;
    }

    private List<String> readAllFiles(String dirPath) throws IOException {
        List<String> files = new ArrayList<>();
        File dir = new File(dirPath);
        File[] tempList = dir.listFiles();
        String str = "";
        StringBuilder sb = new StringBuilder();
        for(File file : tempList){
            BufferedReader bufferReader = new BufferedReader(new FileReader(file));
            while ((str = bufferReader.readLine())!=null)
                sb.append(str+"\n");
            files.add(sb.toString());
        }
        return files;
    }


    public Configtx.ConfigValue buildConfigValue(String value){
        Configtx.ConfigValue configValue = Configtx.ConfigValue.newBuilder().setVersion(VERSION).setValue(ByteString.copyFrom(value.getBytes())).build();
        return configValue;
    }
    public Configtx.ConfigValue buildConfigValue(byte[] value){
        Configtx.ConfigValue configValue = Configtx.ConfigValue.newBuilder().setVersion(VERSION).setValue(ByteString.copyFrom(value)).build();
        return configValue;
    }
    public Configtx.ConfigValue buildConfigValue(ByteString value){
        Configtx.ConfigValue configValue = Configtx.ConfigValue.newBuilder().setVersion(VERSION).setValue(value).build();
        return configValue;
    }

    class MSP{
        String id;
        List<String> rootCerts;
        List<String> admins;
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
    }

    private Configtx.ConfigSignature signChannelConfigUpdate(Configtx.ConfigUpdate configUpdate){
        //nonce random byteString, default length = 24
        HFClient hfClient = chaincodeService.getHfClient();
        byte[] bytes = new byte[24];
        new Random().nextBytes(bytes);
        Common.SignatureHeader signatureHeader = Common.SignatureHeader.newBuilder().setCreator(ByteString.copyFromUtf8(hfClient.getUserContext().getName())).setNonce(ByteString.copyFrom(bytes)).build();
        byte[] signatureHeaderBytes = signatureHeader.toByteArray();
        byte[] configUpdateBytes = configUpdate.toByteArray();

        byte[] concat = Arrays.copyOf(signatureHeaderBytes, signatureHeaderBytes.length + configUpdateBytes.length);
        System.arraycopy(configUpdateBytes, 0, concat, signatureHeaderBytes.length, configUpdateBytes.length);
        byte[] signature = null;
        try {
            signature = hfClient.getCryptoSuite().sign(hfClient.getUserContext().getEnrollment().getKey(), concat);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
        Configtx.ConfigSignature configSignature = Configtx.ConfigSignature.newBuilder().setSignatureHeader(signatureHeader.toByteString()).setSignature(ByteString.copyFrom(signature)).build();
        return configSignature;
    }
}
