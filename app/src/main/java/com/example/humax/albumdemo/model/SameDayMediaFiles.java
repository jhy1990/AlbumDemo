package com.example.humax.albumdemo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SameDayMediaFiles implements Serializable {
    public List<Asset> assets = new ArrayList<>();
    public String date;

    public SameDayMediaFiles(String date, Asset asset) {
        this.date = date;
        assets.add(asset);
    }

    public void add(Asset asset) {
        assets.add(asset);
    }

    public int size() {
        return assets.size();
    }
}
