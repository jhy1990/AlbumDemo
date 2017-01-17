package com.example.humax.albumdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.humax.albumdemo.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends AppCompatActivity {
    public static final String KEY_VIDEO_PATH = "KEY_VIDEO_PATH";
    @BindView(R.id.player)
    VideoView player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        String stringExtra = getIntent().getStringExtra(KEY_VIDEO_PATH);
        if (TextUtils.isEmpty(stringExtra)) {
            ToastUtil.ToastLong(this,"文件不存在");
        }else {
            player.setVideoPath(stringExtra);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(player);
            player.setMediaController(mediaController);
            player.start();
        }

    }
}
