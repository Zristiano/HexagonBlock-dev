package com.example.yuanmengzeng.hexagonblock.Account;

import android.app.Activity;
import android.content.Context;

import com.example.yuanmengzeng.hexagonblock.CommonUtils;
import com.example.yuanmengzeng.hexagonblock.QQ.LoginListner;
import com.example.yuanmengzeng.hexagonblock.QQ.QQLogin;
import com.tencent.connect.common.Constants;

/**
 * 账户信息工具类 Created by yuanmengzeng on 2016/8/15.
 */
public class AccountUtils
{
    public static AccountInfo getAccountInfo(Context context)
    {
        long expire;
        String expireTime = CommonUtils.readPrefsString(context, Constants.PARAM_EXPIRES_IN);
        long systemMillis = System.currentTimeMillis();

        try
        {
            /**
             * 登录Token有效期
             */
            expire = Long.parseLong(expireTime);
            if (expire > systemMillis)
            {
                return new AccountInfo(context);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void Login(Activity activity, LoginListner loginListner)
    {
        QQLogin qqLogin = new QQLogin(activity, loginListner);
        qqLogin.login();
    }
}
