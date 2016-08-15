package com.example.yuanmengzeng.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

/**
 * Created by yuanmengzeng on 2016/5/9.
 */
public class ImageLoader
{

    public static void loadPic(ImageView imageView, String Url)
    {
        loadPic(imageView, Url, null);
    }

    public static void loadPic(ImageView imageView, String Url, ImgLoaderCallBack callBack)
    {

        /**
         * 通过缓存加载图片
         */
        Bitmap cache = MemoryCache.getInstance().getBitmap(Url);
        if (cache != null)
        {
            imageView.setImageBitmap(cache);
            if (callBack != null)
                callBack.onImgLoadSuccess();
            return;
        }
        else if ((cache = FileCache.getInstance().getBitmap(Url)) != null)
        {
            imageView.setImageBitmap(cache);
            if (callBack != null)
                callBack.onImgLoadSuccess();
            // 将文件缓存写入内存缓存。
            MemoryCache.getInstance().putBitmap(Url, cache);
            return;
        }
        if (isNetWorkConncted(imageView.getContext()))
        {
            new ImageLoadAsyncTask(imageView, callBack).execute(Url);
        }
        else if (callBack != null)
        {
            callBack.onImgLoadFail();
        }
    }

    public interface ImgLoaderCallBack
    {
        void onImgLoadSuccess();

        void onImgLoadFail();
    }

    public static boolean isNetWorkConncted(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();

    }

}
