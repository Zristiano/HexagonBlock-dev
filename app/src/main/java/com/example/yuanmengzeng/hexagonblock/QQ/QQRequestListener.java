package com.example.yuanmengzeng.hexagonblock.QQ;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;

/**
 * Created by yuanmengzeng on 2016/8/10.
 */
public class QQRequestListener implements IRequestListener
{
    @Override
    public void onComplete(JSONObject jsonObject)
    {
        ZYMLog.info("QQ request json object is " + jsonObject.toString());
    }

    @Override
    public void onIOException(IOException e)
    {
        e.printStackTrace();
    }

    @Override
    public void onMalformedURLException(MalformedURLException e)
    {
        e.printStackTrace();
    }

    @Override
    public void onJSONException(JSONException e)
    {
        e.printStackTrace();
    }

    @Override
    public void onConnectTimeoutException(ConnectTimeoutException e)
    {
        e.printStackTrace();
    }

    @Override
    public void onSocketTimeoutException(SocketTimeoutException e)
    {
        e.printStackTrace();
    }

    @Override
    public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e)
    {
        e.printStackTrace();
    }

    @Override
    public void onHttpStatusException(HttpUtils.HttpStatusException e)
    {
        e.printStackTrace();
    }

    @Override
    public void onUnknowException(Exception e)
    {
        e.printStackTrace();
    }
}
