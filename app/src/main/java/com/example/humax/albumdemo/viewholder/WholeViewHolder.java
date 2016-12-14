package com.example.humax.albumdemo.viewholder;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.benny.library.autoadapter.AutoRecyclerAdapter;
import com.benny.library.autoadapter.SimpleAdapterItemAccessor;
import com.benny.library.autoadapter.viewcreator.ViewCreator;
import com.benny.library.autoadapter.viewcreator.ViewCreatorCollection;
import com.benny.library.autoadapter.viewholder.DataGetter;
import com.example.humax.albumdemo.ImageGalleryActivity;
import com.example.humax.albumdemo.R;
import com.example.humax.albumdemo.model.Asset;
import com.example.humax.albumdemo.model.ContentType;
import com.example.humax.albumdemo.model.SameDayMediaFiles;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class WholeViewHolder extends BaseViewHolder<SameDayMediaFiles> {

    @BindView(R.id.tv_month)
    TextView tv_month;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.devide)
    TextView devide;

    private List<Asset> assets;
    private SelectListener selectListener;
    private int totalPosition;

    public WholeViewHolder(int totalPosition){
        this.totalPosition = totalPosition - 1;
    }

    @Override
    public void onDataChange(DataGetter<SameDayMediaFiles> getter, int nowPosition) {
        SameDayMediaFiles sameDayMediaFiles = getter.data;
        if (sameDayMediaFiles == null) return;
        String[] split = sameDayMediaFiles.date.split("/");
        tv_month.setText(split[1] + "月");
        tv_day.setText(split[2] + "日");

        devide.setVisibility(nowPosition == totalPosition ? View.GONE : View.VISIBLE);

        recycle.setLayoutManager(new GridLayoutManager(context,5, GridLayoutManager.VERTICAL, false));
        SimpleAdapterItemAccessor<Asset> itemAccessor = new SimpleAdapterItemAccessor<>();
        assets = sameDayMediaFiles.assets;
        itemAccessor.append(assets);
        ViewCreatorCollection<Asset> collection = new ViewCreatorCollection.Builder<Asset>()
                .addFilter((data, position, itemCount) -> data.contentType == ContentType.PHOTO, new ViewCreator<>(R.layout.adapter_item_album_picture, PhotoViewHolder::new))
                .addFilter((data, position, itemCount) -> data.contentType == ContentType.VIDEO, new ViewCreator<>(R.layout.adapter_item_album_video, VideoPreviewViewHolder::new))
                .build();
        AutoRecyclerAdapter<Asset> adapter = new AutoRecyclerAdapter<>(itemAccessor, collection);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            Asset clicked = itemAccessor.get(position);
            switch (clicked.contentType) {
                case PHOTO:
                    ArrayList<String> photos = getPhotos();
                    context.startActivity(new Intent(context, ImageGalleryActivity.class).putExtra(ImageGalleryActivity.KEY_IMAGES, photos).putExtra(ImageGalleryActivity.KEY_INDEX, photos.indexOf(clicked.content)));
                    break;
                case VIDEO:
                    //context.startActivity(new Intent(context, VideoPlayerActivity.class).putExtra(VideoPlayerActivity.KEY_VIDEO_PATH, clicked.content));
                    break;
            }
        });
        
        adapter.setOnItemLongClickListener((parent, view, position, id) -> {
            Asset clicked = itemAccessor.get(position);
            selectListener.select(sameDayMediaFiles.date,clicked);
            return true;
        });
        recycle.setAdapter(adapter);
    }

    private ArrayList<String> getPhotos(){
        ArrayList<String> photos = new ArrayList<>();
        for (Asset asset : assets) {
            photos.add(asset.content);
        }
        return photos;
    }
    public interface SelectListener{
        void select(String date, Asset asset);
    }
    public void setOnSelectListener(SelectListener selectListener){
        this.selectListener = selectListener;
    }
}
