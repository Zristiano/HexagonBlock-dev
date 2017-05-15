package com.example.yuanmengzeng.hexagonblock.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <p>
 * </p>
 * Created by yuanmengzeng on 2017/5/15.
 */

public class DownloadPrefs
{
    private final static String TITLE = "_title";

    private final static String IMG = "_img";

    private static SharedPreferences getPrefs(Context ctx)
    {
        SharedPreferences sharePrefs = ctx.getSharedPreferences("download", Context.MODE_PRIVATE);
        return sharePrefs;
    }

    private static SharedPreferences.Editor getEditor(Context ctx)
    {
        SharedPreferences.Editor editor = getPrefs(ctx).edit();
        return editor;
    }

    public static void setURLTitle(Context ctx, String Url, String title)
    {
        SharedPreferences.Editor editor = getEditor(ctx);
        editor.putString(Url + TITLE, title);
        editor.apply();
    }

    public static void setURLImg(Context ctx, String Url, String img)
    {
        SharedPreferences.Editor editor = getEditor(ctx);
        editor.putString(Url + IMG, img);
        editor.apply();
    }

    public static String getURLTitle(Context ctx, String Url)
    {
        SharedPreferences prefs = getPrefs(ctx);
        return prefs.getString(Url + TITLE, "");
    }

    public static String getURLImg(Context ctx, String Url)
    {
        SharedPreferences prefs = getPrefs(ctx);
        return prefs.getString(Url + IMG, "");
    }
}
