package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

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

    @SerializedName("User")
    private User userInfo;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

}
