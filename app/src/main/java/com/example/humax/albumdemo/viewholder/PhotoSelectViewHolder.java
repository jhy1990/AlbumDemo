package com.example.humax.albumdemo.viewholder;

import android.widget.CheckBox;

import com.benny.library.autoadapter.viewholder.DataGetter;
import com.bumptech.glide.Glide;
import com.example.humax.albumdemo.R;
import com.example.humax.albumdemo.model.SelectableAsset;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;

public class PhotoSelectViewHolder extends BaseViewHolder<SelectableAsset> {

    @BindView(R.id.image)
    RoundedImageView vImage;

    @BindView(R.id.selected)
    CheckBox vSelected;

    @Override
    public void onDataChange(DataGetter<SelectableAsset> getter, int position) {
        SelectableAsset selectableAsset = getter.data;
        if (selectableAsset == null) return;
        vSelected.setChecked(selectableAsset.selected);
        Glide.with(context).load(selectableAsset.asset.content).centerCrop().into(vImage);
    }
}
