package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * VehicleInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:45
 */
public class VehicleInfo extends BaseEntity {
    @JsonProperty("RoadTransportCertificateNumber")
    private String RoadTransportCertificateNumber; //车辆道路运输证号

    @JsonProperty("PermitNumber")
    private String PermitNumber; //选填 许可证编号

    @JsonProperty("VehicleNumber")
    private String VehicleNumber; //车辆牌照号

    @JsonProperty("TrailerVehiclePlateNumber")
    private String TrailerVehiclePlateNumber; //选填 挂车牌照号

    @JsonProperty("VehicleClassificationCode")
    private String VehicleClassificationCode; //车辆分类代码 4.2.4

    @JsonProperty("LicensePlateTypeCode")
    private String LicensePlateTypeCode; //牌照类型代码 4.2.3

    @JsonProperty("VehicleTonnage")
    private Float VehicleTonnage; //车辆载重量(吨)

    @JsonProperty("Owner")
    private String Owner; //选填 所有人

    @JsonProperty("GoodsInfoList")
    private List<GoodsInfo> GoodsInfoList; // 货物信息

    @JsonProperty("DriverList")
    private List<Driver> DriverList; // 驾驶员信息
}
