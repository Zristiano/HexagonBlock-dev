package com.example.yuanmengzeng.hexagonblock.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import yuanmengzeng.donwload.DownloadEntity;
import yuanmengzeng.donwload.DownloadUtil;
import yuanmengzeng.donwload.OnDownloadProgressListener;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 下载服务 Created by yuanmengzeng on 2017/3/24.
 */

public class DownloadService extends Service implements DownloadManager
{

    public static final String URL = "DownloadService.URL";

    public static final String THREAD_NUM = "DownloadService.THREAD_NUMBER";

    public static final String DIR = "DownloadService.DIRECTORY";

    private HashMap<String, DownloadEntity> execuTasks = new HashMap<>();

    // private HashMap<String, Integer> idMaps = new HashMap<>();

    // private HashMap<String, OnDownloadProgressListener> listeners = new
    // HashMap<>();
    private LinkedList<OnDownloadProgressListener> listeners = new LinkedList<>();

    private DownloadBinder mBinder = new DownloadBinder();

    /**
     * intent请求服务的id
     */
    private int startId;

    @Override
    public void onCreate()
    {
        Log.e("DownloadService", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e("DownloadService", "onStartCommand");
        Log.i("DownloadService", "startId -> " + startId);
        this.startId = startId;
        addTask(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("DownloadService", "onBind");
        // addTask(intent);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        Log.e("DownloadService", "onUnbind");
        return false; // next bind action will still call onBind() instead of
        // onRebind();
    }

    @Override
    public void onRebind(Intent intent)
    {
        Log.e("DownloadService", "onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy()
    {
        Log.e("DownloadService", "onDestroy");
        super.onDestroy();
    }

    private void addTask(Intent intent)
    {
        String url = intent.getStringExtra(URL);
        if (TextUtils.isEmpty(url))
        {
            return;
        }
        // idMaps.put(url, startId);
        if (!execuTasks.containsKey(url))
        {
            String dir = intent.getStringExtra(DIR);
            int threadNum = intent.getIntExtra(THREAD_NUM, 1);
            DownloadEntity task = new DownloadEntity(this, dir, url, threadNum);
            task.setTimeOut(60000, 20000);
            execuTasks.put(url, task);
            task.setOnDownloadInfoListenr(listener);
            task.start();
        }
    }

    private OnDownloadProgressListener listener = new OnDownloadProgressListener()
    {
        @Override
        public void onNewInfo(String url, long progress, long fileSize)
        {
            for (OnDownloadProgressListener l : listeners)
            {
                l.onNewInfo(url, progress, fileSize);
            }
        }

        @Override
        public void onFinished(String url)
        {
            ZYMLog.info("onFinished(),url->"+url);
            for (OnDownloadProgressListener l : listeners)
            {
                l.onFinished(url);
            }
            execuTasks.remove(url);
            if (execuTasks.size() == 0)
            {
                stopSelf();
            }
            // stopSelf(idMaps.get(url));//
            // 确保服务停止请求始终基于最近的启动请求，避免在执行完一次任务后直接停止当前服务。
            // idMaps.remove(url);
        }

        @Override
        public void onError(String url, int errCode, String msg)
        {
            ZYMLog.error("onError,url->"+url);
            ZYMLog.error("errCode->"+errCode+"   msg->"+msg);
            for (OnDownloadProgressListener l : listeners)
            {
                l.onError(url, errCode, msg);
            }
            execuTasks.remove(url);
            if (execuTasks.size() == 0)
            {
                stopSelf();
            }
            // stopSelf(idMaps.get(url));
            // idMaps.remove(url);
        }
    };

    @Override
    public void addOnDownloadProgressListener(OnDownloadProgressListener l)
    {
        listeners.add(l);
    }

    @Override
    public void removeOnDownloadProgressListener(OnDownloadProgressListener l)
    {
        listeners.remove(l);
    }

    @Override
    public void stop(String url)
    {
        execuTasks.get(url).interrupt();
        execuTasks.remove(url);
    }

    @Override
    public DownloadEntity getEntity(String url)
    {
        return execuTasks.get(url);
    }

    public class DownloadBinder extends Binder
    {
        public WeakReference<DownloadManager> getDownloadManager()
        {
            return new WeakReference<DownloadManager>(DownloadService.this);
        }
    }
}
