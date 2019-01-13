package com.example.yuanmengzeng.hexagonblock.CustomView.pulllayout;

import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

public interface IPullToRefreshListViewHeader
{
    void setState(State state);

    /**
     * @param height Header的高度
     */
    void setHeight(int height);

    /**
     * @return Header的高度
     */
    int getHeight();

    /**
     * @return 实际内容的固定高度
     */
    int getContentHeight();

    /**
     * @return Header是否可见，即height>0
     */
    boolean isVisible();

    /**
     * @return Header是否完全可见，即height>contentHeight
     */
    boolean isFullyShow();

    /**
     * @return HeaderView
     */
    View getView();

    /**
     * @return 时间
     */
    TextView getTimeView();

    void release();

    int getScrollDuration();

    /**
     * @return 外部listView是否需要等待header的刷新动画执行完毕
     */
    boolean needWaitForAnimation();

    /**
     * 设置header动画执行完毕后的回调
     */
    void setOnAnimationStopListener(OnAnimationStopListener listener);

    /**
     * 设置背景图片
     */
    void setBackgroundImageUrl(String url, int defaultId);

    void setHeaderBackground(@ColorInt int color);

    enum State
    {
        NORMAL, READY, REFRESHING, STOP
    }

    interface OnAnimationStopListener
    {
        void onAnimationStopped();
    }
}
