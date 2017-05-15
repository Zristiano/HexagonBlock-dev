package com.example.yuanmengzeng.hexagonblock.download;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.example.yuanmengzeng.hexagonblock.download.data.ListDownloadedData;
import com.example.yuanmengzeng.hexagonblock.download.data.ListDownloadingData;

import yuanmengzeng.donwload.DownloadItem;

/**
 * <P>
 * 下载列表单项
 * </P>
 * Created by yuanmengzeng on 2017/4/5.
 */

public class DownloadItemView extends FrameLayout implements View.OnClickListener
{

    private final static float DRAG_RATIO = 0.8f;

    private final static float FRICTION_COE = 1.0f;

    /** 删除按钮隐藏 **/
    private final static int DELELTE_BTN_HIDE = 0;

    /** 删除按钮缩回 **/
    private final static int INCOMING_MOTION_RETRACT = 1;

    /** 删除按钮撑满 **/
    private final static int INCOMING_MOTION_BULGE = 2;

    /** 下载内容view左移拉长 **/
    private final static int INCOMING_MOTION_PROLONG = 3;

    /** 删除按钮显示 **/
    private final static int DELETE_BTN_SHOW = 4;

    /** 手指正在屏幕拖动 **/
    private final static int BEING_DRAG = 5;

    private ImageView icon;

    private NumberProgressBar progressBar;

    private ImageView controlBtn;

    private FrameLayout deleteBtn;

    private View itemContent;

    private OnDownloadItemListener listener;

    private ListDownloadedData item;

    private int mTouchSlop;

    private VelocityTracker vTacker;

    private int deleteBtnWidth;

    private int state = DELELTE_BTN_HIDE;

    private float slipDistance = 0.0f;

    private float slipVelocity;

    private boolean pendingRestractAnim = false;

    public DownloadItemView(Context context, OnDownloadItemListener listener)
    {
        super(context);
        this.listener = listener;
        init();
    }

