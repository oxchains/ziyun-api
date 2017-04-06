package com.oxchains.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * DrugInformation
 *
 * @author liuruichao
 * Created on 2017/4/6 13:35
 */
@Data
public class DrugInformation extends BaseEntity {
     private String DrugName; //药品名称

     private String ApprovalNumber; //批准文号

     private String Size; //规格

     private String Form; //剂型

     private String Manufacturer; //生产企业

     private String NDCNumber; //药品本位码

     private String NDCNumberRemark; //本位码备注

     private String MedicineInstruction; //使用说明书
}
