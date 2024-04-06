package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result {

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @SerializedName("result")
    private String result;

    public String getWebstore() {
        return webstore;
    }

    public void setWebstore(String webstore) {
        this.webstore = webstore;
    }

    @SerializedName("WebServer")
    private String webstore;

    public String getStoreNickName() {
        return StoreNickName;
    }

    public void setStoreNickName(String storeNickName) {
        StoreNickName = storeNickName;
    }

    @SerializedName("StoreNickName")
    private String StoreNickName;

    public ArrayList<Result> getKioskAvailableStors() {
        return KioskAvailableStors;
    }

    public void setKioskAvailableStors(ArrayList<Result> kioskAvailableStors) {
        KioskAvailableStors = kioskAvailableStors;
    }

    @SerializedName("KioskAvailableStors")
    ArrayList<Result> KioskAvailableStors;


}
