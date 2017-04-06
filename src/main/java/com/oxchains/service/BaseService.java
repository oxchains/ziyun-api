package com.oxchains.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * BaseService
 *
 * @author liuruichao
 * Created on 2017/4/6 16:23
 */
public abstract class BaseService {
    protected final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
}
