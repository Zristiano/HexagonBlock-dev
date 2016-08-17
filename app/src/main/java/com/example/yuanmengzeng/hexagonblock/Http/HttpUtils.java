package com.example.yuanmengzeng.hexagonblock.Http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Http请求工具类
 *
 * @author <a href="mailto:wentaoli@pptv.com">wentaoli</a>
 */
public class HttpUtils
{

    /**
     * http post
     */
    public static final String POST = HttpPost.METHOD_NAME;

    /**
     * http get
     */
    public static final String GET = HttpGet.METHOD_NAME;

    /**
     * http post body不是键值对，而是普通字符串的情况
     */
    public static final String HTTP_POST_BODY_KEY = "____http_post_body_key___";

    /**
     * 默认请求超时时间 30s
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 15000;

    /**
     * 默认响应超时时间 30s
     */
    public static final int DEFAULT_SOCKET_TIMEOUT = 30000;

    /**
     * 网络未连接异常
     */
    public static class NetworkUnavailableException extends Exception
    {
        /**
         * 网络未连接
         */

        public NetworkUnavailableException(String message)
        {
            super(message);
        }
    }

    /**
     * 网络请求状态异常
     */
    public static class HttpStatusException extends Exception
    {

        public HttpStatusException(String message)
        {
            super(message);
        }
    }

    /**
     * http请求返回数据
     */
    public final static class Http
    {
        public static class Resp extends BaseResp
        {
            /**
             * 返回数据
             */
            public String data = "";

            /**
             * 返回的头部
             */
            public Header[] headers;

            public Resp()
            {
            }

            public Resp(String data)
            {
                this.data = data;
            }
        }
    }

