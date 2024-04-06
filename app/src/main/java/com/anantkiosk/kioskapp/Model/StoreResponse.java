package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreResponse {

    @SerializedName("result")
    private boolean result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;

    public ArrayList<Store> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(ArrayList<Store> userInfo) {
        this.userInfo = userInfo;
    }

    @SerializedName("StoreInfo")
    private ArrayList<Store> userInfo;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }


}
