package com.example.yuanmengzeng.hexagonblock.QQ;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by yuanmengzeng on 2016/8/10.
 */
public class QQUiListener implements IUiListener
{
    @Override
    public void onComplete(Object o)
    {
        if (o != null)
        {
            ZYMLog.info("QQ request json object is " + o.toString());
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
