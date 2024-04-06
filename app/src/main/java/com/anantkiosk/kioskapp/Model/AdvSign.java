package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

public class AdvSign {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
