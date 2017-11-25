package com.proj.balance.money;

/**
 * Created by anjali desai on 10-11-2017.
 */

public class AddMembersData {

    String personName;
    String contactNum;
    String imageUrl;
    Boolean isAdded;

    public AddMembersData(){

    }

    public AddMembersData(String pName, String contact, Boolean add){
        this.personName = pName;
        this.contactNum = contact;
        this.isAdded = add;
    }
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContactNum() {return contactNum;}

    public void setContactNum(String contactNum) {this.contactNum = contactNum;}

    public Boolean getAdded() {
        return isAdded;
    }

    public void setAdded(Boolean added) {
        isAdded = added;
    }
}
