package com.oxchains.bean.model;

import lombok.Data;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.util.Set;

/**
 * Customer
 *
 * @author liuruichao
 * Created on 2017/3/30 14:45
 */
@Data
public class Customer implements User {
    private String name;

    private Enrollment enrollment;

    private Set<String> roles;

    private String account;

    private String affiliation;

    private String mspID;

    public Customer() {
    }

    public Customer(String name, Enrollment enrollment, Set<String> roles, String account, String affiliation, String mspID) {
        this.name = name;
        this.enrollment = enrollment;
        this.roles = roles;
        this.account = account;
        this.affiliation = affiliation;
        this.mspID = mspID;
    }

    @Override
    public String getMSPID() {
        return mspID;
    }
}
