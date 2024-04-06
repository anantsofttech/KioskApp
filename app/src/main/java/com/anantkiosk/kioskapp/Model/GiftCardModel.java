package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GiftCardModel {

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getGift_Card_Originaly_Sold_On() {
        return Gift_Card_Originaly_Sold_On;
    }

    public void setGift_Card_Originaly_Sold_On(String gift_Card_Originaly_Sold_On) {
        Gift_Card_Originaly_Sold_On = gift_Card_Originaly_Sold_On;
    }

    public String getGift_Card_Purchaser() {
        return Gift_Card_Purchaser;
    }

    public void setGift_Card_Purchaser(String gift_Card_Purchaser) {
        Gift_Card_Purchaser = gift_Card_Purchaser;
    }

    public String getGift_Card_Recipient() {
        return Gift_Card_Recipient;
    }

    public void setGift_Card_Recipient(String gift_Card_Recipient) {
        Gift_Card_Recipient = gift_Card_Recipient;
    }

    public String getOriginal_purchase_amount() {
        return Original_purchase_amount;
    }

    public void setOriginal_purchase_amount(String original_purchase_amount) {
        Original_purchase_amount = original_purchase_amount;
    }

    public String getBalance_remaining() {
        return Balance_remaining;
    }

    public void setBalance_remaining(String balance_remaining) {
        Balance_remaining = balance_remaining;
    }

    public String getCard_was_last_used_on() {
        return Card_was_last_used_on;
    }

    public void setCard_was_last_used_on(String card_was_last_used_on) {
        Card_was_last_used_on = card_was_last_used_on;
    }

    public String getInvoice_Card_was_purchased_on() {
        return Invoice_Card_was_purchased_on;
    }

    public void setInvoice_Card_was_purchased_on(String invoice_Card_was_purchased_on) {
        Invoice_Card_was_purchased_on = invoice_Card_was_purchased_on;
    }

    public String getCard_type() {
        return Card_type;
    }

    public void setCard_type(String card_type) {
        Card_type = card_type;
    }

    public String getDollars_used_sofar() {
        return Dollars_used_sofar;
    }

    public void setDollars_used_sofar(String dollars_used_sofar) {
        Dollars_used_sofar = dollars_used_sofar;
    }

    @SerializedName("station")
    private String station;

    @SerializedName("Gift-Card-Originaly-Sold-On")
    private String Gift_Card_Originaly_Sold_On;

    @SerializedName("Gift-Card-Purchaser")
    private String Gift_Card_Purchaser;

    @SerializedName("Gift-Card-Recipient")
    private String Gift_Card_Recipient;

    @SerializedName("Original-purchase-amount")
    private String Original_purchase_amount;

    @SerializedName("Balance-remaining")
    private String Balance_remaining;

    @SerializedName("Card-was-last-used-on")
    private String Card_was_last_used_on;

    @SerializedName("Invoice-Card-was-purchased-on")
    private String Invoice_Card_was_purchased_on;

    @SerializedName("Card_type")
    private String Card_type;

    @SerializedName("Dollars-used-sofar")
    private String Dollars_used_sofar;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    @SerializedName("Points Total")
    private String points;

    @SerializedName("Rewards Total")
    private String rewards;


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @SerializedName("Customer")
    private String customer;
    public ArrayList<Purchase> getArrCategory() {
        return arrCategory;
    }

    public void setArrCategory(ArrayList<Purchase> arrCategory) {
        this.arrCategory = arrCategory;
    }

    @SerializedName("products")
    ArrayList<Purchase> arrCategory;



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


    @SerializedName("category_name")
    private String category_name;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("Program Type")
    private String ProgramType;

    @SerializedName("Invoice Date")
    private String InvoiceDate;

    public String getProgramType() {
        return ProgramType;
    }

    public void setProgramType(String programType) {
        ProgramType = programType;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getInvNo() {
        return InvNo;
    }

    public void setInvNo(String invNo) {
        InvNo = invNo;
    }

    @SerializedName("InvNo")
    private String InvNo;

    public ArrayList<Product> getArrProducts() {
        return arrProducts;
    }

    public void setArrProducts(ArrayList<Product> arrProducts) {
        this.arrProducts = arrProducts;
    }

    @SerializedName("Items")
    ArrayList<Product> arrProducts;
}
