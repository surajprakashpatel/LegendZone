package com.srsoft.legendzone.models;

import android.net.Uri;

public class User {

    private String userName;
    private String userAge;
    private String userEmail;
    private String userId;
    private String userPhoneNumber;
    private String userPincode;
    private Uri userPhotoUrl;

    private String referralId;

    private String referredBy;
    private int totalBalance;


    public User(){

    }



    public User(String userId, String userName, String userAge, String userEmail, String userPhoneNumber, String userPincode, Uri userPhotoUrl, String referralId, String referredBy, Integer totalBalance) {
        this.userName = userName;
        this.userAge = userAge;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPhoneNumber = userPhoneNumber;
        this.userPincode = userPincode;
        this.userPhotoUrl = userPhotoUrl;
        this.referralId = referralId;
        this.referredBy = referredBy;
        this.totalBalance = totalBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserphoneNumber() {
        return userPhoneNumber;
    }

    public void setUserphoneNumber(String userphoneNumber) {
        this.userPhoneNumber = userphoneNumber;
    }

    public String getUserpincode() {
        return userPincode;
    }

    public void setUserpincode(String userpincode) {
        this.userPincode = userpincode;
    }

    public Uri getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(Uri userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getTotalBalance() {
        return totalBalance;
    }
    public String getReferralId() {
        return referralId;
    }

    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }
}
