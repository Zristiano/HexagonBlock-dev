package com.example.yuanmengzeng.hexagonblock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.example.yuanmengzeng.hexagonblock.Account.AccountInfo;
import com.example.yuanmengzeng.hexagonblock.Account.AccountUtils;
import com.example.yuanmengzeng.hexagonblock.Animation.X3DRotation;
import com.example.yuanmengzeng.hexagonblock.CustomView.HexagonImageView;
import com.example.yuanmengzeng.hexagonblock.CustomView.OvalImageView;
import com.example.yuanmengzeng.hexagonblock.QQ.LoginListner;
import com.example.yuanmengzeng.hexagonblock.QQ.QQUiListener;
import com.example.yuanmengzeng.hexagonblock.RankList.RankDialog;
import com.example.yuanmengzeng.imageloader.ImageLoader;
import com.tencent.tauth.Tencent;

/**
 * Created by yuanmengzeng on 2016/6/29.
 */
public class ThirdTestActivity extends FragmentActivity implements View.OnClickListener
{

    private View buzzer3D;

    private View share3D;

    private View restart3D;

    private HexagonImageView hexagonImageView;

    private OvalImageView ovalImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bind_service);
        initView();
    }

    private void initView()
    {
        buzzer3D = findViewById(R.id.buzzer_3d);
        buzzer3D.setOnClickListener(this);

        share3D = findViewById(R.id.share_3d);
        share3D.setOnClickListener(this);

        restart3D = findViewById(R.id.reboot_game_3d);
        restart3D.setOnClickListener(this);

        hexagonImageView = (HexagonImageView) findViewById(R.id.textHexagonImgView);
        ovalImageView = (OvalImageView) findViewById(R.id.textOvalImgView);

        findViewById(R.id.rotate_3D_start_btn).setOnClickListener(this);
    }

    private void startBuzzer3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(300);
        x3DRotation.setInterpolator(new AccelerateInterpolator());
        x3DRotation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                buzzer3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                startRestart3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        buzzer3D.startAnimation(x3DRotation);
    }

    private void startShare3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(300);
        x3DRotation.setInterpolator(new AccelerateInterpolator());
        x3DRotation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                share3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                startBuzzer3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        share3D.startAnimation(x3DRotation);
    }

    private void startRestart3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(300);
        x3DRotation.setInterpolator(new AccelerateInterpolator());
        x3DRotation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                restart3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        restart3D.startAnimation(x3DRotation);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rotate_3D_start_btn:
                // buzzer3D.setVisibility(View.GONE);
                // share3D.setVisibility(View.GONE);
                // restart3D.setVisibility(View.GONE);
                // startShare3DRotation();
                ImageLoader.loadPic(ovalImageView,
                        "http://img5.imgtn.bdimg.com/it/u=4061832848,2725312771&fm=206&gp=0.jpg");
                // new RankDialog().show(getSupportFragmentManager(),null);
                login();
                break;
            default:
                break;
        }
    }

    private void login()
    {
        AccountInfo accountInfo = AccountUtils.getAccountInfo(this);
        if (accountInfo == null)
        {
            AccountUtils.Login(this, new LoginListner()
            {
                @Override
                public void onLoginSuccess(AccountInfo accountInfo)
                {
                    ZYMLog.info(
                            "username is " + accountInfo.getNickname() + "  expire is " + accountInfo.getExpireTime());
                }

                @Override
                public void onLoginFail(String message)
                {
                    ZYMLog.info("onLoginFail : " + message);
                }
            });
        }
        else
        {
            ZYMLog.info("username is " + accountInfo.getNickname() + "  expire is " + accountInfo.getExpireTime());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, new QQUiListener());
    }
}
