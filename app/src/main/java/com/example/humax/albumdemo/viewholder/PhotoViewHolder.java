package com.example.humax.albumdemo.viewholder;

import com.benny.library.autoadapter.viewholder.DataGetter;
import com.bumptech.glide.Glide;
import com.example.humax.albumdemo.R;
import com.example.humax.albumdemo.model.Asset;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;

public class PhotoViewHolder extends BaseViewHolder<Asset> {
    private com.example.humax.albumdemo.model.Asset Asset;

    @BindView(R.id.image)
    RoundedImageView imageView;

    @Override
    public void onDataChange(DataGetter<Asset> getter, int position) {
        Asset = getter.data;
        if (Asset == null) return;
        Glide.with(context).load(Asset.content).centerCrop().into(imageView);
    }
}
