package com.example.humax.albumdemo.model;

import com.google.gson.annotations.SerializedName;

public enum ContentType {
    @SerializedName("0")
    TEXT(0),

    @SerializedName("1")
    PHOTO(1),

    @SerializedName("2")
    VOICE(2),

    @SerializedName("3")
    VIDEO(3);

    private int value;
    ContentType(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
