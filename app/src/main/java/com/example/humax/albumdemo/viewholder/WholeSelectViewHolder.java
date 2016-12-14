package com.example.humax.albumdemo.viewholder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.benny.library.autoadapter.AutoRecyclerAdapter;
import com.benny.library.autoadapter.SimpleAdapterItemAccessor;
import com.benny.library.autoadapter.viewcreator.ViewCreator;
import com.benny.library.autoadapter.viewcreator.ViewCreatorCollection;
import com.benny.library.autoadapter.viewholder.DataGetter;
import com.example.humax.albumdemo.R;
import com.example.humax.albumdemo.model.Asset;
import com.example.humax.albumdemo.model.ContentType;
import com.example.humax.albumdemo.model.RecordingAsset;
import com.example.humax.albumdemo.model.SameDayMediaFiles;
import com.example.humax.albumdemo.model.SelectableAsset;
import com.example.humax.albumdemo.utils.SdUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WholeSelectViewHolder extends BaseViewHolder<SameDayMediaFiles> {

    @BindView(R.id.tv_month)
    TextView tv_month;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    private RecordingAsset chosen;
    private boolean firstAdd = true;

    public WholeSelectViewHolder(RecordingAsset asset) {
        chosen = asset;
    }

    @Override
    public void onDataChange(DataGetter<SameDayMediaFiles> getter, int nowPosition) {
        SameDayMediaFiles sameDayMediaFiles = getter.data;
        List<Asset> assets = sameDayMediaFiles.assets;
        List<SelectableAsset> selectableAssets = new ArrayList<>();
        if (sameDayMediaFiles == null) return;
        String[] split = sameDayMediaFiles.date.split("/");
        tv_month.setText(split[1] + "月");
        tv_day.setText(split[2] + "日");

        recycle.setLayoutManager(new GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false));

        List<RecordingAsset> list = SdUtils.readListFromSdCard(context);
        if (list != null) {
            if (firstAdd) {
                list.add(chosen);
                firstAdd = false;
            }
        } else {
            list = new ArrayList<>();
            list.add(chosen);
            firstAdd = false;
        }
        SdUtils.writeListIntoSDcard(context, list);


        SimpleAdapterItemAccessor<SelectableAsset> itemAccessor = new SimpleAdapterItemAccessor<>();
        String path;
        for (int i = 0; i < assets.size(); i++) {
            boolean select = false;
            path = assets.get(i).content;
            RecordingAsset recordingAsset = new RecordingAsset(sameDayMediaFiles.date, path);
            if (list.contains(recordingAsset)) select = true;
            selectableAssets.add(new SelectableAsset(assets.get(i), select));
        }

        itemAccessor.append(selectableAssets);
        ViewCreatorCollection<SelectableAsset> collection = new ViewCreatorCollection.Builder<SelectableAsset>()
                .addFilter((data, position, itemCount) -> data.asset.contentType == ContentType.PHOTO, new ViewCreator<>(R.layout.adapter_item_album_picture_select, PhotoSelectViewHolder::new))
                .addFilter((data, position, itemCount) -> data.asset.contentType == ContentType.VIDEO, new ViewCreator<>(R.layout.adapter_item_album_video_select, VideoSelectViewHolder::new))
                .build();
        AutoRecyclerAdapter<SelectableAsset> adapter = new AutoRecyclerAdapter<>(itemAccessor, collection);
        adapter.setOnItemClickListener((parent, view, position, id) -> {
            SelectableAsset selectableAsset = itemAccessor.get(position);
            selectableAsset.putToggle();
            itemAccessor.notifyDataSetChanged();

            List<RecordingAsset> readListFromSdCardlist = SdUtils.readListFromSdCard(context);
            if (readListFromSdCardlist == null) {
                readListFromSdCardlist = new ArrayList<>();
            }

            RecordingAsset recordingAsset = new RecordingAsset(sameDayMediaFiles.date, selectableAsset.asset.content);
            if (selectableAsset.selected) {
                readListFromSdCardlist.add(recordingAsset);
            } else {
                readListFromSdCardlist.remove(recordingAsset);
            }

            SdUtils.writeListIntoSDcard(context, readListFromSdCardlist);
        });
        recycle.setAdapter(adapter);
    }
}
