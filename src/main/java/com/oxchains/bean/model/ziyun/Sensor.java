package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;

import java.util.List;

/**
 * 冷链 传感器监控信息
 *
 * @author liuruichao
 * Created on 2017/4/6 16:28
 */
public class Sensor extends BaseEntity {
    @JsonProperty("SensorNumber")
    private String SensorNumber; //传感器编号

    @JsonProperty("SensorType")
    private String SensorType; //传感器类型

    @JsonProperty("EquipmentNumber")
    private String EquipmentNumber; //设备编号

    @JsonProperty("EquipmentType")
    private String EquipmentType; //设备类型

    @JsonProperty("Time")
    private Long Time; //时间戳

    @JsonProperty("Temperature")
    private List<Float> Temperature; //温度

    @JsonProperty("Humidity")
    private List<Float> Humidity; //湿度

    @JsonProperty("GPSLongitude")
    private Double GPSLongitude; //GPS经度

    @JsonProperty("GPSLatitude")
    private Double GPSLatitude; //GPS纬度

    @JsonProperty("Address")
    private String Address; //地址
    
    @JsonProperty("Token")
    private String Token;//token
    
    @JsonIgnore
    public String getToken() {
		return Token;
	}
    
    public void setToken(String Token) {
		this.Token = Token;
	}
}