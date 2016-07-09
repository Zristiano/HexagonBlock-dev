package com.example.yuanmengzeng.hexagonblock.Share;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Interpolator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.R;

/**
 * 分享dialog Created by yuanmengzeng on 2016/7/7.
 */
public class ShareDialog extends Dialog implements View.OnClickListener
{

    private View wechatFrd, wechatTmLn;

    private int wechatFrdLoc[] = new int[2]; // 微信好友图初始坐标

    private int wechatTmLnLoc[] = new int[2]; // 微信朋友圈初始坐标

    private int screenHeight, screenwidth;

    private int score; // 游戏得分

    public ShareDialog(Context context, int score)
    {
        super(context, R.style.share_dialog);
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_dialog);
        initView();
    }

    private void initView()
    {

        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        screenwidth = getContext().getResources().getDisplayMetrics().widthPixels;

        wechatFrd = findViewById(R.id.wechat_friend);
        wechatTmLn = findViewById(R.id.wechat_timeLine);

        View container = findViewById(R.id.share_container);
        container.setLayoutParams(new FrameLayout.LayoutParams(screenwidth, screenHeight));

        container.setOnClickListener(this);
        wechatFrd.setOnClickListener(this);
        wechatTmLn.setOnClickListener(this);

        setCanceledOnTouchOutside(true);
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    private boolean isEnterAnimEnable = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if (hasFocus)
        {
            wechatFrd.getLocationInWindow(wechatFrdLoc);
            wechatTmLn.getLocationInWindow(wechatTmLnLoc);
        }
        if (isEnterAnimEnable)
        {
            startEnterAnim();
            isEnterAnimEnable = false;
        }
    }

    private void startEnterAnim()
    {

        TranslateAnimation transAnim_wechat_friend = new TranslateAnimation(0, 0,
                -wechatFrdLoc[1] - wechatFrd.getMeasuredHeight(), 0.0f);
        transAnim_wechat_friend.setInterpolator(new MyInterpolator());
        transAnim_wechat_friend.setDuration(300);

        TranslateAnimation transAnim_wechat_timeLine = new TranslateAnimation(0, 0, screenHeight - wechatTmLnLoc[1],
                0.0f);
        transAnim_wechat_timeLine.setInterpolator(new MyInterpolator());
        transAnim_wechat_timeLine.setDuration(300);

        wechatFrd.startAnimation(transAnim_wechat_friend);
        wechatTmLn.startAnimation(transAnim_wechat_timeLine);

        // ObjectAnimator objAnim_wechat_friend =
        // ObjectAnimator.ofFloat(wechatFrd, "translationY",
        // -wechatFrdLoc[1] - wechatFrd.getMeasuredHeight(), 0.0f);
        //
        // ObjectAnimator objAnim_wechat_timeLine =
        // ObjectAnimator.ofFloat(wechatTmLn, "translationY", screenHeight,
        // 0.0f);
        //
        // AnimatorSet animatorSet = new AnimatorSet();
        // animatorSet.setDuration(300);
        // animatorSet.setInterpolator(new MyInterpolator());
        // animatorSet.playTogether(objAnim_wechat_friend,
        // objAnim_wechat_timeLine);
        // animatorSet.start();

    }

    private boolean exitAnimIsRunning = false;

    private void startExitAnim()
    {
        if (exitAnimIsRunning)
            return;
        TranslateAnimation transAnim_wechat_friend = new TranslateAnimation(0.0f, 0.0f, 0.0f,
                screenHeight - wechatFrdLoc[1]);
        transAnim_wechat_friend.setInterpolator(new AccelerateInterpolator());
        transAnim_wechat_friend.setDuration(300);

        TranslateAnimation transAnim_wechat_timeLine = new TranslateAnimation(0.0f, 0.0f, 0.0f,
                -wechatTmLn.getMeasuredHeight() - wechatTmLnLoc[1]);
        transAnim_wechat_timeLine.setInterpolator(new AccelerateInterpolator());
        transAnim_wechat_timeLine.setDuration(300);

        transAnim_wechat_timeLine.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                exitAnimIsRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                dismiss();
                isEnterAnimEnable = true;
                exitAnimIsRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        wechatFrd.startAnimation(transAnim_wechat_friend);
        wechatTmLn.startAnimation(transAnim_wechat_timeLine);

        // ObjectAnimator objAnim_wechat_friend =
        // ObjectAnimator.ofFloat(wechatFrd, "translationY", 0.0f,
        // screenHeight);
        //
        // ObjectAnimator objAnim_wechat_timeLine =
        // ObjectAnimator.ofFloat(wechatTmLn, "translationY", 0.0f,
        // -wechatTmLn.getMeasuredHeight() - wechatTmLnLoc[1]);
        //
        // AnimatorSet animatorSet = new AnimatorSet();
        // animatorSet.addListener(new Animator.AnimatorListener()
        // {
        // @Override
        // public void onAnimationStart(Animator animation)
        // {
        //
        // }
        //
        // @Override
        // public void onAnimationEnd(Animator animation)
        // {
        // isEnterAnimEnable = true;
        // ShareDialog.this.dismiss();
        // }
        //
        // @Override
        // public void onAnimationCancel(Animator animation)
        // {
        //
        // }
        //
        // @Override
        // public void onAnimationRepeat(Animator animation)
        // {
        //
        // }
        // });
        // animatorSet.setDuration(300);
        // animatorSet.setInterpolator(new DecelerateInterpolator());
        // animatorSet.playTogether(objAnim_wechat_friend,
        // objAnim_wechat_timeLine);
        // animatorSet.start();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.share_container:
                // startExitAnim();
                isEnterAnimEnable = true;
                startExitAnim();
                break;
            case R.id.wechat_friend:
                shareToWechat(WeChat.SHARE_TO_FRIEND);
                isEnterAnimEnable = true;
                dismiss();
                break;
            case R.id.wechat_timeLine:
                shareToWechat(WeChat.SHARE_TO_TIMELINE);
                isEnterAnimEnable = true;
                dismiss();
        }
    }

    private void shareToWechat(int shareTo)
    {
        WeChat weChat = new WeChat(getContext(), shareTo);
        int mp3_num = CommonData.MP3.length;
        int order = (int) (Math.random() * mp3_num);
        order = order < mp3_num ? order : mp3_num - 1;
        weChat.shareScore(score, order);
    }

    class MyInterpolator extends OvershootInterpolator
    {
        public float getInterpolation(float t)
        {
            t -= 1.0f;
            return t * t * (1.8f * t + 0.8f) + 1.0f;
        }
    }

}
