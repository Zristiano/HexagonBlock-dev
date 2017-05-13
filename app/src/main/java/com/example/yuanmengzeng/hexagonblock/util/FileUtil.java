package com.example.yuanmengzeng.hexagonblock.util;

import android.graphics.PorterDuff;
import android.os.Environment;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <p>
 * 文件工具类
 * </p>
 * Created by yuanmengzeng on 2017/5/13.
 */

public class FileUtil
{
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory() + File.separator + "HexagonBlock";

    private static final String MODEL_DIRECTORY = ROOT_DIR + File.separator + "model";

    public static void writeModelToFile(Class<?> cls, Serializable object)
    {
        try
        {
            String className = cls.getSimpleName();
            File file = new File(MODEL_DIRECTORY);
            if (!file.exists() && !file.mkdirs())
            {
                ZYMLog.error("创建model文件夹失败");
                return;
            }
            file = new File(MODEL_DIRECTORY + File.separator + className);
            if (file.exists() && !file.delete())
            {
                ZYMLog.error("删除已有的" + className + "模型文件失败");
                return;
            }
            if (!file.createNewFile())
            {
                ZYMLog.error("创建" + className + "模型文件失败");
            }
            FileOutputStream fop = new FileOutputStream(file);
            ObjectOutputStream objectOp = new ObjectOutputStream(fop);
            objectOp.writeObject(object);
            objectOp.flush();
            objectOp.close();
        }
        catch (IOException e)
        {
            ZYMLog.error("" + e);
        }
    }

    public static Object readModelFromFile(Class<?> cls)
    {
        try
        {
            File file = new File(MODEL_DIRECTORY + File.separator + cls.getSimpleName());
            if (!file.exists())
            {
                ZYMLog.error(cls.getSimpleName() + "模型文件不存在");
                return null;
            }
            ObjectInputStream objectIp = new ObjectInputStream(new FileInputStream(file));
            Object object = objectIp.readObject();
            objectIp.close();
            return object;
        }
        catch (Exception e)
        {
            ZYMLog.error("" + e);
        }
        return null;
    }

}
