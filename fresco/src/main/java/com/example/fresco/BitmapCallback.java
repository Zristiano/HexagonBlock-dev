package com.example.fresco;

import android.graphics.Bitmap;

/**
 * <p>
 * Bitmap加载回调
 * </p>
 * Created by yuanmengzeng on 2017/6/14.
 */

public interface BitmapCallback
{
    void onLoadingComplete(String url, Bitmap bitmap);

    void onLoadingFail(String url);
}
