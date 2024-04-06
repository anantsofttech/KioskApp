package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GiftcardResponse {

    public ArrayList<GiftCardModel> getGiftCardModels() {
        return giftCardModels;
    }

    public void setGiftCardModels(ArrayList<GiftCardModel> giftCardModels) {
        this.giftCardModels = giftCardModels;
    }

    private ArrayList<GiftCardModel> giftCardModels;

}
