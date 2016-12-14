package com.example.humax.albumdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import com.benny.library.autoadapter.AutoRecyclerAdapter;
import com.benny.library.autoadapter.SimpleAdapterItemAccessor;
import com.benny.library.autoadapter.viewcreator.ViewCreator;
import com.example.humax.albumdemo.model.Asset;
import com.example.humax.albumdemo.model.ContentType;
import com.example.humax.albumdemo.model.RecordingAsset;
import com.example.humax.albumdemo.model.SameDayMediaFiles;
import com.example.humax.albumdemo.utils.DateUtils;
import com.example.humax.albumdemo.utils.SdUtils;
import com.example.humax.albumdemo.viewholder.WholeSelectViewHolder;
import com.example.humax.albumdemo.viewholder.WholeViewHolder;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.albumlist)
    RecyclerView albumlist;

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.TITLE
    };
    SimpleAdapterItemAccessor<SameDayMediaFiles> itemAccessor = new SimpleAdapterItemAccessor<>();

    private SameDayMediaFiles sameDayMediaFiles;
    private boolean isFirst = true;
    private boolean isSelect = false;
    private boolean isMove = false;
    private boolean needMeasure = true;
    private int distanceY = 0;
    private AutoRecyclerAdapter<SameDayMediaFiles> listAdapter;
    private WholeViewHolder wholeViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.back, R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (isSelect) {
                    List<RecordingAsset> list = SdUtils.readListFromSdCard(this);
                    clear(list);
                    listAdapter = new AutoRecyclerAdapter<>(itemAccessor, new ViewCreator<>(R.layout.adapter_item_album_whole, () -> {
                        wholeViewHolder = new WholeViewHolder(itemAccessor.size());
                        selectListen();
                        return wholeViewHolder;
                    }));
                    fixPosition(distanceY);
                    distanceY = 0;
                    albumlist.setAdapter(listAdapter);
                    isSelect = false;
                    needMeasure = true;
                    share.setVisibility(View.GONE);
                } else {
                    finish();
                }
                break;
            case R.id.share:
                deleteAssets();
                fixPosition(distanceY);
                needMeasure = true;
                distanceY = 0;
                break;
        }
    }

    private void initView() {
        getFiles();
        albumlist = (RecyclerView) findViewById(R.id.albumlist);
        albumlist.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new AutoRecyclerAdapter<>(itemAccessor, new ViewCreator<>(R.layout.adapter_item_album_whole, () -> {
            wholeViewHolder = new WholeViewHolder(itemAccessor.size());
            selectListen();
            return wholeViewHolder;
        }));
        albumlist.setAdapter(listAdapter);
        needMeasure = true;
        albumlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (needMeasure) {
                    distanceY += dy;
                }
            }
        });
    }

    private void clear(List<RecordingAsset> list) {
        if (list != null)
            list.clear();
        SdUtils.writeListIntoSDcard(this, list);
    }

    public void getFiles() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                200);
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        Uri queryUri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(queryUri, PROJECTION, selection, null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

        if (query == null) return;
        long previousTimestamp = -60 * 60 * 24 * 1000;


        while (query.moveToNext()) {
            String path = query.getString(query.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            int mediaType = query.getInt(query.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
            long timestamp = query.getLong(query.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)) * 1000;

            ContentType contentType = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ? ContentType.PHOTO : ContentType.VIDEO;

            String createdAt = DateFormat.format("yyyy/MM/dd", new Date(timestamp)).toString();
            if (isFirst) {
                sameDayMediaFiles = new SameDayMediaFiles(createdAt, new Asset(contentType, path));
                isFirst = false;
            } else {
                if (DateUtils.isSameDay(previousTimestamp, timestamp)) {
                    sameDayMediaFiles.add(new Asset(contentType, path));
                } else {
                    itemAccessor.append(sameDayMediaFiles);
                    sameDayMediaFiles = new SameDayMediaFiles(createdAt, new Asset(contentType, path));
                }
            }
            previousTimestamp = timestamp;
        }
        runOnUiThread(() -> {
            itemAccessor.append(sameDayMediaFiles);
            itemAccessor.notifyDataSetChanged();
        });
    }

    public void selectListen() {
        wholeViewHolder.setOnSelectListener((date, asset) -> {
            needMeasure = false;
            RecordingAsset nowAsset = new RecordingAsset(date, asset.content);
            listAdapter = new AutoRecyclerAdapter<>(itemAccessor, new ViewCreator<>(R.layout.adapter_item_album_whole, () -> new WholeSelectViewHolder(nowAsset)));
            albumlist.setAdapter(listAdapter);
            isMove = true;
            share.setVisibility(View.VISIBLE);
            isSelect = true;
            fixPosition(distanceY);
        });
    }

    private void fixPosition(int diatance) {
        isMove = true;
        albumlist.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isMove) {
                    isMove = false;
                    albumlist.scrollBy(0, diatance);
                }
            }
        });
    }

    private void deleteAssets() {
        delete();
        share.setVisibility(View.GONE);
        listAdapter = new AutoRecyclerAdapter<>(itemAccessor, new ViewCreator<>(R.layout.adapter_item_album_whole, () -> {
            wholeViewHolder = null;
            wholeViewHolder = new WholeViewHolder(itemAccessor.size());
            selectListen();
            return wholeViewHolder;
        }));
        albumlist.setAdapter(listAdapter);
        itemAccessor.notifyDataSetChanged();
        isSelect = false;
    }

    private void delete() {
        List<RecordingAsset> list = SdUtils.readListFromSdCard(this);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                RecordingAsset recordingAsset = list.get(i);
                deleteFile(recordingAsset);
                for (int j = 0; j < itemAccessor.size(); j++) {
                    SameDayMediaFiles sameDayMediaFiles = itemAccessor.get(j);
                    if (sameDayMediaFiles.date.equals(recordingAsset.date)) {
                        List<Asset> assets = sameDayMediaFiles.assets;
                        for (int w = 0; w < assets.size(); w++) {
                            Asset asset = assets.get(w);
                            if (asset.content.equals(recordingAsset.content)) {
                                assets.remove(asset);
                                sameDayMediaFiles.assets = assets;
                                w--;
                            }
                        }
                        if (assets.size() == 0) {
                            itemAccessor.remove(j);
                            j--;
                        }
                    }
                }
            }
        }
    }

    private void deleteFile(RecordingAsset recordingAsset) {
        String path = recordingAsset.content;
        if (!TextUtils.isEmpty(path)) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
            file.delete();
        }
    }
}

