package com.example.humax.albumdemo.model;

import java.io.Serializable;

public class RecordingAsset implements Serializable {
    public String date;
    public String content;

    public RecordingAsset(String date, String content) {
        this.date = date;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecordingAsset)) return false;

        RecordingAsset that = (RecordingAsset) o;

        if (!date.equals(that.date)) return false;
        return content.equals(that.content);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }
}
