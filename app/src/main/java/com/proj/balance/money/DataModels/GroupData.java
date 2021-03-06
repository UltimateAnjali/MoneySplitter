package com.proj.balance.money.DataModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;



/**
 * Created by anjali desai on 25-10-2017.
 */

public class GroupData implements Parcelable{

    public String grpName;
    public String grpType;
    public String grpAdmin;
    public String grpKey;
    HashMap<String,String> members = new HashMap<>();
    HashMap<String, Boolean> bills = new HashMap<>();

    public GroupData(){
    }

    public GroupData(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Creator<GroupData> CREATOR = new Creator<GroupData>() {
        @Override
        public GroupData createFromParcel(Parcel in) {
            return new GroupData(in);
        }

        @Override
        public GroupData[] newArray(int size) {
            return new GroupData[size];
        }
    };

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

    public void setMembers(HashMap<String, String> members) {
        this.members = members;
    }

    public void setBills(HashMap<String, Boolean> bills) {
        this.bills = bills;
    }

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

    public HashMap<String, String> getMembers() {
        return members;
    }

    public HashMap<String, Boolean> getBills() {
        return bills;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(grpName);
        parcel.writeString(grpType);
        parcel.writeString(grpAdmin);
        parcel.writeString(grpKey);
        parcel.writeMap(members);
        parcel.writeMap(bills);
    }

    public void readFromParcel(Parcel in) {
        grpName = in.readString();
        grpType = in.readString();
        grpAdmin = in.readString();
        grpKey = in.readString();
        members = in.readHashMap(ClassLoader.getSystemClassLoader());
        bills = in.readHashMap(ClassLoader.getSystemClassLoader());
    }
}