    /**
     * 网络请求
     *
     * @param context context
     * @param url 请求url
     * @param params 参数信息
     * @param headers 请求头信息
     * @param method post 或 get
     * @param timeout 超时时间
     * @param listener 回调
     * @see org.apache.http.client.methods.HttpGet
     * @see org.apache.http.client.methods.HttpPost
     */
    public static void requestAsync(final Context context, final String url, final Bundle params, final Bundle headers,
            final String method, final int timeout, final IRequestListener listener)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Http.Resp resp = request(context, url, params, headers, method, timeout);
                    if (resp == null)
                    {
                        if (listener != null)
                        {
                            listener.onUnknownException(new Exception());
                        }
                        ZYMLog.error("requestAsync resp == null ");
                        return;
                    }
                    if (listener != null)
                    {
                        listener.onComplete(resp);
                    }
                }
                catch (NetworkUnavailableException e)
                {
                    if (listener != null)
                    {
                        listener.onNetworkUnavailableException(e);
                    }
                    ZYMLog.error("requestAsync error: " + e);
                }
                catch (MalformedURLException e)
                {
                    if (listener != null)
                    {
                        listener.onMalformedURLException(e);
                    }
                    ZYMLog.error("requestAsync error: " + e);
                }
                catch (SocketTimeoutException e)
                {
                    if (listener != null)
                    {
                        listener.onSocketTimeoutException(e);
                    }
                    ZYMLog.error("requestAsync error: " + e);
                }
                catch (ConnectTimeoutException e)
                {
                    if (listener != null)
                    {
                        listener.onConnectTimeoutException(e);
                    }
                    ZYMLog.error("requestAsync error: " + e);
                }
                catch (IOException e)
                {
                    if (listener != null)
                    {
                        listener.onIOException(e);
                    }
                    ZYMLog.error("requestAsync error: " + e);
                }
                catch (HttpStatusException e)
                {
                    if (listener != null)
                    {
                        listener.onHttpStatusException(e);
                    }
                }
                catch (Exception e)
                {
                    if (listener != null)
                    {
                        listener.onUnknownException(e);
                    }
                    ZYMLog.error("requestAsync error: " + e);
                }
            }
        }).start();
    }

    /**
     * 网络请求
     *
     * @param context context
     * @param url 请求url
     * @param params 参数信息
     * @param headers 请求头信息
     * @param method post 或 get
     * @param timeout 超时时间
     * @return 返回值
     * @throws NetworkUnavailableException 网络不可达
     * @throws IOException 网络IO异常
     * @see org.apache.http.client.methods.HttpGet
     * @see org.apache.http.client.methods.HttpPost
     */
    public static Http.Resp request(Context context, String url, Bundle params, Bundle headers, String method,
            int timeout) throws NetworkUnavailableException, IOException, HttpStatusException
    {
        // 如果url是空
        if (TextUtils.isEmpty(url))
        {
            throw new MalformedURLException("请求URL错误");
        }
        // httpClient
        HttpClient httpClient = getHttpClient();
        HttpParams httpParams = httpClient.getParams();
        timeout = timeout > 0 ? timeout : DEFAULT_CONNECT_TIMEOUT;
        // 设置超时
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);

        // uriRequest
        HttpUriRequest uriRequest = null;
        // 如果是get请求
        if (method.equals(HttpGet.METHOD_NAME))
        {
            // 组装参数列表
            String queryStr = generateQuery(params, true);
            // url
            if (!TextUtils.isEmpty(queryStr))
            {
                if (!url.endsWith("?"))
                {
                    url += "?";
                }
                url += queryStr;
            }
            ZYMLog.info("request url get ->" + url);
            uriRequest = new HttpGet(url);
            // 添加http头信息
            putHeaders(uriRequest, headers);
            // 添加gzip支持
            uriRequest.addHeader("Accept-Encoding", "gzip");
        }
        else if (method.equals(HttpPost.METHOD_NAME))
        {
            ZYMLog.info("request url post ->" + url);
            uriRequest = new HttpPost(url);
            // 添加http头信息
            putHeaders(uriRequest, headers);
            // 添加gzip支持
            uriRequest.addHeader("Accept-Encoding", "gzip");
            if (params != null && !TextUtils.isEmpty(params.getString(HTTP_POST_BODY_KEY)))
            {
                try
                {
                    ((HttpPost) uriRequest).setEntity(new StringEntity(params.getString(HTTP_POST_BODY_KEY)));
                }
                catch (UnsupportedEncodingException e)
                {
                    ZYMLog.error("request setStringEntity UnsupportedEncodingException ");
                }
            }
            else
            {
                // 组装参数列表
                HttpEntity entity = generateEntity(params);
                ((HttpPost) uriRequest).setEntity(entity);
            }
        }
        // 发送请求
        HttpResponse httpResponse = httpClient.execute(uriRequest);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        Http.Resp resp = new Http.Resp();
        // 如果返回结果正常
        if (statusCode == HttpStatus.SC_OK)
        {
            // 请求头
            resp.headers = httpResponse.getAllHeaders();
            // 请求成功
            resp.errorCode = 0;
            //
            resp.message = "成功";
            // 请求返回的数据
            resp.data = getResponseString(httpResponse);
            ZYMLog.info("request execute httpResponse " + resp.toString());
        }
        else
        {
            throw new HttpStatusException("http请求状态错误:" + statusCode);
        }
        return resp;
    }

    /**
     * 组装Http请求头信息
     *
     * @param request 请求
     * @param headers 头信息
     */
    private static void putHeaders(HttpUriRequest request, Bundle headers)
    {
        if (headers != null && !headers.isEmpty())
        {
            for (String key : headers.keySet())
            {
                if (TextUtils.isEmpty(key))
                {
                    continue;
                }
                request.setHeader(key, headers.get(key) + "");
            }
        }
    }

    /**
     * https
     */
    private static DefaultHttpClient getHttpClient()
    {
        try
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        }
        catch (Exception e)
        {
            ZYMLog.error(" getHttpClient:");
        }
        return new DefaultHttpClient();
    }

    /**
     * Read http requests result from response .
     *
     * @param response : http response by executing httpclient
     * @return String : http response content
     */
    private static String getResponseString(HttpResponse response)
    {
        if (response == null)
        {
            return "";
        }
        String result = "";
        HttpEntity entity = response.getEntity();
        if (entity == null)
        {
            return result;
        }

        InputStream inputStream = null;
        ByteArrayOutputStream content = null;
        try
        {
            inputStream = entity.getContent();
            content = new ByteArrayOutputStream();
            Header header = response.getFirstHeader("Content-Encoding");
            // 如果返回的数据使用gzip压缩过的
            if (header != null && header.getValue().toLowerCase().contains("gzip"))
            {
                inputStream = new GZIPInputStream(inputStream);
            }

            // Read response into a buffered stream
            int readBytes;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1)
            {
                content.write(sBuffer, 0, readBytes);
            }
            // Return result from buffered stream
            result = new String(content.toByteArray(), HTTP.UTF_8);
            return result;
        }
        catch (Exception e)
        {
            ZYMLog.error("getResponseString error:");
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (Exception e)
                {
                    // ignore
                }
            }
            if (content != null)
            {
                try
                {
                    content.close();
                }
                catch (Exception e)
                {
                    // ignore
                }
            }
        }
        return "";
    }

    /**
     * 根据post参数生成httpEntity
     *
     * @param params 参数
     * @return httpEntity
     */
    private static HttpEntity generateEntity(Bundle params)
    {
        if (params == null || params.isEmpty())
        {
            return null;
        }

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : params.keySet())
        {
            nameValuePairs.add(new BasicNameValuePair(key, params.getString(key)));
        }
        ZYMLog.info("" + nameValuePairs);
        HttpEntity entity = null;
        try
        {
            entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e)
        {
            ZYMLog.error("generateEntity error ");
        }
        return entity;
    }

    /**
     * 生成http query
     *
     * @param param 参数
     * @param shouldEncode 是否需要encode
     */
    public static String generateQuery(Bundle param, boolean shouldEncode)
    {
        if (param == null || param.isEmpty())
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String key : param.keySet())
        {
            String value = param.getString(key);
            if (value != null)
            {
                if (i == 0)
                {
                    sb.append("");
                }
                else
                {
                    sb.append("&");
                }

                try
                {
                    if (shouldEncode)
                    {
                        sb.append(URLEncoder.encode(key, HTTP.UTF_8)).append("=").append(
                                URLEncoder.encode(value, HTTP.UTF_8));
                    }
                    else
                    {
                        sb.append(key).append("=").append(value);
                    }
                }
                catch (UnsupportedEncodingException e)
                {
                    ZYMLog.error("generateQuery UnsupportedEncodingException :");
                }
            }
            i++;
        }
        ZYMLog.info(sb.toString());
        return sb.toString();
    }

    /**
     * SSL
     */
    static class MySSLSocketFactory extends SSLSocketFactory
    {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
        {
            super(truststore);

            TrustManager tm = new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] {tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException
        {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}
