package com.oxchains.data;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.bean.model.ziyun.Product;
import com.oxchains.common.ChaincodeResp;
import com.oxchains.service.BaseService;
import com.oxchains.util.ResponseUtil;
import com.oxchains.util.TokenUtils;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.http.HttpMethod.POST;
import static com.oxchains.util.ResponseUtil.*;

/**
 * Created by Luo_xuri on 2017/7/18.
 */
@Service
public class ChaincodeData {

    @Value("${fabric.url.tx}") private String txUri;

    private HttpEntity<String> entity;
    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    public ChaincodeData(@Autowired @Qualifier("token") String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);
        this.entity = new HttpEntity<>(httpHeaders);
    }

    public Optional<ChaincodeResp> addProduceInfo(ProduceInfo produceInfo) {
        String token = produceInfo.getToken();
        JwtToken jwt = TokenUtils.parseToken(token);
        produceInfo.setToken(jwt.getId());

        return extract(restTemplate.postForObject(txUri + "saveProduceInfo,"
                + new String[]{gson.toJson(produceInfo)}, entity, String.class), "/data")
                .map(data -> resolve(data, ChaincodeResp.class));

//        return ResponseUtil.extract(restTemplate.exchange(String.format("%s%s,%s", txUri, "saveProduceInfo", produceInfo), POST, entity, String.class).getBody(), "/data").map(data -> ResponseUtil.resolve(data, ChaincodeResp.class));
    }
}
