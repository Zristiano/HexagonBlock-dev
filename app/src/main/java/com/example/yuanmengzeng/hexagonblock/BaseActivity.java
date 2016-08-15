package com.example.yuanmengzeng.hexagonblock;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tencent.tauth.Tencent;

/**
 * Created by yuanmengzeng on 2016/8/9.
 */
public class BaseActivity extends FragmentActivity
{
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Tencent.onActivityResultData(requestCode,resultCode,data)
    }
}
