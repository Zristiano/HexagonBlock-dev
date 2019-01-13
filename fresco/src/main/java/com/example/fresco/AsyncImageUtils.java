package com.example.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;

/**
 * Created by fanzhang on 2017/11/1.
 *
 */
public class AsyncImageUtils
{
    /**
     * 获取本地图片
     * 
     * @param path
     * @see [类、类#方法、类#成员]
     */
    public static void getLocalBitmap(Context context, String path, BitmapCallback callback)
    {
        if (TextUtils.isEmpty(path))
        {
            return;
        }
        downloadBitmap(context, "file://" + path, callback);
    }

    /**
     * 下载图片到本地，暂不显示
     *
     * @param context ctx
     * @param url 图片url
     * @param callback 获取bitmap的回调
     * @see [类、类#方法、类#成员]
     */
    public static void downloadBitmap(Context context, String url, final BitmapCallback callback)
    {
        if (context != null)
        {
            url = ImgUriUtils.getWebpUrl(context, url);
        }
        if (TextUtils.isEmpty(url))
        {
            return;
        }
        final String urlString = url;
        ImageRequest imageRequest = ImageRequest.fromUri(urlString);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(
                imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber()
        {
            @Override
            protected void onNewResultImpl(Bitmap bitmap)
            {
                if (callback != null)
                {
                    if (bitmap == null)
                    {
                        Log.w(ImgUriUtils.LOG_TAG, "zym --> download bitmap == null");
                        callback.onLoadingFail(urlString);
                        return;
                    }
                    /**
                     * pipeline回调回来的bitmap只能在这个回调函数的范围内使用，此回调函数执行完之后fresco会将此bitmap回收掉，
                     * 这一机制导致用该bitmap创建的BitmapDrawable在每次绘制view时，由于都会访问该bitmap,造成crash，
                     * 所以为避免此问题，利用回调回来的bitmap创建一个新的bitmap供其他对象使用。
                     **/
                    Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), true);
                    if (newBitmap == null)
                    {
                        callback.onLoadingFail(urlString);
                        return;
                    }
                    callback.onLoadingComplete(urlString, newBitmap);
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource)
            {
                if (callback != null)
                {
                    callback.onLoadingFail(urlString);
                }
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }

    /**
     * 清除缓存
     * 
     * @param url 图片url
     * @see [类、类#方法、类#成员]
     */
    public static void removeCache(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return;
        }
        ImagePipeline imgPipeline = Fresco.getImagePipeline();
        imgPipeline.evictFromCache(Uri.parse(url));
    }
}
