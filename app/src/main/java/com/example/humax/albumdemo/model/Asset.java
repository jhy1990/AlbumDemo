package com.example.humax.albumdemo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Asset implements Serializable {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_PICTURE = 1;
    public static final int TYPE_VIDEO = 1;

    public Asset() {}

    public Asset(ContentType contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String id;

    @SerializedName("content_type")
    public ContentType contentType;
    public String content;

    @Override
    public boolean equals(Object o) {
        if(o instanceof Asset) {
            return ((Asset) o).content.equals(content);
        }
        else if(o instanceof String) {
            return content.equals(o);
        }
        return false;
    }
}
