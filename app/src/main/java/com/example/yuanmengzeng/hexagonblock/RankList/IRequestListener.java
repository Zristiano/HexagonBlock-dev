package com.example.yuanmengzeng.hexagonblock.RankList;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * 网络请求返回回调接口
 *
 * @author <a href="mailto:wentaoli@pptv.com">wentaoli</a>
 * @version 1.0.0
 */
public interface IRequestListener
{

    /**
     * 网络请求成功回调
     *
     * @param result 数据
     */
    void onComplete(HttpUtils.Http.Resp result);

    /**
     * 参数异常
     *
     * @param e 异常
     */
    void onParamsException(Exception e);

    /**
     * IO异常
     *
     * @param e 异常
     */
    void onIOException(IOException e);

    /**
     * URL非法异常
     *
     * @param e 异常
     */
    void onMalformedURLException(MalformedURLException e);

    /**
     * JSON解析异常
     *
     * @param e 异常
     */
    void onJSONException(JSONException e);

    /**
     * 链接超时异常
     *
     * @param e 异常
     */
    void onConnectTimeoutException(ConnectTimeoutException e);

    /**
     * 传输超时异常
     *
     * @param e 异常
     */
    void onSocketTimeoutException(SocketTimeoutException e);

    /**
     * 网络未连接异常
     *
     * @param e 异常
     */
    void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e);

    /**
     * http状态异常
     *
     * @param e 异常
     */
    void onHttpStatusException(HttpUtils.HttpStatusException e);

    /**
     * 未知异常
     *
     * @param e 异常
     */
    void onUnknownException(Exception e);

    /**
     * 空实现
     */
    class SimpleRequestListener implements IRequestListener
    {

        @Override
        public void onComplete(HttpUtils.Http.Resp result)
        {
        }

        @Override
        public void onParamsException(Exception e)
        {
        }

        @Override
        public void onIOException(IOException e)
        {
        }

        @Override
        public void onMalformedURLException(MalformedURLException e)
        {
        }

        @Override
        public void onJSONException(JSONException e)
        {
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e)
        {
        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e)
        {
        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e)
        {
        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e)
        {
        }

        @Override
        public void onUnknownException(Exception e)
        {
        }
    }
}
