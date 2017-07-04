package com.oxchains.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.oxchains.bean.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.common.Configtx;
import org.hyperledger.fabric.protos.msp.Identities;
import org.hyperledger.fabric.protos.peer.FabricTransaction;
import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.PrivateKey;
import java.security.Security;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * ChaincodeService
 *
 * @author liuruichao
 * Created on 2017/4/6 16:23
 */
@Service
@Slf4j
public class ChaincodeService extends BaseService implements InitializingBean, DisposableBean {
    @Value("${chaincode.name}")
    private String CHAIN_CODE_NAME;

    @Value("${chaincode.path}")
    private String CHAIN_CODE_PATH;

    @Value("${chaincode.version}")
    private String CHAIN_CODE_VERSION;

    @Value("${chaincode.resource.path}")
    private String TEST_FIXTURES_PATH;

    @Value("${chaincode.ca.url}")
    private String CA_URL;

    @Value("${chaincode.orderer.url}")
    private String ORDERER_URL;

    @Value("${chaincode.peer.address.list}")
    private String PEER_LIST;

    @Value("${channel.config.path}")
    private String configPath;

    @Value("${channel.name}")
    private String channelName;

    private Channel channel;

    private HFClient hfClient;

    private HFCAClient hfcaClient;

    private ChaincodeID channelCodeID;

    private Customer peerOrgAdmin;

    private Customer customer;

