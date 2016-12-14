package com.example.humax.albumdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.benny.library.autoadapter.AutoPagerAdapter;
import com.benny.library.autoadapter.SimpleAdapterItemAccessor;
import com.benny.library.autoadapter.viewcreator.ViewCreator;
import com.benny.library.autoadapter.viewholder.DataGetter;
import com.bumptech.glide.Glide;
import com.example.humax.albumdemo.viewholder.BaseViewHolder;
import com.example.humax.albumdemo.widget.HackyViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ImageGalleryActivity extends Activity {
    public static final String KEY_IMAGES = "KEY_IMAGES";
    public static final String KEY_INDEX = "KEY_INDEX";

    @BindView(R.id.pager)
    HackyViewPager vPager;

    List<String> images;
    int index = 0;
    SimpleAdapterItemAccessor<String> itemAccessor = new SimpleAdapterItemAccessor<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            images = getIntent().getStringArrayListExtra(KEY_IMAGES);
            index = getIntent().getIntExtra(KEY_INDEX, 0);
            if (index < 0) index = 0;
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initView() {
        if (images != null) itemAccessor.update(images);
        AutoPagerAdapter<String> adapter = new AutoPagerAdapter<>(itemAccessor, new ViewCreator<>(R.layout.adapter_item_zoom_image, ZoomImageViewHolder::new));
        vPager.setAdapter(adapter);
        vPager.setCurrentItem(index);
    }

    class ZoomImageViewHolder extends BaseViewHolder<String> {
        @BindView(R.id.image)
        PhotoView image;

        @Override
        public void bind(View view) {
            super.bind(view);
            image.setOnViewTapListener((view1, x, y) -> {
                finish();
            });
            itemView.setOnClickListener(v -> {
                finish();
            });
        }

        @Override
        public void onDataChange(DataGetter<String> getter, int position) {
            Glide.with(context).load(getter.data).into(image);
        }
    }
}
