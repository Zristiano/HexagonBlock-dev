package com.example.yuanmengzeng.hexagonblock.QQ;

import com.example.yuanmengzeng.hexagonblock.Account.AccountInfo;

/**
 * 登录监听器 Created by yuanmengzeng on 2016/8/15.
 */
public interface LoginListner
{
    void onLoginSuccess(AccountInfo accountInfo);

    void onLoginFail(String message);

}
