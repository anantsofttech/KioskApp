package com.anantkiosk.kioskapp.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class ImagesDetailModel extends AdvSign {

    @JsonProperty("BannerType")
    private String bannerType;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("ImageNo")
    private String imageNo;
    @JsonProperty("imagepath")
    private Object imagepath;
    @JsonProperty("StoreNo")
    private String storeNo;

    @JsonProperty("BannerType")
    public String getBannerType() {
        return bannerType;
    }

    @JsonProperty("BannerType")
    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Image")
    public String getImage() {
        return image;
    }

    @JsonProperty("Image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("ImageNo")
    public String getImageNo() {
        return imageNo;
    }

    @JsonProperty("ImageNo")
    public void setImageNo(String imageNo) {
        this.imageNo = imageNo;
    }

    @JsonProperty("imagepath")
    public Object getImagepath() {
        return imagepath;
    }

    @JsonProperty("imagepath")
    public void setImagepath(Object imagepath) {
        this.imagepath = imagepath;
    }

    @JsonProperty("StoreNo")
    public String getStoreNo() {
        return storeNo;
    }

    @JsonProperty("StoreNo")
    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("start")
    private String start;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @SerializedName("duration")
    private String duration;


    @SerializedName("id")
    private String id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @SerializedName("url")
    private String url;

    public String getSeconds_per_flip() {
        return seconds_per_flip;
    }

    public void setSeconds_per_flip(String seconds_per_flip) {
        this.seconds_per_flip = seconds_per_flip;
    }

    @SerializedName("seconds_per_flip")
    private String seconds_per_flip;

}

