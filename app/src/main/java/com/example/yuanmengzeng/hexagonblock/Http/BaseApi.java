package com.example.yuanmengzeng.hexagonblock.Http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import com.example.yuanmengzeng.hexagonblock.Http.HttpUtils;
import com.example.yuanmengzeng.hexagonblock.Http.IRequestListener;
import com.example.yuanmengzeng.hexagonblock.Http.IUiListener;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * API接口基类
 *
 * @author <a href="mailto:wentaoli@pptv.com">wentaoli</a>
 * @since 1.0.0
 */
public abstract class BaseApi
{

    /**
     * context
     */
    protected Context mContext;

    /**
     * 初始化
     *
     * @param context context
     */
    public BaseApi(Context context)
    {
        mContext = context;
    }

    /**
     * 默认网络请求回调
     */
    public static class BaseRequestListener implements IRequestListener
    {
        /**
         * 分隔符
         */
        private static final String SPLIT = "-=-=-=-=-=-=";

        /**
         * UI回调
         */
        private final IUiListener mUiListener;

        /**
         * handler
         */
        private final Handler mHandler;

        /**
         * 初始化
         *
         * @param listener ui回调
         */
        public BaseRequestListener(Context context, IUiListener listener)
        {
            this.mUiListener = listener;
            this.mHandler = new Handler(context.getMainLooper())
            {
                public void handleMessage(Message msg)
                {
                    if (mUiListener == null)
                    {
                        return;
                    }
                    ZYMLog.info("request finish == >" + msg);
                    switch (msg.what)
                    {
                        case 0: // 成功
                            if (msg.obj instanceof HttpUtils.Http.Resp)
                            {
                                mUiListener.onComplete(((HttpUtils.Http.Resp) msg.obj).data);
                            }
                            else
                            {
                                mUiListener.onError("未知错误");
                            }
                            break;
                        case 1: // 失败
                            String result = msg.obj + "", shortMsg = "", detailMsg = "";
                            if (!TextUtils.isEmpty(result))
                            {
                                String[] datas = result.split(SPLIT);
                                if (datas.length == 2)
                                {
                                    shortMsg = datas[0];
                                    detailMsg = datas[1];
                                }
                                else
                                {
                                    shortMsg = result;
                                }
                            }
                            mUiListener.onError(shortMsg + "\n" + detailMsg);
                            break;
                    }
                }
            };
        }

        @Override
        public void onComplete(HttpUtils.Http.Resp result)
        {
            mHandler.sendMessage(mHandler.obtainMessage(0, 0, 0, result));
        }

        @Override
        public void onParamsException(Exception e)
        {
            // String error = Constants.Message.REQUEST_PARAMS_ILLEGAL + SPLIT
            // + Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.REQUEST_PARAMS_ILLEGAL, 0, error));
        }

        @Override
        public void onIOException(IOException e)
        {
            // String error = Constants.Message.IO_EXCEPTION + SPLIT +
            // Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.IO_EXCEPTION, 0, error));
        }

        @Override
        public void onMalformedURLException(MalformedURLException e)
        {
            // String error = Constants.Message.REQUEST_URL_ILLEGAL + SPLIT +
            // Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.REQUEST_URL_ILLEGAL, 0, error));
        }

        @Override
        public void onJSONException(JSONException e)
        {
            // String error = Constants.Message.PARSER_JSON_EXCEPTION + SPLIT
            // + Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.PARSER_JSON_EXCEPTION, 0, error));
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e)
        {
            // String error = Constants.Message.NETWORK_CONNECT_TIMEOUT +
            // SPLIT + Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.NETWORK_CONNECT_TIMEOUT, 0, error));
        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e)
        {
            // String error = Constants.Message.NETWORK_SOCKET_TIMEOUT + SPLIT
            // + Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.NETWORK_SOCKET_TIMEOUT, 0, error));
        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e)
        {
            // String error = e.getMessage() + SPLIT +
            // Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.NETWORK_UNAVAILABLE, 0, error));
        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e)
        {
            // String error = e.getMessage() + SPLIT +
            // Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.HTTP_STATUS_EXCEPTION, 0, error));
        }

        @Override
        public void onUnknownException(Exception e)
        {
            // String error = Constants.Message.UNKNOWN + SPLIT +
            // Arrays.toString(e.getStackTrace());
            // mHandler.sendMessage(mHandler.obtainMessage(1,
            // Constants.ErrorCode.UNKNOWN, 0, error));
        }
    }
}
