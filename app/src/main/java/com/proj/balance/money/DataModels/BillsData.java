package com.proj.balance.money.DataModels;

import java.util.Currency;
import java.util.HashMap;

/**
 * Created by anjali desai on 19-12-2017.
 */

public class BillsData {

    String description;
    String payer;
    String splitType;
    String totalAmount;
    String currency;
    String billKey;
    String groupKey;
    HashMap<String,String> members = new HashMap<>();

    public BillsData(){

    }

    public String getBillKey() {
        return billKey;
    }

    public void setBillKey(String billKey) {
        this.billKey = billKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HashMap<String, String> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }

    public String getGroupKey() { return groupKey; }

    public void setGroupKey(String groupKey) { this.groupKey = groupKey; }
}
