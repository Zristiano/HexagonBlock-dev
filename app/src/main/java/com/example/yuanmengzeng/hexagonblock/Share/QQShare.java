package com.example.yuanmengzeng.hexagonblock.Share;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.QQ.QQ;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.connect.share.QQShare;

/**
 * QQ、QQ空间分享 Created by yuanmengzeng on 2016/8/9.
 */
public class QQshare extends QQ
{

    private int shareType;

    public QQshare(Activity activity, int shareType)
    {
        super(activity);
        this.shareType = shareType;
    }

    public void shareScore(int score, int order)
    {
        final Bundle params = new Bundle();

        if (shareType == QQshare.SHARE_TO_QQ) // qq好友
        {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, mActivity.getString(R.string.share_title, score));
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mActivity.getString(R.string.share_desc1) + order);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, CommonData.DOWNLOAD_WEB_URL);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, CommonData.SHARE_URL_2);
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mActivity.getString(R.string.app_name));
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, CommonData.MP3[order]);
            mTencent.shareToQQ(mActivity, params, new QQListner());
        }
        else if (shareType == QQshare.SHARE_TO_QZONE) // qq空间
        {
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mActivity.getString(R.string.share_title, score));
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
                    mActivity.getString(R.string.share_desc2, score, CommonData.SECOND_STAGE_SOCRE));
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, CommonData.DOWNLOAD_WEB_URL);
            ArrayList<String> images = new ArrayList<>();
            images.add(CommonData.SHARE_URL_2);
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
            params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, mActivity.getString(R.string.app_name));
            mTencent.shareToQzone(mActivity, params, new QQListner());
        }
    }

    private class QQListner implements IUiListener
    {

        @Override
        public void onComplete(Object o)
        {
            if (o != null)
            {
                ZYMLog.info("ZYM o is " + o.toString());
            }
        }

        @Override
        public void onError(UiError uiError)
        {
            ZYMLog.info("ZYM errorCode is " + uiError.errorCode + "  msg is " + uiError.errorMessage + "  detail is "
                    + uiError.errorDetail);
        }

        @Override
        public void onCancel()
        {

        }
    }

}
