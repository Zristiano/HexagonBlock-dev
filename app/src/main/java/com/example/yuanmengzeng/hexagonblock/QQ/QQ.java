package com.example.yuanmengzeng.hexagonblock.QQ;

import android.app.Activity;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.CommonUtils;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

/**
 * QQ包 Created by yuanmengzeng on 2016/8/9.
 */
public class QQ
{
    protected Tencent mTencent;

    protected Activity mActivity;

    protected String openId; // 用户openId

    protected String accessToken; // 权限token

    protected String expireTime; // 时效

    public static final int SHARE_TO_QQ = 0;

    public static final int SHARE_TO_QZONE = 1;

    public QQ(Activity activity)
    {

        mActivity = activity;
        mTencent = Tencent.createInstance(CommonData.QQ_APP_ID, mActivity.getApplicationContext());
        openId = CommonUtils.readPrefsString(mActivity, Constants.PARAM_OPEN_ID);
        accessToken = CommonUtils.readPrefsString(mActivity, Constants.PARAM_ACCESS_TOKEN);
        // expireTime = CommonUtils.readPrefsString(mActivity,
        // Constants.PARAM_EXPIRES_IN);
        mTencent.setOpenId(openId);
        mTencent.setAccessToken(accessToken, expireTime);
        // if (!TextUtils.isEmpty(expireTime))
        // {
        // mTencent.setAccessToken(accessToken, expireTime);
        // }
        // else
        // {
        // mTencent.setAccessToken(accessToken, null);
        // }
    }

}
