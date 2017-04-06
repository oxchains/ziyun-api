package com.oxchains.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * BaseController
 *
 * @author liuruichao
 * Created on 2017/4/6 11:37
 */
public class BaseController {
    protected final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
}