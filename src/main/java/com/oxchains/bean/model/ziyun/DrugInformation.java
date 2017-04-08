package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * DrugInformation
 *
 * @author liuruichao
 * Created on 2017/4/6 13:35
 */
public class DrugInformation extends BaseEntity {
    @JsonProperty("DrugName")
    private String DrugName; //药品名称

    @JsonProperty("ApprovalNumber")
    private String ApprovalNumber; //批准文号

    @JsonProperty("Size")
    private String Size; //规格

    @JsonProperty("Form")
    private String Form; //剂型

    @JsonProperty("Manufacturer")
    private String Manufacturer; //生产企业

    @JsonProperty("NDCNumber")
    private String NDCNumber; //药品本位码

    @JsonProperty("NDCNumberRemark")
    private String NDCNumberRemark; //本位码备注

    @JsonProperty("MedicineInstruction")
    private String MedicineInstruction; //使用说明书
}
