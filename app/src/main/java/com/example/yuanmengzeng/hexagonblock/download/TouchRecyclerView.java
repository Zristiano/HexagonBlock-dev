package com.example.yuanmengzeng.hexagonblock.download;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * <P>
 * 用于查看触摸事件的view
 * </P>
 * Created by yuanmengzeng on 2017/4/6.
 */

public class TouchRecyclerView extends RecyclerView
{

    private DownloadItemView deleteBtnShowView;

    private boolean hasDelelteBtnShown = false;

    private boolean isPerformingHide = false;

    public TouchRecyclerView(Context context)
    {
        super(context);
    }

    public TouchRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public TouchRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    //
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        ZYMLog.error("onInterceptTouchEvent()");
        LogTouchInfo(ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        ZYMLog.error("dispatchTouchEvent()");
        LogTouchInfo(ev.getAction());
        float x = ev.getX();
        float y = ev.getY();
        if (hasDelelteBtnShown && !deleteBtnShowView.isTouchDeleteArea(ev.getX(), ev.getY()))
        {
            deleteBtnShowView.hideDelteBtn();
            isPerformingHide = true;
        }
        return isPerformingHide || super.dispatchTouchEvent(ev);
    }

    private void LogTouchInfo(int action)
    {
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                ZYMLog.error("ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                ZYMLog.error("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                ZYMLog.error("ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
                ZYMLog.error("ACTION_UP");
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        ZYMLog.info("onLayout()");
    }

    public void setDeleteBtnShowView(DownloadItemView itemView, boolean isShow)
    {
        hasDelelteBtnShown = isShow;
        if (isShow)
        {
            deleteBtnShowView = itemView;
        }
        else
        {
            isPerformingHide = false;
            deleteBtnShowView = null;
        }
    }
}
