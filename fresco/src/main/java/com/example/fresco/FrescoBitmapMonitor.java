package com.example.fresco;

import android.util.Log;

import com.facebook.imagepipeline.memory.BitmapCounter;
import com.facebook.imagepipeline.memory.BitmapCounterProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by yuanmengzeng on 2017/9/21.
 */

public class FrescoBitmapMonitor
{
    private long totalMemoryInKB = 0;
    private BitmapCounter bitmapCounter;

    private int mInterval = 500;

    private boolean isRunning = false;

    private long lastMillis;

    private int maxBitmapCount = 0;

    private int maxBitmapTotalSize = 0;

    private static class InstanceHolder
    {
        private final static FrescoBitmapMonitor Instance = new FrescoBitmapMonitor();
    }

    public static FrescoBitmapMonitor getInstance()
    {
        return InstanceHolder.Instance;
    }

    private FrescoBitmapMonitor()
    {
        bitmapCounter = BitmapCounterProvider.get();
        checkTotalMemory();
    }

    public void monitor(int interval)
    {
        mInterval = interval;
        if (maxBitmapCount <= 0)
        {
            maxBitmapCount = BitmapCounterProvider.MAX_BITMAP_COUNT;
        }
        if (maxBitmapTotalSize <= 0)
        {
            maxBitmapTotalSize = BitmapCounterProvider.MAX_BITMAP_TOTAL_SIZE;
        }
        if (isRunning)
        {
            return;
        }
        isRunning = true;
        lastMillis = System.currentTimeMillis();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (isRunning)
                {
                    if (System.currentTimeMillis() - lastMillis >= mInterval)
                    {
                        lastMillis = System.currentTimeMillis();
                        Log.i("PPTV Fresco",
                                String.format("bitmap pool  count->%s   maxCount->%s      size->%s   maxSize->%s",
                                        bitmapCounter.getCount(), maxBitmapCount, bitmapCounter.getSize(),
                                        maxBitmapTotalSize));
                    }
                }
            }
        }).start();
    }

    /**
     * 获得系统总内存
     */
    private long checkTotalMemory()
    {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long totalMemoryInKB = 0;
        try
        {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            totalMemoryInKB = Long.valueOf(arrayOfString[1]).intValue();// 获得系统总内存，单位是KB,乘以1024转换为B
            localBufferedReader.close();
        }
        catch (IOException e)
        {
        }
        Log.i("get Total memory", totalMemoryInKB + "");
        this.totalMemoryInKB = totalMemoryInKB;
        return totalMemoryInKB;
    }

    public long getTotalMemoryInKB()
    {
        return totalMemoryInKB;
    }

    public void stop()
    {
        isRunning = false;
    }

}
