package com.oxchains.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * VehicleInfo
 *
 * @author liuruichao
 * Created on 2017/4/6 16:45
 */
@Data
public class VehicleInfo extends BaseEntity {
    private String RoadTransportCertificateNumber; //车辆道路运输证号

    private String PermitNumber; //选填 许可证编号

    private String VehicleNumber; //车辆牌照号

    private String TrailerVehiclePlateNumber; //选填 挂车牌照号

    private String VehicleClassificationCode; //车辆分类代码 4.2.4

    private String LicensePlateTypeCode; //牌照类型代码 4.2.3

    private Float VehicleTonnage; //车辆载重量(吨)

    private String Owner; //选填 所有人

    private List<GoodsInfo> GoodsInfoList; // 货物信息

    private List<Driver> DriverList; // 驾驶员信息
}
