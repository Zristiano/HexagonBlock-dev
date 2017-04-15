package com.example.yuanmengzeng.hexagonblock.download;

/**
 * Created by yuanmengzeng on 2017/4/5.
 */

public interface OnDownloadItemListener
{
    void onDelete(String urlString);

    void onStartDownload(String urlString);

    void onPauseDownload(String urlString);
}
