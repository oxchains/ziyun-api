package com.oxchains.service;

import com.google.protobuf.ByteString;
import com.oxchains.bean.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.peer.Chaincode;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * ChaincodeService
 *
 * @author liuruichao
 * Created on 2017/4/6 16:23
 */
@Service
@Slf4j
public class ChaincodeService extends BaseService implements InitializingBean {
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

    @Value("${chain.config.path}")
    private String configPath;

    @Value("${chain.name}")
    private String chainName;

    private Chain chain;

    private HFClient hfClient;

    private ChainCodeID chainCodeID;

    public void installChaincode() throws InvalidArgumentException, ProposalException {
        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
        installProposalRequest.setChaincodeID(chainCodeID);
        installProposalRequest.setChaincodeSourceLocation(new File(TEST_FIXTURES_PATH));
        installProposalRequest.setChaincodeVersion(CHAIN_CODE_VERSION);

        Collection<ProposalResponse> responses = chain.sendInstallProposal(installProposalRequest, chain.getPeers());
        for (ProposalResponse response : responses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                System.out.println(String.format("Successful install proposal response Txid: %s from peer %s",
                        response.getTransactionID(),
                        response.getPeer().getName()));
            } else {
                System.out.println("install chaincode error!");
            }
        }
    }

    public void instantiateChaincode() throws IOException, ProposalException, InvalidArgumentException, InterruptedException, ExecutionException, TimeoutException, ChaincodeEndorsementPolicyParseException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();
        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chainCodeID);
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[]{});

        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        // 背书策略
        chaincodeEndorsementPolicy.fromFile(new File(TEST_FIXTURES_PATH + "/members_from_org1_or_2.policy"));
        //chaincodeEndorsementPolicy.fromYamlFile(new File(TEST_FIXTURES_PATH + "/chaincodeendorsementpolicy.yaml"));
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Collection<ProposalResponse> successful = new ArrayList<>();
        // Send instantiate transaction to peers
        Collection<ProposalResponse> responses = chain.sendInstantiationProposal(instantiateProposalRequest, chain.getPeers());
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
            chain.sendTransaction(successful, chain.getOrderers());
            System.out.println("instantiateChaincode done");
        }
    }

    public Chain getChain(String chainName, Orderer orderer) throws InvalidArgumentException, TransactionException {
        Chain chain = hfClient.newChain(chainName);

        Set<Peer> peers = getPeers();
        for (Peer peer : peers) {
            chain.addPeer(peer);
        }

        chain.addOrderer(orderer);
        chain.initialize();
        return chain;
    }

    public Chain createChain(String configPath, Orderer orderer, String chainName) throws IOException, InvalidArgumentException, TransactionException, ProposalException {
        ChainConfiguration chainConfiguration = new ChainConfiguration(new File(configPath));
        Chain newChain = hfClient.newChain(chainName, orderer, chainConfiguration);
        newChain.setTransactionWaitTime(100000);
        newChain.setDeployWaitTime(120000);

        Set<Peer> peers = getPeers();
        for (Peer peer : peers) {
            System.out.println("join chain: " + newChain.joinPeer(peer));
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
        transactionProposalRequest.setChaincodeID(chainCodeID);
        transactionProposalRequest.setFcn(func);
        transactionProposalRequest.setArgs(args);

        // send Proposal to peers
        Collection<ProposalResponse> transactionPropResp = chain.sendTransactionProposal(transactionProposalRequest, chain.getPeers());

        // send Proposal to orderers
        Collection<ProposalResponse> successful = new ArrayList<>();
        for (ProposalResponse response : transactionPropResp) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                txID = response.getTransactionID();
                successful.add(response);
            }
        }
        chain.sendTransaction(successful, chain.getOrderers());

        return txID;
    }

    public String query(String func, String[] args) {
        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(func);
        queryByChaincodeRequest.setChaincodeID(chainCodeID);

        Collection<ProposalResponse> queryProposals = null;
        try {
            queryProposals = chain.queryByChaincode(queryByChaincodeRequest, chain.getPeers());
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

    public String queryBlockInfo() throws InvalidArgumentException, ProposalException {
        BlockchainInfo channelInfo = chain.queryBlockchainInfo();
        String chainCurrentHash = Hex.encodeHexString(channelInfo.getCurrentBlockHash());
        String chainPreviousHash = Hex.encodeHexString(channelInfo.getPreviousBlockHash());

        System.out.println("height: " + channelInfo.getHeight());
        System.out.println("currentHash: " + chainCurrentHash);
        System.out.println("previousHash: " + chainPreviousHash);

        // TODO test
        BlockInfo blockInfo = queryBlock(channelInfo.getHeight() - 1);
        System.out.println(blockInfo);
        /*blockInfo = queryBlock(blockInfo.getPreviousHash());
        System.out.println(blockInfo);*/
        Common.BlockData blockData = blockInfo.getBlock().getData();
        for (ByteString str : blockData.getDataList()) {
            System.out.println(str.toStringUtf8());
        }
        return null;
    }

    public BlockInfo queryBlock(long blockNumber) throws ProposalException, InvalidArgumentException {
        BlockInfo blockInfo = chain.queryBlockByNumber(blockNumber);
        String previousHash = Hex.encodeHexString(blockInfo.getPreviousHash());
        System.out.println("queryBlockByNumber returned correct block with blockNumber " + blockInfo.getBlockNumber()
                + " \n previous_hash " + previousHash);
        return blockInfo;
    }

    public BlockInfo queryBlock(byte[] hash) throws ProposalException, InvalidArgumentException {
        BlockInfo blockInfo = chain.queryBlockByHash(hash);
        System.out.println("queryBlockByHash returned block with blockNumber " + blockInfo.getBlockNumber());
        return null;
    }

    public BlockInfo queryBlock(String txID) throws ProposalException, InvalidArgumentException {
        BlockInfo blockInfo = chain.queryBlockByTransactionID(txID);
        System.out.println("queryBlockByTxID returned block with blockNumber " + blockInfo.getBlockNumber());
        return null;
    }

    public TransactionInfo queryTransactionInfo(String txID) throws InvalidArgumentException, ProposalException {
        TransactionInfo transactionInfo = chain.queryTransactionByID(txID);
        return transactionInfo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            String username = "admin";
            String password = "adminpw";
            ArrayList<String> roles = new ArrayList<>();
            String account = null;
            String affiliation = "peerOrg1";
            String mspID = "Org1MSP";
            //String mspID = "ziyun.MSP";
            String ordererName = "orderer0";

            hfClient = HFClient.createNewInstance();
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            hfClient.setMemberServices(new HFCAClient(CA_URL, null));

            Enrollment enrollment = hfClient.getMemberServices().enroll(username, password);
            Customer customer = new Customer(username, enrollment, roles, account, affiliation, mspID);
            hfClient.setUserContext(customer);

            Orderer orderer = hfClient.newOrderer(ordererName, ORDERER_URL, null);
                // 只有第一次需要创建chain
            // IOException, InvalidArgumentException, TransactionException, ProposalException
            try {
                chain = createChain(configPath, orderer, chainName);
            } catch (IOException | InvalidArgumentException | TransactionException | ProposalException e) {
                log.warn("createChain error!", e);
                chain = getChain(chainName, orderer);
            } catch (Exception e) {
                log.error("createChain error!", e);
            }
            chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                    .setVersion(CHAIN_CODE_VERSION)
                    .setPath(CHAIN_CODE_PATH).build();
        } catch (Exception e) {
            log.error("CargoService init error!", e);
            System.exit(1);
        }
    }
}
