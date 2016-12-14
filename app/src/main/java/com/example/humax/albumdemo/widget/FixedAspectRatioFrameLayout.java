package com.example.humax.albumdemo.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.humax.albumdemo.R;


public class FixedAspectRatioFrameLayout extends FrameLayout {
    /**
     * (width / height)
     */
    private float aspectRatio;

    public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioFrameLayout);
        aspectRatio = a.getFloat(R.styleable.FixedAspectRatioFrameLayout_aspect_ratio, 1.3333f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int receivedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int receivedHeight = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth;
        int measuredHeight;
        boolean widthDynamic;

        if (receivedHeight == 0) {
            widthDynamic = false;
        } else if (receivedWidth == 0) {
            widthDynamic = true;
        } else {
            widthDynamic = receivedHeight * aspectRatio >= receivedWidth;
        }
        if (widthDynamic) {
            int w = (int) (receivedHeight * aspectRatio);
            measuredWidth = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
            measuredHeight = heightMeasureSpec;
        } else {
            measuredWidth = widthMeasureSpec;
            int h = (int) (receivedWidth / aspectRatio);
            measuredHeight = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        super.onMeasure(measuredWidth, measuredHeight);
    }
}