    public void installChaincode() throws InvalidArgumentException, ProposalException {
        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
        installProposalRequest.setChaincodeID(channelCodeID);
        installProposalRequest.setChaincodeSourceLocation(new File(TEST_FIXTURES_PATH));
        installProposalRequest.setChaincodeVersion(CHAIN_CODE_VERSION);

        Collection<ProposalResponse> responses = hfClient.sendInstallProposal(installProposalRequest, channel.getPeers());
        for (ProposalResponse response : responses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                System.out.println(String.format("Successful install proposal response Txid: %s from peer %s",
                        response.getTransactionID(),
                        response.getPeer().getName()));
            } else {
                System.out.println("install channelcode error!");
            }
        }
    }

    public void instantiateChaincode() throws IOException, ProposalException, InvalidArgumentException, InterruptedException, ExecutionException, TimeoutException, ChaincodeEndorsementPolicyParseException {
        //hfClient.setUserContext(customer);
        ChaincodeID channelCodeID = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();
        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(channelCodeID);
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[]{});
        Map<String, byte[]> transientMap = new HashMap<>();
        transientMap.put("HyperLedgerFabric", "InstantiateProposalRequest:JavaSDK".getBytes(UTF_8));
        transientMap.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
        instantiateProposalRequest.setTransientMap(transientMap);

        ChaincodeEndorsementPolicy channelcodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        // 背书策略
        //channelcodeEndorsementPolicy.fromFile(new File(TEST_FIXTURES_PATH + "/members_from_org1_or_2.policy"));
        channelcodeEndorsementPolicy.fromYamlFile(new File(TEST_FIXTURES_PATH + "/channelcodeendorsementpolicy.yaml"));
        instantiateProposalRequest.setChaincodeEndorsementPolicy(channelcodeEndorsementPolicy);

        Collection<ProposalResponse> successful = new ArrayList<>();
        // Send instantiate transaction to peers
        Collection<ProposalResponse> responses = channel.sendInstantiationProposal(instantiateProposalRequest, channel.getPeers());
        if (responses != null && responses.size() > 0) {
            for (ProposalResponse response : responses) {
                if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
                    successful.add(response);
                    log.info(String.format("Succesful instantiate proposal response Txid: %s from peer %s",
                            response.getTransactionID(),
                            response.getPeer().getName()));
                } else {
                    System.out.println("Instantiate Chaincode error! " + response.getMessage());
                }
            }

            /// Send instantiate transaction to orderer
            channel.sendTransaction(successful, channel.getOrderers());
            System.out.println("instantiateChaincode done");
        }
    }

    public Set<String> getChannels() throws ProposalException, InvalidArgumentException {
        Set<String> channels = hfClient.queryChannels(channel.getPeers().iterator().next());
        return channels;
    }

    public List<Query.ChaincodeInfo> getInstalledChaincodes() throws ProposalException, InvalidArgumentException {
        List<Query.ChaincodeInfo> channelcodeInfos = hfClient.queryInstalledChaincodes(channel.getPeers().iterator().next());
        for (Query.ChaincodeInfo channelcodeInfo : channelcodeInfos) {
            System.out.println(channelcodeInfo.getName());
        }
        return channelcodeInfos;
    }

    public Channel getChain(String channelName, Orderer orderer) throws InvalidArgumentException, TransactionException {
        Channel channel = hfClient.newChannel(channelName);

        Set<Peer> peers = getPeers();
        for (Peer peer : peers) {
            channel.addPeer(peer);
        }

        channel.addOrderer(orderer);
        channel.initialize();
        return channel;
    }

    public Channel createChain(String configPath, Orderer orderer, String channelName) throws IOException, InvalidArgumentException, TransactionException, ProposalException {
        ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(configPath));
        hfClient.setUserContext(peerOrgAdmin);
        Channel newChain = hfClient.newChannel(channelName, orderer, channelConfiguration, hfClient.getChannelConfigurationSignature(channelConfiguration, peerOrgAdmin));
        newChain.setTransactionWaitTime(100000);
        newChain.setDeployWaitTime(120000);

        Set<Peer> peers = getPeers();
        for (Peer peer : peers) {
            log.info("join channel: " + newChain.joinPeer(peer));
        }

        newChain.initialize();
        return newChain;
    }

    private Set<Peer> getPeers() throws InvalidArgumentException {
        Set<Peer> peers = new HashSet<>();
        String[] peerAddressList = PEER_LIST.split(",");
        for (String address : peerAddressList) {
            String[] params = address.split("@");
            peers.add(hfClient.newPeer(params[0], params[1]));
        }
        return peers;
    }

    public String invoke(String func, String[] args) throws InvalidArgumentException, ProposalException, InterruptedException, ExecutionException, TimeoutException {
        String txID = null;

        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(channelCodeID);
        transactionProposalRequest.setFcn(func);
        transactionProposalRequest.setArgs(args);

        // send Proposal to peers
        Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());

        // send Proposal to orderers
        Collection<ProposalResponse> successful = new ArrayList<>();
        for (ProposalResponse response : transactionPropResp) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                txID = response.getTransactionID();
                successful.add(response);
            }
        }
        channel.sendTransaction(successful, channel.getOrderers());

        return txID;
    }

    public String query(String func, String[] args) {
        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(func);
        queryByChaincodeRequest.setChaincodeID(channelCodeID);

        Collection<ProposalResponse> queryProposals = null;
        try {
            queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
        } catch (InvalidArgumentException | ProposalException ignored) {
            return null;
        }
        for (ProposalResponse proposalResponse : queryProposals) {
            if (proposalResponse.isVerified() && proposalResponse.getStatus() == ProposalResponse.Status.SUCCESS) {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                return payload;
            }
        }

        return null;
    }

    public BlockchainInfo queryChain() throws InvalidArgumentException, ProposalException, InvalidProtocolBufferException {
        BlockchainInfo blockchannelInfo = channel.queryBlockchainInfo();
        /*String channelCurrentHash = Hex.encodeHexString(blockchannelInfo.getCurrentBlockHash());
        String channelPreviousHash = Hex.encodeHexString(blockchannelInfo.getPreviousBlockHash());*/

        /*System.out.println("height: " + blockchannelInfo.getHeight());
        System.out.println("currentHash: " + channelCurrentHash);
        System.out.println("previousHash: " + channelPreviousHash);*/

        System.out.println("size: " + blockchannelInfo.getBlockchainInfo().getSerializedSize());
       // TODO test
        for (int i = 0; i < blockchannelInfo.getHeight(); i++) {
            BlockInfo blockInfo = queryBlock(i);
            // block header
            Common.BlockHeader blockHeader = blockInfo.getBlock().getHeader();
            /*System.out.printf("dataHash: %s, previousHash: %s.\n",
                    Hex.encodeHexString(blockHeader.getDataHash().toByteArray()),
                    Hex.encodeHexString(blockHeader.getPreviousHash().toByteArray()));*/
            Common.BlockMetadata blockMetadata = blockInfo.getBlock().getMetadata();
            /*for (ByteString str : blockMetadata.getMetadataList()) {
                Common.Metadata metadata = Common.Metadata.parseFrom(str);
                System.out.println(metadata);
            }*/
            //System.out.println(blockMetadata);

            // block data
            Common.BlockData blockData = blockInfo.getBlock().getData();
            for (ByteString data : blockData.getDataList()) {
                // 获取txID
                Common.Envelope envelope = Common.Envelope.parseFrom(data);
                Common.Payload payload = Common.Payload.parseFrom(envelope.getPayload());
                Common.ChannelHeader channelHeader = Common.ChannelHeader.parseFrom(payload.getHeader().getChannelHeader());
                System.out.println("txID: " + channelHeader.getTxId());
                //TransactionInfo transactionInfo = queryTransactionInfo(channelHeader.getTxId());

                if (channelHeader.getType() == 1) {
                    // 获取config
                    Configtx.ConfigEnvelope configEnvelope = Configtx.ConfigEnvelope.parseFrom(payload.getData());
                    Map<String, Configtx.ConfigGroup> map = configEnvelope.getConfig()
                            .getChannelGroup()
                            .getGroupsMap();

                    // decode ConfigGroup
                    parseConfigGroup(map);
                } else if (channelHeader.getType() == 3) {
                    // 获取transaction
                    FabricTransaction.Transaction transaction = FabricTransaction.Transaction.parseFrom(payload.getData());
                    for (FabricTransaction.TransactionAction transactionAction : transaction.getActionsList()) {
                        FabricTransaction.ChaincodeActionPayload channelcodeActionPayload = FabricTransaction.ChaincodeActionPayload.parseFrom(transactionAction.getPayload());
                        channelcodeActionPayload
                                .getAction()
                                .getEndorsementsList()
                                .forEach(endorsement -> {
                                    try {
                                        Identities.SerializedIdentity endorser = Identities.SerializedIdentity.parseFrom(endorsement.getEndorser());
                                        System.out.println(String.format("mspID: %s, idByte: %s.", endorser.getMspid(), endorser.getIdBytes().toStringUtf8()));
                                    } catch (InvalidProtocolBufferException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println();
                                });
                        System.out.println();
                    }
                } else {
                    throw new RuntimeException("Only able to decode ENDORSER_TRANSACTION and CONFIG type blocks");
                }
                System.out.println();
            }
            //System.out.println("height: " + i + ", blockData count: " + blockData.getDataCount());
        }
        System.out.println();

        /*System.out.println("==================================");
        TransactionInfo transactionInfo = queryTransactionInfo("d2433a1e17e542bf865cbe2d3dd952d6c3f4a66f46476d86622313d6fcefbd3d");
        System.out.println(transactionInfo.getEnvelope());*/
        return blockchannelInfo;
    }

    private void parseConfigGroup(Map<String, Configtx.ConfigGroup> map) {
        Set<Map.Entry<String, Configtx.ConfigGroup>> entrySet = map.entrySet();
        for (Map.Entry<String, Configtx.ConfigGroup> entry : entrySet) {
            String key = entry.getKey();
            Configtx.ConfigGroup configGroup = entry.getValue();
            long version = configGroup.getVersion();
            // TODO groups 这是一个递归的过程

            // values
            Map<String, Configtx.ConfigValue> valueMap = configGroup.getValuesMap();
            for (String valueKey : valueMap.keySet()) {
                Configtx.ConfigValue configValue = valueMap.get(valueKey);
            }
        }
    }

    private void parseConfigValue(Configtx.ConfigValue configValue) {
        long version = configValue.getVersion();
        String modPolicy = configValue.getModPolicy();
        Map map = new HashMap();
    }

    public BlockInfo queryBlock(long blockNumber) throws ProposalException, InvalidArgumentException {
        BlockInfo blockInfo = channel.queryBlockByNumber(blockNumber);
        /*String previousHash = Hex.encodeHexString(blockInfo.getPreviousHash());
        System.out.println("queryBlockByNumber returned correct block with blockNumber " + blockInfo.getBlockNumber()
                + " \n previous_hash: " + previousHash);*/
        return blockInfo;
    }

    public BlockInfo queryBlock(byte[] hash) throws ProposalException, InvalidArgumentException {
        BlockInfo blockInfo = channel.queryBlockByHash(hash);
        System.out.println("queryBlockByHash returned block with blockNumber " + blockInfo.getBlockNumber());
        return null;
    }

    public BlockInfo queryBlock(String txID) throws ProposalException, InvalidArgumentException {
        BlockInfo blockInfo = channel.queryBlockByTransactionID(txID);
        System.out.println("queryBlockByTxID returned block with blockNumber " + blockInfo.getBlockNumber());
        return null;
    }

    public TransactionInfo queryTransactionInfo(String txID) throws InvalidArgumentException, ProposalException, InvalidProtocolBufferException {
        TransactionInfo transactionInfo = channel.queryTransactionByID(txID);
        Common.Payload payload = Common.Payload.parseFrom(transactionInfo.getProcessedTransaction().getTransactionEnvelope().getPayload());
        Common.ChannelHeader channelHeader = Common.ChannelHeader.parseFrom(payload.getHeader().getChannelHeader());
        System.out.println(channelHeader.getChannelId());
        System.out.println("time: " + channelHeader.getTimestamp().getNanos());
        return transactionInfo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            initPeerAdmin();
            // SampleUser{name='peerOrg1Admin', roles=null, account='null', affiliation='null', organization='peerOrg1', enrollmentSecret='null', enrollment=org.hyperledger.fabric.sdkintegration.SampleStore$SampleStoreEnrollement@ffaa6af, keyValStore=org.hyperledger.fabric.sdkintegration.SampleStore@5562c41e, keyValStoreName='user.peerOrg1AdminpeerOrg1', mspID='Org1MSP'}
            String username = "admin";
            String password = "adminpw";
            Set<String> roles = null;
            String account = null;
            String affiliation = "peerOrg1";
            String mspID = "Org1MSP";
            //String mspID = "ziyun.MSP";
            String ordererName = "orderer0";

            hfClient = HFClient.createNewInstance();
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            hfcaClient = HFCAClient.createNewInstance(CA_URL, null);
            hfcaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

            Enrollment enrollment = hfcaClient.enroll(username, password);
            customer = new Customer(username, enrollment, roles, account, affiliation, mspID);
            hfClient.setUserContext(customer);

            Orderer orderer = hfClient.newOrderer(ordererName, ORDERER_URL, null);
                // 只有第一次需要创建channel
            // IOException, InvalidArgumentException, TransactionException, ProposalException
            try {
                channel = createChain(configPath, orderer, channelName);
            } catch (IOException | InvalidArgumentException | TransactionException | ProposalException e) {
                log.warn("createChain error!", e);
                orderer = hfClient.newOrderer(ordererName, ORDERER_URL, null);
                channel = getChain(channelName, orderer);
            } catch (Exception e) {
                log.error("createChain error!", e);
            }
            channelCodeID = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME)
                    .setVersion(CHAIN_CODE_VERSION)
                    .setPath(CHAIN_CODE_PATH).build();
        } catch (Exception e) {
            log.error("CargoService init error!", e);
            System.exit(1);
        }
    }

    private void initPeerAdmin() throws IOException {
        peerOrgAdmin = new Customer();
        peerOrgAdmin.setName("peerAdmin");
        peerOrgAdmin.setAccount(null);
        peerOrgAdmin.setAffiliation(null);
        peerOrgAdmin.setMspID("Org1MSP");
        peerOrgAdmin.setRoles(null);

        String certificate = new String(IOUtils.toByteArray(new FileInputStream(TEST_FIXTURES_PATH + "/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem")), "UTF-8");

        //PrivateKey privateKey = getPrivateKeyFromFile(privateKeyFile);
        String privateKeyFile = TEST_FIXTURES_PATH + "/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/0880f9de3e22993623203af9c3045c891318b02fbf81225f0ece4bb77a34076c_sk";
        final PEMParser pemParser = new PEMParser(new StringReader(new String(IOUtils.toByteArray(new FileInputStream(privateKeyFile)))));

        PrivateKeyInfo pemPair = (PrivateKeyInfo) pemParser.readObject();

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);

        peerOrgAdmin.setEnrollment(AdminEnrollment.createInstance(privateKey, certificate));
    }

    public HFClient getHfClient() {
        return hfClient;
    }

    public Orderer getOrderer() {
        return channel.getOrderers().iterator().next();
    }

    public Channel getChain() {
        return channel;
    }

    private static class AdminEnrollment implements Enrollment {
        private PrivateKey privateKey;

        private String certificate;

        private AdminEnrollment() {
        }

        private static AdminEnrollment createInstance(PrivateKey privateKey, String certificate) {
            AdminEnrollment adminEnrollment = new AdminEnrollment();
            adminEnrollment.privateKey = privateKey;
            adminEnrollment.certificate = certificate;
            return adminEnrollment;
        }

        @Override
        public PrivateKey getKey() {
            return privateKey;
        }

        @Override
        public String getCert() {
            return certificate;
        }
    }

    @Override
    public void destroy() throws Exception {
        // channel shutdown
        channel.shutdown(true);
    }
}
