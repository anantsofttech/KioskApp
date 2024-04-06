package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

public class Drinks {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("ingredients")
    private String ingredients;

    @SerializedName("method")
    private String method;

    @SerializedName("serve")
    private String serve;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getServe() {
        return serve;
    }

    public void setServe(String serve) {
        this.serve = serve;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;

}
