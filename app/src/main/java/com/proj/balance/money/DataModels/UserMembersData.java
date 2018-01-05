package com.proj.balance.money.DataModels;

/**
 * Created by anjali desai on 25-12-2017.
 */

public class UserMembersData {
    String userKey;
    String userAmount;
    String userName;

    public UserMembersData(){

    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
