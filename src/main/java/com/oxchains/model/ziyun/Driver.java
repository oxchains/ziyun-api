package com.oxchains.model.ziyun;

import com.oxchains.common.BaseEntity;
import lombok.Data;

/**
 * Driver
 *
 * @author liuruichao
 * Created on 2017/4/6 16:49
 */
@Data
public class Driver extends BaseEntity {
    private String QualificationCertificateNumber; //选填 驾驶员从业资格证号

    private String NameOfPerson; //选填 姓名

    private String TelephoneNumber; //选填 电话号码
}
