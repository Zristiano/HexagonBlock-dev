package com.example.yuanmengzeng.imageloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 、 Created by yuanmengzeng on 2016/5/12.
 */
public class FileCache
{

    private static class InstanceHolder
    {
        private static FileCache FILECACHE = new FileCache();
    }

    public static FileCache getInstance()
    {
        return InstanceHolder.FILECACHE;
    }

    private static final String IMAGE_DIRECTORY = "_image";

    private String rootPath;

    private FileCache()
    {
        if (!Environment.getExternalStorageDirectory().exists())
            return;
        rootPath = Environment.getExternalStorageDirectory().getPath() + File.separator
                + getClass().getPackage().getName() + IMAGE_DIRECTORY;

        File imageDirectory = new File(rootPath);
        if (imageDirectory.exists())
            return;
        if (imageDirectory.mkdirs())
        {
            Log.i("ZYM", "image directory is made successfully");
        }
        else
        {
            Log.e("ZYM", "image directory made failure");
        }
    }

    public Bitmap getBitmap(String url)
    {
        url = parseUrl(url);
        File imgFile = new File(rootPath + File.separator + url);
        if (!imgFile.exists())
            return null;
        Bitmap bitmap = BitmapFactory.decodeFile(rootPath + File.separator + url);
        return bitmap;
    }

    public void putBitmap(String url, Bitmap bitmap)
    {
        url = parseUrl(url);
        File imgFile = new File(rootPath + File.separator + url);
        if (imgFile.exists() || bitmap == null)
            return;
        try
        {
            if (imgFile.createNewFile())
            {
                FileOutputStream opStream = new FileOutputStream(imgFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, opStream);
                opStream.flush();
                opStream.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String parseUrl(String url)
    {
        String[] sub = url.split("http:");
        if (sub.length >= 2)
        {
            url = sub[1].replaceAll("[:/.\\\\?=&,]", "");
        }
        else
        {
            url = url.replace("[:/.\\\\?=&,]", "");
        }
        return url;
    }

}
