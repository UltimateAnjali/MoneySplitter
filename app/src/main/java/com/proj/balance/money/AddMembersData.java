package com.proj.balance.money;

/**
 * Created by anjali desai on 10-11-2017.
 */

public class AddMembersData {

    String personName;
    String imageUrl;

    public AddMembersData(){

    }

    public AddMembersData(String pName, String iUrl){
        this.personName = pName;
        this.imageUrl = iUrl;
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
}
