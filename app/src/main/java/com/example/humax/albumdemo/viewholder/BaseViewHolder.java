package com.example.humax.albumdemo.viewholder;

import android.content.Context;
import android.view.View;

import com.benny.library.autoadapter.viewholder.IViewHolder;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<T> implements IViewHolder<T> {
    protected Context context;
    public View itemView;

    @Override
    public void bind(View view) {
        itemView = view;
        context = view.getContext();
        ButterKnife.bind(this, view);
    }
}

