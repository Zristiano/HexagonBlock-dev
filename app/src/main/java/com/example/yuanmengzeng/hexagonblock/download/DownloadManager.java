package com.example.yuanmengzeng.hexagonblock.download;

import yuanmengzeng.donwload.DownloadEntity;
import yuanmengzeng.donwload.OnDownloadProgressListener;

/**
 * <P>
 * 下载管理接口
 * </P>
 * Created by yuanmengzeng on 2017/3/27.
 */

public interface DownloadManager
{
    DownloadEntity getEntity(String url);

    void addOnDownloadProgressListener(OnDownloadProgressListener listener);

    void removeOnDownloadProgressListener(OnDownloadProgressListener listener);

    void stop(String url);

}
