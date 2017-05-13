package com.example.yuanmengzeng.hexagonblock.model;

import java.io.Serializable;

/**
 * <p>
 * 升级数据结构
 * </p>
 * Created by yuanmengzeng on 2017/5/12.
 */

public class UpgradeModel implements Serializable
{
    private static final long serialVersionUID = 201705121951L;

    /**
     * 普通升级
     */
    public static final int MODE_NORMAL = 1;

    /**
     * 推荐升级
     */
    public static final int MODE_RECOMMEND = 2;

    /**
     * 强制升级
     */
    public static final int MODE_FORCE = 3;

    public int id;

    public int versionCode;

    /** 升级级别：1->普通升级； 2->推荐升级； 3->强制升级 **/
    public int modelLevel;

    public String versionName;

    public String apkUrl;

    public String tips;

    @Override
    public String toString()
    {
        String formatStr = "{id:%d,version_code:%d,version_name:%s,model:%d,apk:%s,tips:%s}";
        return String.format(formatStr,id,versionCode,versionName,modelLevel,apkUrl,tips);
    }
}
