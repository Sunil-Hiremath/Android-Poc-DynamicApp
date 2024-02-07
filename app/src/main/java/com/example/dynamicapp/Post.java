package com.example.dynamicapp;

import com.google.gson.annotations.SerializedName;

public class Post {
    private int userId;

    private Integer id;

    private String title;

    @SerializedName("body") // if json key and variable name is different then we use serializable
    private String text; //in the json we have body as the key

    public Post(int userId, String title, String text) {
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
