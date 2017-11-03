package com.proj.balance.money;

import java.util.List;

/**
 * Created by anjali desai on 25-10-2017.
 */

public class GroupData {

    public String grpName;
    public String grpType;
    public String grpAdmin;
    public int members;
    public List<String> memberNames;

    public GroupData(){
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public void setGrpType(String grpType) {
        this.grpType = grpType;
    }

    public void setGrpAdmin(String grpAdmin) {
        this.grpAdmin = grpAdmin;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public void setMemberNames(List<String> memberNames) {
        this.memberNames = memberNames;
    }

    public String getGrpName() {
        return grpName;
    }

    public String getGrpType() {
        return grpType;
    }

    public String getGrpAdmin() {
        return grpAdmin;
    }

    public int getMembers() {
        return members;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }
}
