package com.example.yuanmengzeng.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by yuanmengzeng on 2016/5/12.
 */
public class MemoryCache
{

    /**
     * 静态内部类单例模式
     */
    private static class InstanceHolder
    {
        private static MemoryCache INSTANCE = new MemoryCache();
    }

    public static MemoryCache getInstance()
    {
        return InstanceHolder.INSTANCE;
    }

    // 当存储Bitmap的大小大于LruCache设定的值，系统自动释放内存
    private LruCache<String, Bitmap> imageCache;

    private MemoryCache()
    {

        // //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
        long cacheSize = Runtime.getRuntime().maxMemory() / 8;

        imageCache = new LruCache<String, Bitmap>((int) cacheSize)
        {

            // 必须重写此方法，测量bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                return value.getByteCount();
            }
        };
    }

    public Bitmap getBitmap(String url)
    {
        return imageCache.get(url);
    }

    public void putBitmap(String url, Bitmap bitmap)
    {
        if (getBitmap(url) == null)
        {
            imageCache.put(url, bitmap);
        }
    }

}
