package com.example.yuanmengzeng.hexagonblock.CustomView.pulllayout;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class DefautHeaderView implements IPullToRefreshListViewHeader
{
    private Context mCtx;

    public DefautHeaderView(Context context){
        mCtx = context;
    }

    @Override
    public void setState(State state)
    {

    }

    @Override
    public void setHeight(int height)
    {

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getContentHeight()
    {
        return 0;
    }

    @Override
    public boolean isVisible()
    {
        return false;
    }

    @Override
    public boolean isFullyShow()
    {
        return false;
    }

    @Override
    public View getView()
    {
        return new TextView(mCtx);
    }

    @Override
    public TextView getTimeView()
    {
        return null;
    }

    @Override
    public void release()
    {

    }

    @Override
    public int getScrollDuration()
    {
        return 0;
    }

    @Override
    public boolean needWaitForAnimation()
    {
        return false;
    }

    @Override
    public void setOnAnimationStopListener(OnAnimationStopListener listener)
    {

    }

    @Override
    public void setBackgroundImageUrl(String url, int defaultId)
    {

    }

    @Override
    public void setHeaderBackground(int color)
    {

    }
}
