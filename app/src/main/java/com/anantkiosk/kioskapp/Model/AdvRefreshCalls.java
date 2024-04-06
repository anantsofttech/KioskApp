package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

public class AdvRefreshCalls {

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRequested() {
        return requested;
    }

    public void setRequested(String requested) {
        this.requested = requested;
    }

    public String getSignid() {
        return Signid;
    }

    public void setSignid(String signid) {
        Signid = signid;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    @SerializedName("ResponseXml")
    private String response;

    @SerializedName("RequestXml")
    private String requested;

    @SerializedName("SignID")
    private String Signid;

    @SerializedName("ErrorMessage")
    private String ErrorMessage;

    @SerializedName("DeviceID")
    private String DeviceID;

    public String getStoreNO() {
        return StoreNO;
    }

    public void setStoreNO(String storeNO) {
        StoreNO = storeNO;
    }

    @SerializedName("StoreNO")
    private String StoreNO;

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    @SerializedName("CreateDate")
    private String CreateDate;

}
