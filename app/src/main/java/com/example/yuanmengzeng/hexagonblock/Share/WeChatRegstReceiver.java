package com.example.yuanmengzeng.hexagonblock.Share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.example.yuanmengzeng.hexagonblock.CommonData;

/**
 *
 * Created by yuanmengzeng on 2016/7/4.
 */
public class WeChatRegstReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {

        final IWXAPI api = WXAPIFactory.createWXAPI(context, CommonData.WeChat_APP_ID, true);

        // 将该app注册到微信
        api.registerApp(CommonData.WeChat_APP_ID);
    }
}
