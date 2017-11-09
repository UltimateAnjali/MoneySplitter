package com.proj.balance.money;

import java.util.List;

/**
 * Created by anjali desai on 25-10-2017.
 */

public class GroupData {

    public String grpName;
    public String grpType;
    public String grpAdmin;
    public String grpKey;

    public GroupData(){
    }

    //Setters
    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public void setGrpType(String grpType) {
        this.grpType = grpType;
    }

    public void setGrpAdmin(String grpAdmin) {
        this.grpAdmin = grpAdmin;
    }

    public void setGrpKey(String grpKey) { this.grpKey = grpKey; }

    //Getters
    public String getGrpName() {
        return grpName;
    }

    public String getGrpType() {
        return grpType;
    }

    public String getGrpAdmin() {
        return grpAdmin;
    }

    public String getGrpKey() { return grpKey; }

}
