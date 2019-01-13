package com.example.yuanmengzeng.hexagonblock.Share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.QQ.QQLogin;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 分享dialog Created by yuanmengzeng on 2016/7/7.
 */
public class ShareDialog extends Dialog implements View.OnClickListener
{

    private View wechatFrd, wechatTmLn, qqFrd, qzone;

    private int wechatFrdLoc[] = new int[2]; // 微信好友图初始坐标

    private int wechatTmLnLoc[] = new int[2]; // 微信朋友圈初始坐标

    private int qqFrdLoc[] = new int[2];

    private int qzoneLoc[] = new int[2];

    private int screenHeight, screenwidth;

    private int score; // 游戏得分

    private Activity activity;

    public ShareDialog(Context context, int score)
    {
        super(context, R.style.share_dialog);
        activity = (Activity) context;
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
        qqFrd = findViewById(R.id.qq);
        qzone = findViewById(R.id.qzone);
        showIcon(false);

        View container = findViewById(R.id.share_container);
        container.setLayoutParams(new FrameLayout.LayoutParams(screenwidth, screenHeight));

        container.setOnClickListener(this);
        wechatFrd.setOnClickListener(this);
        wechatTmLn.setOnClickListener(this);
        qqFrd.setOnClickListener(this);
        qzone.setOnClickListener(this);

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
            qqFrd.getLocationInWindow(qqFrdLoc);
            qzone.getLocationInWindow(qzoneLoc);
        }
        if (isEnterAnimEnable)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    startEnterAnim();
                    isEnterAnimEnable = false;
                }
            }, 30);
        }
    }

    private void startEnterAnim()
    {
        if (interpolator == null)
        {
            interpolator = new MyInterpolator();
        }

        TranslateAnimation transAnim_wechat_friend = new TranslateAnimation(0, 0,
                -wechatFrdLoc[1] - wechatFrd.getMeasuredHeight(), 0.0f);
        transAnim_wechat_friend.setInterpolator(interpolator);
        transAnim_wechat_friend.setDuration(300);

        TranslateAnimation transAnim_wechat_timeLine = new TranslateAnimation(0, 0,
                -wechatTmLnLoc[1] - wechatTmLn.getMeasuredHeight(), 0.0f);
        transAnim_wechat_timeLine.setInterpolator(interpolator);
        transAnim_wechat_timeLine.setDuration(300);

        TranslateAnimation tranAnim_qq_friend = new TranslateAnimation(0, 0, screenHeight - qqFrdLoc[1], 0.0f);
        tranAnim_qq_friend.setInterpolator(interpolator);
        tranAnim_qq_friend.setDuration(300);

        TranslateAnimation transAnim_qzone = new TranslateAnimation(0, 0, screenHeight - qzoneLoc[1], 0.0f);
        transAnim_qzone.setInterpolator(interpolator);
        transAnim_qzone.setDuration(300);

        wechatFrd.startAnimation(transAnim_wechat_friend);
        wechatTmLn.startAnimation(transAnim_wechat_timeLine);
        qqFrd.startAnimation(tranAnim_qq_friend);
        qzone.startAnimation(transAnim_qzone);
        showIcon(true);

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
                screenHeight - wechatTmLnLoc[1]);
        transAnim_wechat_timeLine.setInterpolator(new AccelerateInterpolator());
        transAnim_wechat_timeLine.setDuration(300);

        TranslateAnimation transAnim_QQ_friend = new TranslateAnimation(0.0f, 0.0f, 0.0f,
                -qqFrd.getMeasuredHeight() - qqFrdLoc[1]);
        transAnim_QQ_friend.setInterpolator(new AccelerateInterpolator());
        transAnim_QQ_friend.setDuration(300);

        TranslateAnimation transAnim_QZone = new TranslateAnimation(0.0f, 0.0f, 0.0f,
                -qzone.getMeasuredHeight() - qzoneLoc[1]);
        transAnim_QZone.setInterpolator(new AccelerateInterpolator());
        transAnim_QZone.setDuration(300);

        transAnim_QZone.setAnimationListener(new Animation.AnimationListener()
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
                showIcon(false);
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
        qqFrd.startAnimation(transAnim_QQ_friend);
        qzone.startAnimation(transAnim_QZone);

    }

    private void showIcon(boolean isShow)
    {
        int visibility = isShow ? View.VISIBLE : View.INVISIBLE;
        wechatFrd.setVisibility(visibility);
        wechatTmLn.setVisibility(visibility);
        qqFrd.setVisibility(visibility);
        qzone.setVisibility(visibility);
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
                break;
            case R.id.qq:
                shareToQQ(QQshare.SHARE_TO_QQ);
                isEnterAnimEnable = true;
                dismiss();
                break;
            case R.id.qzone:
                shareToQQ(QQshare.SHARE_TO_QZONE);
                // QQLogin();
                isEnterAnimEnable = true;
                dismiss();
                break;
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

    private void shareToQQ(int shareTo)
    {
        QQshare qqShare = new QQshare(activity, shareTo);
        int mp3_num = CommonData.MP3.length;
        int order = (int) (Math.random() * mp3_num);
        order = order < mp3_num ? order : mp3_num - 1;
        qqShare.shareScore(score, order);
    }

    private void QQLogin()
    {
        QQLogin qqLogin = new QQLogin(activity, null);
        qqLogin.login();
    }

    private MyInterpolator interpolator;

    class MyInterpolator extends OvershootInterpolator
    {
        public float getInterpolation(float t)
        {
            t -= 1.0f;
            return t * t * (1.8f * t + 0.8f) + 1.0f;
        }
    }

}
