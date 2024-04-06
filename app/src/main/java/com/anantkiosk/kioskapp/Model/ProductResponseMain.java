package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductResponseMain {

    public ArrayList<Product> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(ArrayList<Product> userInfo) {
        this.userInfo = userInfo;
    }

    @SerializedName("InnerSubModule")
    private ArrayList<Product> userInfo;

}
