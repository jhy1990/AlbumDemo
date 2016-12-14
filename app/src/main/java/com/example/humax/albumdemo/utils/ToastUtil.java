package com.example.humax.albumdemo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

public final class ToastUtil {

    public static void ToastLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void ToastLong(Context context, int resId) {
        Resources res = context.getResources();
        Toast.makeText(context, res.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void ToastShort(Context context, int resId) {
        Resources res = context.getResources();
        Toast.makeText(context, res.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void ToastShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