    public DownloadItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public DownloadItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.download_list_item_processing, this);
        icon = (ImageView) findViewById(R.id.download_icon);
        progressBar = (NumberProgressBar) findViewById(R.id.numberProgressBar);
        controlBtn = (ImageView) findViewById(R.id.download_btn);
        deleteBtn = (FrameLayout) findViewById(R.id.item_delete);
        itemContent = findViewById(R.id.item_content);
        ViewGroup.LayoutParams lp = deleteBtn.getLayoutParams();
        deleteBtnWidth = lp.width;
        lp.width = 0;
        deleteBtn.setLayoutParams(lp);
        deleteBtn.setOnClickListener(this);
        controlBtn.setOnClickListener(this);
        icon.setOnClickListener(this);
        vTacker = VelocityTracker.obtain();
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        ZYMLog.info("lp width -> " + lp.width);
    }

    public void setData(ListDownloadedData item)
    {
        deleteBtn.setSelected(item.isCompleted);
        if (item.isCompleted)
        {
            deleteBtn.setBackgroundColor(Color.RED);
        }
        else
        {
            deleteBtn.setBackgroundColor(Color.BLACK);
        }
        this.item = item;
    }

    private float initialDownX = 0.0f;

    private float initialDownY = 0.0f;

    private float lastDownX = 0.0f;

    private float lastDownY = 0.0f;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        ZYMLog.info("onInterceptTouchEvent()");
        int action = ev.getAction();
        LogTouchInfo(action);
        if (!isDragable())
        {
            return super.onInterceptTouchEvent(ev);
        }
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                requestParentDisallowInterceptTouchEvent(true); // 禁止上层view截取事件
                initialDownX = lastDownX = MotionEventCompat.getX(ev, 0);
                initialDownY = lastDownY = MotionEventCompat.getY(ev, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                float nowX = MotionEventCompat.getX(ev, 0);
                float nowY = MotionEventCompat.getY(ev, 0);
                lastDownX = nowX;
                lastDownY = nowY;
                float dx = initialDownX - nowX;
                float aDy = Math.abs(initialDownY - nowY);
                ZYMLog.info("dx->" + dx + "    dy->" + aDy + "    slop->" + mTouchSlop);
                if (dx > mTouchSlop && dx > aDy) // 手指横滑距离大于竖滑距离，截取此触摸事件
                {
                    return true;
                }
                else if (aDy > mTouchSlop && aDy > (dx + 6))
                {
                    requestParentDisallowInterceptTouchEvent(false); // 允许上层view截取事件
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        ZYMLog.info("onTouchEvent() isDragable->" + isDragable());
        int action = event.getAction();
        LogTouchInfo(action);
        if (!isDragable())
        {
            return super.onTouchEvent(event);
        }
        vTacker.addMovement(event);
        switch (action)
        {
            case MotionEvent.ACTION_MOVE:
                float nowX = MotionEventCompat.getX(event, 0);
                float nowY = MotionEventCompat.getY(event, 0);
                float dx = initialDownX - nowX;
                float aDy = Math.abs(initialDownY - nowY);
                ZYMLog.info("dx->" + dx + "    dy->" + aDy + "    slop->" + mTouchSlop + "     isBeingDrag->"
                        + (state == BEING_DRAG));
                if (dx > mTouchSlop && dx > aDy) // 手指横滑距离大于竖滑距离，截取此触摸事件
                {
                    state = BEING_DRAG;
                }
                else if (state != BEING_DRAG && aDy > mTouchSlop && aDy > (dx + 6))
                {
                    requestParentDisallowInterceptTouchEvent(false); // 允许上层view截取事件
                }
                if (state == BEING_DRAG)
                {
                    dragLayout(dx);
                }
                lastDownX = nowX;
                lastDownY = nowY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (state == BEING_DRAG)
                {
                    performIncomingMotion();
                }
                break;
        }
        return true;
    }

    private void dragLayout(float dx)
    {
        ViewGroup.LayoutParams lp = deleteBtn.getLayoutParams();
        lp.width = (int) ((dx - mTouchSlop) * DRAG_RATIO);
        if (lp.width > deleteBtnWidth)
        {
            float x = (lp.width - deleteBtnWidth);
            slipDistance = (float) Math.floor((100 * Math.log((0.01 * x + 1))));
            ZYMLog.info("dragLayout slipDistance -> " + slipDistance);
            itemContent.scrollTo(
                    (int) slipDistance /*
                                        * ((lp.width - deleteBtnWidth) * 0.4)
                                        */, 0);
            lp.width = deleteBtnWidth;
        }
        else if (lp.width < 0)
        {
            lp.width = 0;
        }
        deleteBtn.setLayoutParams(lp);
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

    private void performIncomingMotion()
    {
        vTacker.computeCurrentVelocity(4);
        slipVelocity = vTacker.getXVelocity();
        vTacker.clear();
        ZYMLog.info("velocity X -> " + slipVelocity);
        int nowDeleteBtnWidth = deleteBtn.getLayoutParams().width;
        ZYMLog.info("nowDeleteBtnWidth->" + nowDeleteBtnWidth);
        if (nowDeleteBtnWidth >= deleteBtnWidth)
        {
            state = INCOMING_MOTION_PROLONG;
        }
        else if (nowDeleteBtnWidth >= deleteBtnWidth / 2)
        {
            state = INCOMING_MOTION_BULGE;
        }
        else
        {
            if (slipVelocity < -12.0f)
            {
                state = INCOMING_MOTION_BULGE;
                // slipVelocity += 12.0f;
            }
            else
            {
                state = INCOMING_MOTION_RETRACT;
                // state = INCOMING_MOTION_BULGE;
            }
        }
        invalidate();
    }

    @Override
    public void computeScroll()
    {
        ZYMLog.info("computeScroll()");
        switch (state)
        {
            case INCOMING_MOTION_PROLONG:
                prolongAnim();
                break;
            case INCOMING_MOTION_BULGE:
                bulgeAnim();
                break;
            case INCOMING_MOTION_RETRACT:
                retractAnim();
                break;
            default:
                break;
        }

    }

    private void prolongAnim()
    {
        if (slipVelocity < 0)
        {
            ZYMLog.info("slipVelocity->" + slipVelocity);
            float v = Math.abs(slipVelocity);
            // float a = slipDistance*slipDistance * 0.08f; // 加速度
            float a = 0.0002f * (slipDistance * slipDistance + 20 * slipDistance);
            ZYMLog.info(" acc speed ->" + a);
            a = a > FRICTION_COE ? a : FRICTION_COE;
            v -= a;
            if (v > 0)
            {
                float distance = v / 4; // 此次滑动距离
                itemContent.scrollBy((int) distance, 0);
                slipDistance += distance;
                slipVelocity = -v;
                invalidate();
            }
            else
            {
                recoilBack();
            }
        }
        recoilBack();
    }

    private ValueAnimator recoilAnimator;

    private void recoilBack()
    {
        ZYMLog.info("recoilBack()");
        int left = itemContent.getScrollX();
        ZYMLog.info("scroll x -> " + left);
        if (recoilAnimator == null)
        {
            recoilAnimator = new ValueAnimator();
            recoilAnimator.setInterpolator(new DecelerateInterpolator());
            recoilAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    itemContent.scrollTo((int) animation.getAnimatedValue(), 0);
                }
            });
            recoilAnimator.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    slipDistance = 0.0f;
                    slipVelocity = 0.0f;
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    state = DELETE_BTN_SHOW;
                    if (pendingRestractAnim)
                    {
                        retractAnim();
                    }
                    if (onDeleteBtnShowListener != null)
                    {
                        onDeleteBtnShowListener.onShowHide(DownloadItemView.this, true);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {

                }
            });
        }
        if (recoilAnimator.isRunning())
        {
            return;
        }
        recoilAnimator.setIntValues(left, 0);
        int duration = (left * 500 / 200);
        recoilAnimator.setDuration(duration < 200 ? 200 : duration);
        recoilAnimator.start();
    }

    private ValueAnimator bulgeAnimator;

    private void bulgeAnim()
    {
        if (bulgeAnimator == null)
        {
            bulgeAnimator = new ValueAnimator();
            bulgeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    ViewGroup.LayoutParams lp = deleteBtn.getLayoutParams();
                    lp.width = (Integer) animation.getAnimatedValue();
                    deleteBtn.setLayoutParams(lp);
                }
            });
            bulgeAnimator.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    state = INCOMING_MOTION_BULGE;
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    if (slipVelocity < 0)
                    {
                        state = INCOMING_MOTION_PROLONG; // 进行下一个延长动作
                        postInvalidate();
                    }
                    else
                    {
                        state = DELETE_BTN_SHOW;
                        if (pendingRestractAnim)
                        {
                            retractAnim();
                        }
                        if (onDeleteBtnShowListener != null)
                        {
                            onDeleteBtnShowListener.onShowHide(DownloadItemView.this, true);
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {

                }
            });
        }
        if (bulgeAnimator.isRunning())
        {
            return;
        }
        int deleteBtnNowWidth = deleteBtn.getWidth();
        bulgeAnimator.setIntValues(deleteBtnNowWidth, deleteBtnWidth);
        if (slipVelocity >= 0) // 手指最后一个动作是往回拉
        {
            bulgeAnimator.setInterpolator(new DecelerateInterpolator());
            bulgeAnimator.setDuration((deleteBtnWidth - deleteBtnNowWidth) * 400 / deleteBtnWidth);
            bulgeAnimator.start();
        }
        else
        {
            bulgeAnimator.setInterpolator(new LinearInterpolator());
            float v = Math.abs(slipVelocity);
            int duration = (int) (200 - v * 50 / 10);
            bulgeAnimator.setDuration(duration <= 50 ? 50 : duration);
            bulgeAnimator.start();
        }
    }

    private ValueAnimator retractAnimator;

    private void retractAnim()
    {
        if (retractAnimator == null)
        {
            retractAnimator = new ValueAnimator();
            retractAnimator.setInterpolator(new DecelerateInterpolator());
            retractAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    ViewGroup.LayoutParams lp = deleteBtn.getLayoutParams();
                    lp.width = (Integer) animation.getAnimatedValue();
                    deleteBtn.setLayoutParams(lp);
                }
            });
            retractAnimator.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    state = INCOMING_MOTION_RETRACT;
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    state = DELELTE_BTN_HIDE;
                    pendingRestractAnim = false;
                    if (onDeleteBtnShowListener != null)
                    {
                        onDeleteBtnShowListener.onShowHide(DownloadItemView.this, false);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {

                }
            });
        }
        if (retractAnimator.isRunning())
        {
            return;
        }
        int deleteBtnNowWidth = deleteBtn.getWidth();
        retractAnimator.setIntValues(deleteBtnNowWidth, 0);
        retractAnimator.setDuration(deleteBtnNowWidth * 400 / deleteBtnWidth);
        retractAnimator.start();
    }

    public void hideDelteBtn()
    {
        if (state != DELETE_BTN_SHOW) // 展示删除按钮的动画还未做完就需要将删除按钮回缩
        {
            pendingRestractAnim = true;
        }
        else
        {
            retractAnim();
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept)
    {
        final ViewParent parent = getParent();
        if (parent != null)
        {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private boolean isDragable()
    {
        return state == DELELTE_BTN_HIDE || state == DELETE_BTN_SHOW || state == BEING_DRAG;
    }

    public boolean isTouchDeleteArea(float x, float y)
    {

        Rect frame = new Rect();
        getHitRect(frame);
        x -= frame.left; // 减去当前item的起始范围
        y -= frame.top;
        deleteBtn.getHitRect(frame);
        return frame.contains((int) x, (int) y);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.item_delete:
                // deleteItemAnim(item.url);
                listener.onDelete(item.downloadUrl);
                if (onDeleteBtnShowListener != null)
                {
                    onDeleteBtnShowListener.onShowHide(DownloadItemView.this, false);
                }
                break;
            case R.id.download_icon:
                // listener.onStartDownload(item.url);
                // ViewGroup.LayoutParams lp = deleteBtn.getLayoutParams();
                // lp.width = 100;
                // deleteBtn.setLayoutParams(lp);
                ViewGroup.LayoutParams lp = controlBtn.getLayoutParams();
                lp.width = 20;
                controlBtn.setLayoutParams(lp);
                break;
            case R.id.download_btn:
                listener.onPauseDownload(item.downloadUrl);
        }
    }

    private void deleteItemAnim(String url)
    {
        ValueAnimator animator = ValueAnimator.ofInt(getHeight(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = (int) animation.getAnimatedValue();
                setLayoutParams(lp);
            }
        });
        animator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                listener.onDelete(item.downloadUrl);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                // listener.onDelete(item.url);
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    private OnDeleteBtnShowListener onDeleteBtnShowListener;

    public void setOnDeleteBtnShowListener(OnDeleteBtnShowListener l)
    {
        onDeleteBtnShowListener = l;
    }

    public interface OnDeleteBtnShowListener
    {
        void onShowHide(DownloadItemView view, boolean isShow);
    }
}
