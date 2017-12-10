package com.proj.balance.money.DataModels;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by anjali desai on 08-10-2017.
 */

public class UserData implements Serializable {

    public String userName;
    public String userGivenName;
    public String userFamilyName;
    public String userEmail;
    public String userId;
    public String userPhoto;
    public String firebaseUid;
    public HashMap<String,Boolean> groups = new HashMap<>();
    public String userContact;
    public Boolean dataflag;


    public UserData(){
    }

    public UserData(String mUserName, String mUserGivenName, String mUserFamilyName,
                    String mUserEmail, String mUserId, String mUserpic){
        this.userName = mUserName;
        this.userGivenName = mUserGivenName;
        this.userFamilyName = mUserFamilyName;
        this.userEmail = mUserEmail;
        this.userId = mUserId;
        this.userPhoto = mUserpic;
    }

    //Setters
    public void setUserName(String mUserName){
        this.userName = mUserName;
    }

    public void setUserGivenName(String userGivenName) {
        this.userGivenName = userGivenName;
    }

    public void setUserFamilyName(String userFamilyName) {
        this.userFamilyName = userFamilyName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {this.userPhoto = userPhoto;}

    public void setFirebaseUid(String firebaseUid) {this.firebaseUid = firebaseUid;}

    public void setGroups(HashMap<String, Boolean> groups) {this.groups = groups;}

    public void setUserContact(String userContact) {this.userContact = userContact;}

    public void setDataflag(Boolean dataflag) {this.dataflag = dataflag;}


    //Getters
    public String getUserName(){
        return userName;
    }

    public String getUserGivenName() {
        return userGivenName;
    }

    public String getUserFamilyName() {
        return userFamilyName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUserContact() {return userContact;}

    public Boolean getDataflag() {return dataflag;}

    public String getFirebaseUid() {return firebaseUid;}

    public HashMap<String, Boolean> getGroups() {return groups;}
}
