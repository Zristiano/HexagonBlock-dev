package com.example.yuanmengzeng.hexagonblock.Share;

import java.security.spec.MGF1ParameterSpec;
import java.util.Random;
import java.util.concurrent.RunnableFuture;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信分享 Created by yuanmengzeng on 2016/7/4.
 */
public class WeChat
{

    private Context context;

    int shareType;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public static final int SHARE_TO_FRIEND = 1;

    public static final int SHARE_TO_TIMELINE = 2;

    public WeChat(Context context, int shareType)
    {
        this.context = context;
        this.shareType = shareType;
        api = WXAPIFactory.createWXAPI(context, CommonData.WeChat_APP_ID, false);
        api.registerApp(CommonData.WeChat_APP_ID);
    }

    public void shareScore(int score, int order)
    {

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.julia_share); // R.drawable.julia_share

        WXWebpageObject webObj = new WXWebpageObject(CommonData.WECHAT_SHARE_WEB);

        WXMusicObject musicObj = new WXMusicObject();

        musicObj.musicUrl = CommonData.MP3[order];

        WXMediaMessage msg = new WXMediaMessage();

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();

        msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图

        final SendMessageToWX.Req req = new SendMessageToWX.Req();

        if (shareType == SHARE_TO_FRIEND)
        {
            msg.mediaObject = musicObj;
            msg.title = context.getString(R.string.share_title, "" + score);
            msg.description = context.getString(R.string.share_desc1) + order;
            req.transaction = buildTransaction("music");
        }
        else
        {
            msg.mediaObject = webObj;
            msg.title = context.getString(R.string.share_desc2, score, CommonData.SECOND_STAGE_SOCRE);
            req.transaction = buildTransaction("web");
        }

        req.message = msg;
        req.scene = getShareScene();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                api.sendReq(req);
            }
        }).start();
    }

    private String buildTransaction(final String type)
    {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 分享到会话列表还是朋友圈
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int getShareScene()
    {
        if (shareType == SHARE_TO_FRIEND)
        {
            return SendMessageToWX.Req.WXSceneSession;
        }
        else
        {
            return SendMessageToWX.Req.WXSceneTimeline;
        }
    }
}
