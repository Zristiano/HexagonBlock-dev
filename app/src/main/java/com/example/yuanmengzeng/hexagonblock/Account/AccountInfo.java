package com.example.yuanmengzeng.hexagonblock.Account;

import java.io.Serializable;
import android.content.Context;
import android.provider.SyncStateContract;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.CommonUtils;
import com.tencent.connect.common.Constants;

/**
 * Created by yuanmengzeng on 2016/8/15.
 */
public class AccountInfo implements Serializable
{

    public static final long serialVersionUID = 201608151460L;

    public AccountInfo(Context context)
    {
        openId = CommonUtils.readPrefsString(context, Constants.PARAM_OPEN_ID);
        accessToken = CommonUtils.readPrefsString(context, Constants.PARAM_ACCESS_TOKEN);
        expireTime = CommonUtils.readPrefsString(context, Constants.PARAM_EXPIRES_IN);
        nickname = CommonUtils.readPrefsString(context, CommonData.NICKNAME);
        avatar = CommonUtils.readPrefsString(context, CommonData.AVATAR);
    }

    private String openId;

    private String accessToken;

    private String expireTime;

    private String nickname;

    private String avatar;

    public String getOpenId()
    {
        return openId;
    }

    public String getExpireTime()
    {
        return expireTime;
    }

    public String getNickname()
    {
        return nickname;
    }

    public String getAvatar()
    {
        return avatar;
    }
}
