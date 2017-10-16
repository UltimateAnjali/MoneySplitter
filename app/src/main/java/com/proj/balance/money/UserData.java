package com.proj.balance.money;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by anjali desai on 08-10-2017.
 */

public class UserData implements Serializable {

    public static String userName;
    public static String userGivenName;
    public static String userFamilyName;
    public static String userEmail;
    public static String userId;
    public static Uri userPhoto;

    public UserData(){
    }

    public UserData(String mUserName, String mUserGivenName, String mUserFamilyName,
                    String mUserEmail, String mUserId, Uri mUserpic){
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

    public void setUserPhoto(Uri userPhoto) {
        this.userPhoto = userPhoto;
    }


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

    public Uri getUserPhoto() {
        return userPhoto;
    }
}
