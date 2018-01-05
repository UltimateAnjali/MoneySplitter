package com.proj.balance.money.DataModels;

/**
 * Created by anjali desai on 25-12-2017.
 */

public class GroupOwingsData {

    String grpKey;
    String grpName;
    String grpType;
    String grpOwing;

    public GroupOwingsData(){

    }
    public String getGrpKey() {
        return grpKey;
    }

    public void setGrpKey(String grpKey) {
        this.grpKey = grpKey;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpType() {
        return grpType;
    }

    public void setGrpType(String grpType) {
        this.grpType = grpType;
    }

    public String getGrpOwing() {
        return grpOwing;
    }

    public void setGrpOwing(String grpOwing) {
        this.grpOwing = grpOwing;
    }
}
