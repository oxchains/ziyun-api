package com.oxchains.service;

import com.oxchains.model.Customer;
import com.oxchains.util.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ChaincodeService
 *
 * @author liuruichao
 * Created on 2017/4/6 16:23
 */
@Service
@Slf4j
public class ChaincodeService extends BaseService {
    private static final String CHAIN_CODE_NAME = "demo.go";

    private static final String CHAIN_CODE_PATH = "ziyun_cc";

    private static final String CHAIN_CODE_VERSION = "1.0";

    private static final String TEST_FIXTURES_PATH = "/Users/liuruichao/javaSRC/oxchains/ziyundemo/src/main/resources";

    private static final String CA_URL = "http://localhost:7054";

    private static final String ORDERER_URL = "grpc://localhost:7050";

    private static final String PEER_LIST = "peer0@grpc://localhost:7051,peer1@grpc://localhost:7056";

    private Chain chain;

    private HFClient hfClient;

    public ChaincodeService() {
        try {
            String username = "admin";
            String password = "adminpw";
            ArrayList<String> roles = new ArrayList<>();
            String account = null;
            String affiliation = "peerOrg1";
            String mspID = "Org1MSP";
            //String mspID = "ziyun.MSP";
            String configPath = "/Users/liuruichao/javaSRC/oxchains/ziyundemo/src/main/resources/foo.tx";
            String ordererName = "orderer0";
            String chainName = "foo";

            MemberServices memberServices = new HFCAClient(CA_URL, null);
            memberServices.setCryptoSuite(CryptoUtils.createCryptoSuite());
            Enrollment enrollment = memberServices.enroll(username, password);

            Customer customer = new Customer(username, enrollment, roles, account, affiliation, mspID);

            hfClient = HFClient.createNewInstance();
            hfClient.setCryptoSuite(CryptoUtils.createCryptoSuite());
            hfClient.setMemberServices(memberServices);
            hfClient.setUserContext(customer);

            Orderer orderer = hfClient.newOrderer(ordererName, ORDERER_URL, null);
            try {
                // 只有第一次需要创建chain
                chain = createChain(hfClient, configPath, orderer, "foo");
            } catch (Exception e2) {
                chain = getChain(hfClient, chainName, orderer);
            }
        } catch (Exception e) {
            log.error("CargoService init error!", e);
            System.exit(1);
        }
    }

    public void installChaincode() throws InvalidArgumentException, ProposalException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();
        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
        installProposalRequest.setChaincodeID(chainCodeID);

        installProposalRequest.setChaincodeSourceLocation(new File(TEST_FIXTURES_PATH));
        installProposalRequest.setChaincodeVersion(CHAIN_CODE_VERSION);

        Collection<Peer> peersFromOrg = chain.getPeers();
        Collection<ProposalResponse> responses = chain.sendInstallProposal(installProposalRequest, peersFromOrg);
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

    public void instantiateChaincode() throws IOException, ProposalException, InvalidArgumentException, InterruptedException, ExecutionException, TimeoutException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();
        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chainCodeID);
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[]{"a", "500", "b", "" + 200});

            /*
              policy OR(Org1MSP.member, Org2MSP.member) meaning 1 signature from someone in either Org1 or Org2
              See README.md Chaincode endorsement policies section for more details.
              new File(TEST_FIXTURES_PATH + "/members_from_org1_or_2.policy")
            */
        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);

        Collection<ProposalResponse> successful = new ArrayList<>();
        // Send instantiate transaction to peers
        Collection<ProposalResponse> responses = chain.sendInstantiationProposal(instantiateProposalRequest, chain.getPeers());
        for (ProposalResponse response : responses) {
            if (response.isVerified() && response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
                System.out.println(String.format("Succesful instantiate proposal response Txid: %s from peer %s",
                        response.getTransactionID(),
                        response.getPeer().getName()));
            } else {
                System.out.println("Instantiate Chaincode error! " + response.getMessage());
            }
        }

        /// Send instantiate transaction to orderer
        chain.sendTransaction(successful, chain.getOrderers()).get(120, TimeUnit.SECONDS);
        System.out.println("instantiateChaincode done");
    }

    public Chain getChain(HFClient hfClient, String chainName, Orderer orderer) throws InvalidArgumentException, TransactionException {
        Chain chain = hfClient.newChain(chainName);

        Set<Peer> peers = getPeers();
        for (Peer peer : peers) {
            chain.addPeer(peer);
        }

        chain.addOrderer(orderer);
        chain.initialize();
        return chain;
    }

    public Chain createChain(HFClient hfClient, String configPath, Orderer orderer, String chainName) throws IOException, InvalidArgumentException, TransactionException, ProposalException {
        ChainConfiguration chainConfiguration = new ChainConfiguration(new File(configPath));
        Chain newChain = hfClient.newChain(chainName, orderer, chainConfiguration);

        Set<Peer> peers = getPeers();
        for (Peer peer : peers) {
            newChain.joinPeer(peer);
        }

        newChain.initialize();
        return newChain;
    }

    public Set<Peer> getPeers() throws InvalidArgumentException {
        Set<Peer> peers = new HashSet<>();
        String[] peerAddressList = PEER_LIST.split(",");
        for (String address : peerAddressList) {
            String[] params = address.split("@");
            peers.add(hfClient.newPeer(params[0], params[1]));
        }
        return peers;
    }

    public String invoke(String func, String[] args) throws InvalidArgumentException, ProposalException, InterruptedException, ExecutionException, TimeoutException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION).build();

        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chainCodeID);
        transactionProposalRequest.setFcn(func);
        transactionProposalRequest.setArgs(args);

        Collection<ProposalResponse> transactionPropResp = chain.sendTransactionProposal(transactionProposalRequest, chain.getPeers());
        for (ProposalResponse response : transactionPropResp) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                return response.getTransactionID();
            }
        }

        return null;
    }

    public String query(String func, String[] args) throws ProposalException, InvalidArgumentException {
        ChainCodeID chainCodeID = ChainCodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION).build();
        // .setPath(CHAIN_CODE_PATH).build(); // 查询不需要path

        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(func);
        queryByChaincodeRequest.setChaincodeID(chainCodeID);

        Collection<ProposalResponse> queryProposals = chain.queryByChaincode(queryByChaincodeRequest, chain.getPeers());
        for (ProposalResponse proposalResponse : queryProposals) {
            if (proposalResponse.isVerified() && proposalResponse.getStatus() == ProposalResponse.Status.SUCCESS) {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                return payload;
            }
        }

        return null;
    }
}
