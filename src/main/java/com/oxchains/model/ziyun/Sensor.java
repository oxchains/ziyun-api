package com.oxchains.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 冷链 传感器监控信息
 *
 * @author liuruichao
 * Created on 2017/4/6 16:28
 */
@Data
public class Sensor extends BaseEntity {
     private String SensorNumber; //传感器编号

     private String SensorType; //传感器类型

     private String EquipmentNumber; //设备编号

     private String EquipmentType; //设备类型

     private Long Time; //时间戳

     private List<Float> Temperature; //温度

     private List<Float> Humidity; //湿度

     private Double GPSLongitude; //GPS经度

     private Double GPSLatitude; //GPS纬度

     private String Address; //地址
}