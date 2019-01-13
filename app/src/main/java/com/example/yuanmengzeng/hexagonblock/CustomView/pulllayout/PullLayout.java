package com.example.yuanmengzeng.hexagonblock.CustomView.pulllayout;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * <p>
 * </p>
 * Created by yuanmengzeng on 2017/12/12.
 */

public class PullLayout extends FrameLayout
{
    private View contentView;

    private boolean isPullingDown = false;

    private  PullAndRefreshListViewListener pullTodRefreshListener;

    private IPullToRefreshListViewHeader header;

    private boolean mPullRefreshing;

    private Scroller mScroller;

    private int mTouchSlop = 15;

    private int maxHeaderHeight;

    public PullLayout(Context context)
    {
        super(context);
        init();
    }

    public PullLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        // setOrientation(VERTICAL);
        header = new DefautHeaderView(getContext());
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        ZYMLog.info(" add gif header");
//        ((PTRListViewHeader) header).setResId(R.raw.loading_webp);
        addView(header.getView(), 0,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        post(new Runnable()
        {
            @Override
            public void run()
            {
                if (getChildCount()!=2){
                    throw new RuntimeException("PullLayout must has a child");
                }
                contentView = getChildAt(1);
                header.setHeight(0);
            }
        });
        maxHeaderHeight = getResources().getDisplayMetrics().heightPixels / 3; // 下拉刷新头部最多只能到达全屏高度的1/3
    }

    private float lastY;

    private float lastX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        float nowY = ev.getY();
        float nowX = ev.getX();
        float dy = nowY - lastY;
        float dx = nowX - lastX;
        //  ZYMLog.printEvent(ev, " nowX->" + nowY + " dx->" + dx + " nowY->"
        // + nowY + " dy->" + dy);
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                lastY = nowY;
                lastX = nowX;
                break;
            case MotionEvent.ACTION_MOVE:
                lastY = nowY;
                lastX = nowX;
                if (canPullDown(dx, dy))
                {
                    isPullingDown = true;
                    dy = adjustDy(dy, header.getHeight());
                    updateHeaderHeight(dy);
                    return true;
                }
                else
                {
                    isPullingDown = false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isPullingDown)
                {
                    isPullingDown = false;
                    backHeader();
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean canPullDown(float dx, float dy)
    {
        if (header.getHeight() > 0)
        {
            return true;
        }
        boolean canRecyclerViewScrollUp = contentView.canScrollVertically(-1);
        if (dy > Math.abs(dx) && dy > mTouchSlop) // 下滑操作
        {
            return !canRecyclerViewScrollUp;
        }

        return false;
    }

    private float adjustDy(float dy, int nowHeight)
    {
         ZYMLog.info(" dy->%s   nowHeight->%s", dy, nowHeight);

        dy = dy > 0 ? dy * computeRatio(nowHeight) : dy;
        dy = dy > 30 ? 30 : dy;
        dy = dy < -30 ? -30 : dy;
        return dy;
    }

    private float computeRatio(int nowHeight)
    {
        return (float) (maxHeaderHeight - nowHeight) / (float) maxHeaderHeight;
    }

    private void updateHeaderHeight(float delta)
    {
         ZYMLog.info("pull delta->%s   header.height->%s", delta, header.getHeight());
        int headerHeight = (int) delta + header.getHeight();
        header.setHeight(headerHeight);
        ((MarginLayoutParams) contentView.getLayoutParams()).topMargin = headerHeight;
        if (!mPullRefreshing)
        { // 未处于刷新状态，更新箭头
            if (header.isFullyShow())
            {
                header.setState(IPullToRefreshListViewHeader.State.READY);
            }
            else
            {
                header.setState(IPullToRefreshListViewHeader.State.NORMAL);
            }
        }
    }

    private void backHeader()
    {
        if (!mPullRefreshing && header.isFullyShow()) // 开始刷新
        {
            header.setState(IPullToRefreshListViewHeader.State.REFRESHING);
            mPullRefreshing = true;
            if (pullTodRefreshListener != null)
            {
                pullTodRefreshListener.onRefresh();
            }
        }
        resetHeaderHeight();
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight()
    {
        int height = header.getHeight();
        if (!header.isVisible()) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && !header.isFullyShow())
        {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && header.isFullyShow())
        {
            finalHeight = header.getContentHeight();
        }
        mScroller.startScroll(0, height, 0, finalHeight - height, header.getScrollDuration());
        // trigger computeScroll
        invalidate();
    }

    public void stopRefresh()
    {
        if (mPullRefreshing)
        {
            stopRefreshInternal(false);
        }
    }

    private void stopRefreshInternal(boolean immediately)
    {

        if (!immediately && header.needWaitForAnimation())
        {
            header.setOnAnimationStopListener(new IPullToRefreshListViewHeader.OnAnimationStopListener()
            {
                @Override
                public void onAnimationStopped()
                {
                    mPullRefreshing = false;
                    resetHeaderHeight();
                }
            });
        }
        else
        {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
        header.setState(IPullToRefreshListViewHeader.State.STOP);
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            int headerHeight = mScroller.getCurrY();
            ((MarginLayoutParams) contentView.getLayoutParams()).topMargin = headerHeight;
            header.setHeight(headerHeight);
            postInvalidate();
        }
    }

    public void setPullTodRefreshListener( PullAndRefreshListViewListener listener)
    {
        pullTodRefreshListener = listener;
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface PullAndRefreshListViewListener
    {
        void onRefresh();

        void onLoadMore();
    }

    public interface PullAndRefreshCompleteListener
    {
        void onRefreshComplete();
    }
}
