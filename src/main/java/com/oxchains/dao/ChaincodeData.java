package com.oxchains.dao;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oxchains.bean.model.ziyun.JwtToken;
import com.oxchains.bean.model.ziyun.ProduceInfo;
import com.oxchains.common.ChaincodeResp;
import com.oxchains.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Optional;

import static com.oxchains.util.ResponseUtil.*;

/**
 * Created by Luo_xuri on 2017/7/19.
 */
@Service
@Slf4j
public class ChaincodeData {

    @Value("${fabric.url.tx}")
    private String txUri;

    private HttpEntity<String> entity;
    private RestTemplate restTemplate = new RestTemplate();


    public ChaincodeData(@Autowired @Qualifier("token") String token) {
        System.out.println("===token===" + token);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);
        this.entity = new HttpEntity<>(httpHeaders);
    }

    public Optional<ChaincodeResp> invoke(String func, String[] args) {
        try {
            String arg = StringUtils.join(args, ",");
            String url = txUri + func + "," + "{"+arg+"}";
            return extract(restTemplate.postForObject(url, entity, String.class), "/data").map(data -> resolve(data, ChaincodeResp.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(null);
    }

    public Optional<ChaincodeResp> query(String func, String[] args) {
        String url = txUri + func + "," + StringUtils.join(args, ",");
        return extract(restTemplate.postForObject(url, entity, String.class), "/data")
                .map(data -> resolve(data, ChaincodeResp.class));
    }
}
