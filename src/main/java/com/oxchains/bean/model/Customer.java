package com.oxchains.bean.model;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.util.ArrayList;

/**
 * Customer
 *
 * @author liuruichao
 * Created on 2017/3/30 14:45
 */
public class Customer implements User {
    private String name;

    private Enrollment enrollment;

    private ArrayList<String> roles;

    private String account;

    private String affiliation;

    private String mspID;

    public Customer(String name, Enrollment enrollment, ArrayList<String> roles, String account, String affiliation, String mspID) {
        this.name = name;
        this.enrollment = enrollment;
        this.roles = roles;
        this.account = account;
        this.affiliation = affiliation;
        this.mspID = mspID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<String> getRoles() {
        return roles;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String getMSPID() {
        return mspID;
    }
}
