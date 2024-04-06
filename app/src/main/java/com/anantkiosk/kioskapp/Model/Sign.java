package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

public class Sign {


    @SerializedName("enabled")
    private boolean enabled;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("location")
    private String location;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("facing")
    private String facing;

    @SerializedName("read")
    private String read;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("seconds_per_flip")
    private String seconds_per_flip;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSeconds_per_flip() {
        return seconds_per_flip;
    }

    public void setSeconds_per_flip(String seconds_per_flip) {
        this.seconds_per_flip = seconds_per_flip;
    }

    public String getDaily_impressions() {
        return daily_impressions;
    }

    public void setDaily_impressions(String daily_impressions) {
        this.daily_impressions = daily_impressions;
    }

    @SerializedName("daily_impressions")
    private String daily_impressions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;

}
