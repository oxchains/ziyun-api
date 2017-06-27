package com.oxchains.bean.model.ziyun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oxchains.common.BaseEntity;
import lombok.Data;
import lombok.Generated;

import javax.persistence.*;

/**
 * Created by Luo_xuri on 2017/6/21.
 */
@Data
@Entity
@Table(name = "t_sign")
public class SignData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "hash")
    @JsonProperty("data_hash")
    private String dataHash; // 客户端上传的签名文件的hash值

    @Transient
    @JsonProperty("signature")
    private String signature; // 签名文件的hash值被签名之后返回的hash值

    @Column(name = "nonce")
    private String nonce; // 随机时间

}
