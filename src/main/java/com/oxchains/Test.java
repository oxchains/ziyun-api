package com.oxchains;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.oxchains.bean.model.ziyun.TransportBill;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Test
 *
 * @author liuruichao
 * Created on 2017/4/6 18:37
 */
public class Test {
    private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
    public static void main(String[] args) {
       // str();
       // map();
        jsonToObject();
    }

    private static void str(){
        String s = "data:image/png;base64,iVBORw0KGgoAAAANS";
        String t = s.substring(5,s.indexOf(";base64"));
        System.out.println(t);
        System.out.println(s.substring(s.indexOf(";base64")+8));
    }

    private static void gson(){
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        String s = "{\"status\":0,\"message\":\"操作成功\",\"data\":{\"Token\":\"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ6aXl1bjExMCIsImlhdCI6MTUwMzcyOTMzNywic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTUwNDMzNDEzN30.7YRSsmnWEgdzocTSCwqnGUVs_HXARGyGk5jmtt-oACY\",\"ExpiresIn\":604800}}";
        JsonObject jsonObject = gson.fromJson(s,JsonObject.class);
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.get("status"));
        System.out.println(jsonObject.get("data").getAsJsonObject().get("Token"));

        String d = "{\"status\":0,\"message\":\"操作成功\",\"data\":\"{\\\"Token\\\":\\\"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ6aXl1bjExMCIsImlhdCI6MTUwMzcyOTAzNywic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTUwNDMzMzgzN30.tJQbppKHISFpFLDPtSN_yEdneZjMGjDixn1dfvcu87U\\\",\\\"ExpiresIn\\\":604800}\"}";
        System.out.println(d.replace("\":\"{","\":{").replace("}\"}","}}").replaceAll("\\\"","\""));
        System.out.println("============================================================");
        String a = d.replace("\":\"{","\":{").replace("}\"}","}}").replace("{\\","{").replace("\\\":\\\"","\":\"").replace("\\\",\\\"","\",\"").replace("\\\":","\":");
        System.out.println(a);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        JsonObject j = gson.fromJson("{\"status\":0,\"message\":\"操作成功\",\"data\":{\"Token\":\"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ6aXl1bjExMCIsImlhdCI6MTUwMzczMTk2Miwic3ViIjoidG9rZW4iLCJpc3MiOiJveGNoYWlucyIsImV4cCI6MTUwNDMzNjc2Mn0.zNiK8kWD9oh3Mdy8RDqW1GZ0IiuHS9kWKab7-NtNGv8\",\"ExpiresIn\":604800}}", JsonObject.class);
        System.out.println(jsonObject);
        System.out.println(j.get("message"));
    }

    private static void map(){
        Map<String,String> map =  new HashMap<String,String>();
        map.put("a","avalue");
        map.put("b","bvalue");
        map.put("c","cvalue");
        Iterator it = map.keySet().iterator();
        while(it.hasNext()){
            String key = it.next().toString();
            String value = map.get(key);
            System.out.println(value);;
            if("bvalue".equals(value)){
                map.put(key,"bbbbbbvalue");
            }
        }
        System.out.println(map.toString());
    }

    private static void intdata(){
        String a = "";
        System.out.println(Integer.parseInt(a));
    }


    private static void jsonToObject(){

        String json = "{\"ProductName\":\"310112002939\",\"VehicleInfo\":{\"RoadTransportCertificateNumber\":\"123456789012\",\"TrailerVehiclePlateNumber\":0,\"GoodsInfoList\":[{\"CargoTypeClassificationCode\":\"fresh_fruit\",\"DescriptionOfGoods\":\"??555\"}],\"PermitNumber\":0,\"VehicleTonnage\":10.0,\"LicensePlateTypeCode\":0,\"DriverList\":[{\"QualificationCertificateNumber\":\"\",\"NameOfPerson\":\"???\",\"TelephoneNumber\":\"17603876931\"}],\"VehicleClassificationCode\":0,\"VehicleNumber\":0},\"GoodsTraceList\":[{\"UniqueID\":\"\",\"CommodityCode\":\"\",\"ProductionBatch\":\"\"}],\"GoodsReceiptDateTime\":\"12\",\"TransportState\":\"0\",\"DespatchActualDateTime\":1504062073781,\"ConsignorInfo\":{\"CountrySubdivisionCode\":\"110000000000/110100000000/110101000000\",\"PersonalIdentityDocument\":\"\",\"Consignor\":\"???\"},\"PriceInfo\":{\"TotalMonetaryAmount\":169},\"ConsignmentDateTime\":1504061802877,\"FreeText\":\"\",\"ReportDownLoadUrl\":\"\",\"BusinessTypeCode\":\"1002996\",\"OriginalDocumentNumber\":\"RCD170830105642857\",\"UnifiedSocialCreditIdentifier\":\"11991000010647510A\",\"ConsigneeInfo\":{\"CountrySubdivisionCode\":\"120000000000/120100000000/120101000000\",\"Consignee\":\"?\",\"GoodsReceiptPlace\":\"???/???/???\"},\"Id\":\"e7f450d799af46f586fc6c79f6a5a8cc\",\"InvoiceDownLoadUrl\":\"\",\"Carrier\":\"?????????????\",\"LogisticsTraceList\":[{}],\"UniqueCodes\":[]}";
        try{
            TransportBill transportBill = gson.fromJson(json, TransportBill.class);
            System.out.println(transportBill);
        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }


    }
}
