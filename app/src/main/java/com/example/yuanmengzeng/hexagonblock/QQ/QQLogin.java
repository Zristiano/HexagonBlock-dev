package com.example.yuanmengzeng.hexagonblock.QQ;

import org.json.JSONObject;
import android.app.Activity;

import com.example.yuanmengzeng.hexagonblock.Account.AccountInfo;
import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.CommonUtils;
import com.example.yuanmengzeng.hexagonblock.Share.DiamondDialog;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.UiError;

/**
 * Created by yuanmengzeng on 2016/8/9.
 */
public class QQLogin extends QQ
{

    private String nickname;

    private String avatar;

    private LoginListner loginListner;

    public QQLogin(Activity activity, LoginListner loginListner)
    {
        super(activity);
        this.loginListner = loginListner;
    }

    /**
     * 登录，获取qq的OpenId和Token
     */
    public void login()
    {
        if (!mTencent.isSessionValid())
        {
            mTencent.login(mActivity, CommonData.QQ_SCOPE, new QQUiListener()
            {
                @Override
                public void onComplete(Object o)
                {
                    super.onComplete(o);
                    if (o != null && o instanceof JSONObject)
                    {
                        JSONObject json = (JSONObject) o;
                        if (json.optInt("ret") == 0)
                        {
                            openId = json.optString(Constants.PARAM_OPEN_ID);
                            accessToken = json.optString(Constants.PARAM_ACCESS_TOKEN);
                            expireTime = json.optString(Constants.PARAM_EXPIRES_IN);
                            mTencent.setAccessToken(accessToken, expireTime);
                            mTencent.setOpenId(openId);

                            CommonUtils.writePrefsString(mActivity, Constants.PARAM_OPEN_ID, openId);
                            CommonUtils.writePrefsString(mActivity, Constants.PARAM_ACCESS_TOKEN, accessToken);

                            long expire = Long.parseLong(expireTime) * 1000L + System.currentTimeMillis();
                            CommonUtils.writePrefsString(mActivity, Constants.PARAM_EXPIRES_IN, expire + "");
                            /**
                             * 获取用户信息
                             */
                            getUserInfo();
                        }
                    }
                }

                @Override
                public void onError(UiError uiError)
                {
                    super.onError(uiError);
                    if (loginListner != null)
                    {
                        loginListner.onLoginFail(uiError.errorDetail);
                    }
                }
            });
        }
        else
        {
            if (mTencent.isReady())
                getUserInfo();
        }
    }

    public void getUserInfo()
    {
        UserInfo userInfo = new UserInfo(mActivity, mTencent.getQQToken());
        userInfo.getUserInfo(new QQUiListener()
        {
            @Override
            public void onComplete(Object obj)
            {
                super.onComplete(obj);
                if (obj != null && obj instanceof JSONObject)
                {
                    nickname = ((JSONObject) obj).optString("nickname");
                    avatar = ((JSONObject) obj).optString("figureurl_qq_1");
                    CommonUtils.writePrefsString(mActivity, CommonData.NICKNAME, nickname);
                    CommonUtils.writePrefsString(mActivity, CommonData.AVATAR, avatar);
                    if (loginListner != null)
                    {
                        loginListner.onLoginSuccess(new AccountInfo(mActivity));
                    }
                }
            }

            @Override
            public void onError(UiError uiError)
            {
                super.onError(uiError);
                if (loginListner != null)
                {
                    loginListner.onLoginFail(uiError.errorDetail);
                }
            }
        });
    }

}
