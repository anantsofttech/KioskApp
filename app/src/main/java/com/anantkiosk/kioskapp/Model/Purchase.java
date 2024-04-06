package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Purchase {

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("category_image")
    private String category_image;

    public ArrayList<Product> getArrProducts() {
        return arrProducts;
    }

    public void setArrProducts(ArrayList<Product> arrProducts) {
        this.arrProducts = arrProducts;
    }

    @SerializedName("products")
    ArrayList<Product> arrProducts;

}
