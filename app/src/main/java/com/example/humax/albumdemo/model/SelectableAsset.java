package com.example.humax.albumdemo.model;

import java.io.Serializable;

public class SelectableAsset implements Serializable {
    public Asset asset;
    public boolean selected = false;

    public SelectableAsset(Asset asset, boolean selected) {
        this.asset = asset;
        this.selected = selected;
    }

    public void putToggle(){
        selected = !selected;
    }
}
