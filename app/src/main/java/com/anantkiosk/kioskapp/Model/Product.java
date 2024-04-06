package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Product {

    @SerializedName("Description")
    private String product_name;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    @SerializedName("Item Name")
    private String ItemName;

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    @SerializedName("Qty")
    private String Qty;

    public String getQuantityonHand() {
        return QuantityonHand;
    }

    public void setQuantityonHand(String quantityonHand) {
        QuantityonHand = quantityonHand;
    }

    @SerializedName("QuantityonHand")
    private String QuantityonHand;

    @SerializedName("id")
    private String product_id;

    public String getProduct_unit() {
        return product_unit;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }


    public String getDiscountedamount() {
        return discountedamount;
    }

    public void setDiscountedamount(String discountedamount) {
        this.discountedamount = discountedamount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @SerializedName("Size")
    private String product_unit;

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    @SerializedName("ExpactedDate")
    private String expectedDate;

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    @SerializedName("Department")
    private String Department;


    @SerializedName("SKU")
    private String SKU;

    public String getSizeFlag() {
        return SizeFlag;
    }

    public void setSizeFlag(String sizeFlag) {
        SizeFlag = sizeFlag;
    }

    @SerializedName("SizeFlag")
    private String SizeFlag;


    //@SerializedName("PromoPrice")
    @SerializedName(value = "PromoPrice", alternate = {"promoprice","Promotion_Price"})
    private String discountedamount;

    @SerializedName("PromoStart")
    private String PromoStart;

    public String getPromoStart() {
        return PromoStart;
    }

    public void setPromoStart(String promoStart) {
        PromoStart = promoStart;
    }

    public String getPromoEnd() {
        return PromoEnd;
    }

    public void setPromoEnd(String promoEnd) {
        PromoEnd = promoEnd;
    }

    @SerializedName("PromoEnd")
    private String PromoEnd;

    //@SerializedName("Price")
    @SerializedName(value = "Price", alternate = {"price"})
    private String price;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    @SerializedName("image")
    private String product_image;

    public String getBinLocation() {
        return BinLocation;
    }

    public void setBinLocation(String binLocation) {
        BinLocation = binLocation;
    }

    @SerializedName("BinLocation")
    private String BinLocation;

    @SerializedName("InvLargeImage")
    private String InvLargeImage;

    @SerializedName("InvSmallImage")
    private String InvSmallImage;

    @SerializedName("InvLargeImageFullPath")
    private String InvLargeImageFullPath;

    @SerializedName("InvSmallImageFullPath")
    private String InvSmallImageFullPath;

    public String getInvSmallImageFullPath() {
        return InvSmallImageFullPath;
    }

    public void setInvSmallImageFullPath(String invSmallImageFullPath) {
        InvSmallImageFullPath = invSmallImageFullPath;
    }

    public String getInvLargeImageFullPath() {
        return InvLargeImageFullPath;
    }

    public void setInvLargeImageFullPath(String invLargeImageFullPath) {
        InvLargeImageFullPath = invLargeImageFullPath;
    }

    public String getInvLargeImage() {
        return InvLargeImage;
    }

    public void setInvLargeImage(String invLargeImage) {
        InvLargeImage = invLargeImage;
    }

    public String getInvSmallImage() {
        return InvSmallImage;
    }

    public void setInvSmallImage(String invSmallImage) {
        InvSmallImage = invSmallImage;
    }

    public String getExtend_desc() {
        return Extend_desc;
    }

    public void setExtend_desc(String extend_desc) {
        Extend_desc = extend_desc;
    }

    @SerializedName("Extend_desc")
    private String Extend_desc;

    public String getMultipackArray() {
        return multipackArray;
    }

    public void setMultipackArray(String multipackArray) {
        this.multipackArray = multipackArray;
    }

    @SerializedName("MultipackItems")
    private String multipackArray;

}
