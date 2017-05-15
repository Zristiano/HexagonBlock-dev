package com.example.yuanmengzeng.hexagonblock.download.data;

import android.content.Context;

import com.example.yuanmengzeng.hexagonblock.util.DownloadPrefs;

import yuanmengzeng.donwload.DownloadItem;

/**
 * <p>
 * 加入到下载队列的数据
 * </p>
 * Created by yuanmengzeng on 2017/5/15.
 */

public class ListDownloadedData extends BaseDownloadData
{
    public String img;

    public String filePath;

    public long fileSize;

    public boolean isCompleted;

    public ListDownloadedData()
    {

    }

    public ListDownloadedData(DownloadItem item, String img, String title)
    {
        this.img = img;
        this.title = title;
        downloadUrl = item.url;
        filePath = item.filePath;
        fileSize = item.fileSize;
        isCompleted = item.isCompleted;
    }

    public void store(Context ctx)
    {
        DownloadPrefs.setURLImg(ctx, downloadUrl, img);
        DownloadPrefs.setURLTitle(ctx, downloadUrl, title);
    }
}
