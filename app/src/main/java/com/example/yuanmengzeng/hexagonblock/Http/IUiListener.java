package com.example.yuanmengzeng.hexagonblock.Http;

/**
 * 调回API后的回调接口，所有回调方法均在主线程中执行
 *
 * @author <a href="mailto:wentaoli@pptv.com">wentaoli</a>
 * @version 1.0.0
 */
public interface IUiListener {

    /**
     * 请求成功结果回调
     *
     * @param result 结果信息
     */
    void onComplete(String result);

    /**
     * 请求出现错误后回调
     *
     * @param error error
     */
    void onError(String error);

    /**
     * 请求取消回调
     */
    void onCancel();
}
