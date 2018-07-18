package com.example.yuanmengzeng.hexagonblock;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.PopupWindow;

import com.example.yuanmengzeng.hexagonblock.Animation.X3DRotation;
import com.example.yuanmengzeng.hexagonblock.CustomView.CustomWaveView;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.example.yuanmengzeng.hexagonblock.download.DownloadDialog;
import com.example.yuanmengzeng.hexagonblock.download.DownloadManager;
import com.example.yuanmengzeng.hexagonblock.download.DownloadService;
import com.example.yuanmengzeng.hexagonblock.model.UpgradeModel;
import com.example.yuanmengzeng.hexagonblock.util.FileUtil;

import java.lang.ref.WeakReference;
import yuanmengzeng.donwload.OnDownloadProgressListener;

/**
 * 菜单 Created by yuanmengzeng on 2016/7/9.
 */
public class MenuPopWindow extends PopupWindow implements View.OnClickListener
{
    private Context context;

    private View.OnClickListener onClickListener;

    private View buzzer3D;

    private View share3D;

    private View restart3D;

    private View topList3D;

    private View download3D;

    private View divider_1, divider_2, divider_3, divider_4;

    private CustomWaveView waveView;

    private WeakReference<DownloadManager> dlManagerRef;

    private Interpolator interpolator;

    private boolean isbind = false;

    private int animationTime = 200;

    private UpgradeModel upgradeModel;

    public MenuPopWindow(Context context, View.OnClickListener onClickListener)
    {
        super(context);
        this.context = context;
        this.onClickListener = onClickListener;
        initView();
    }

    private void initView()
    {
        View contentView = LayoutInflater.from(context).inflate(R.layout.menu_layout, null);
        setContentView(contentView);
        setFocusable(true);
        setWidth(-2);
        setHeight(-2);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        interpolator = new AccelerateInterpolator();

        divider_1 = contentView.findViewById(R.id.menu_divider_1);
        divider_2 = contentView.findViewById(R.id.menu_divider_2);
        divider_3 = contentView.findViewById(R.id.menu_divider_3);
        divider_4 = contentView.findViewById(R.id.menu_divider_4);

        buzzer3D = contentView.findViewById(R.id.buzzer_3d);
        buzzer3D.setOnClickListener(onClickListener);
        buzzer3D.setTag(true);

        share3D = contentView.findViewById(R.id.share_3d);
        share3D.setOnClickListener(onClickListener);

        restart3D = contentView.findViewById(R.id.reboot_game_3d);
        restart3D.setOnClickListener(onClickListener);

        topList3D = contentView.findViewById(R.id.rank_list);
        topList3D.setOnClickListener(onClickListener);

        download3D = contentView.findViewById(R.id.download);
        download3D.setOnClickListener(onClickListener /* this */);
        waveView = (CustomWaveView) contentView.findViewById(R.id.waveBg);
        Object obj = FileUtil.readModelFromFile(UpgradeModel.class);
        if (obj != null && obj instanceof UpgradeModel) {
            upgradeModel = (UpgradeModel) obj;
            // TODO: 2017/5/13
            if (upgradeModel.versionCode > 2) {
                contentView.findViewById(R.id.red_point).setVisibility(View.VISIBLE);
            } else {
                contentView.findViewById(R.id.red_point).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff)
    {
        divider_1.setVisibility(View.INVISIBLE);
        divider_2.setVisibility(View.INVISIBLE);
        divider_3.setVisibility(View.INVISIBLE);
        divider_4.setVisibility(View.INVISIBLE);
        buzzer3D.setVisibility(View.GONE);
        share3D.setVisibility(View.GONE);
        topList3D.setVisibility(View.GONE);
        restart3D.setVisibility(View.GONE);
        download3D.setVisibility(View.GONE);
        // super.showAsDropDown(anchor, xoff, yoff, gravity);
        super.showAsDropDown(anchor, xoff, yoff);
        startShare3DRotation();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.download:
                download();
                break;
        }
    }

    private void startShare3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(animationTime);
        x3DRotation.setInterpolator(interpolator);
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
                divider_1.setVisibility(View.VISIBLE);
                startBuzzer3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        share3D.startAnimation(x3DRotation);
    }

    private void startBuzzer3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(animationTime);
        x3DRotation.setInterpolator(interpolator);
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
                divider_2.setVisibility(View.VISIBLE);
                startTopList3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        buzzer3D.startAnimation(x3DRotation);
    }

    private void startTopList3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(animationTime);
        x3DRotation.setInterpolator(interpolator);
        x3DRotation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                topList3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                divider_3.setVisibility(View.VISIBLE);
                startDownload3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        topList3D.startAnimation(x3DRotation);
    }

    private void startDownload3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(animationTime);
        x3DRotation.setInterpolator(interpolator);
        x3DRotation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                download3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                divider_4.setVisibility(View.VISIBLE);
                startRestart3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        download3D.startAnimation(x3DRotation);
    }

    private void startRestart3DRotation()
    {
        X3DRotation x3DRotation = new X3DRotation(270, 360, 0.0f, 0.0f, 0.0f, false);
        x3DRotation.setDuration(animationTime);
        x3DRotation.setInterpolator(interpolator);
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

    private void download()
    {
        Intent intent = new Intent(context, DownloadService.class);
        String url = "http://res.wx.qq.com/voice/getvoice?mediaid=MzA4MTAxMzcxNl8yNjQ5NTkzNjI1";
        // String url =
        // "http://sqdd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.5.0.660_android_r108360_GuanWang_537047121_release_10000484.apk";
        // String url = "http://120.24.93.248/app/HexagonBlock.apk";
        intent.putExtra(DownloadService.URL, url);
        context.startService(intent);
        context.bindService(intent, serviceConn, 0);
    }

    private ServiceConnection serviceConn = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            dlManagerRef = ((DownloadService.DownloadBinder) service).getDownloadManager();
            isbind = true;
            waveView.startWave();
            setProcessListener();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            isbind = false;
        }
    };

    private void setProcessListener()
    {
        if (dlManagerRef != null && dlManagerRef.get() != null)
        {
            dlManagerRef.get().addOnDownloadProgressListener(dlListener);
        }
    }

    private OnDownloadProgressListener dlListener = new OnDownloadProgressListener()
    {
        @Override
        public void onNewInfo(String s, long l, long l1)
        {
            waveView.setWaveBaseRatio((float) l / (float) l1, 100);
        }

        @Override
        public void onFinished(String s)
        {
            waveView.stopWave();
        }

        @Override
        public void onError(String s, int i, String s1)
        {
            waveView.stopWave();
        }
    };

    @Override
    public void dismiss()
    {
        super.dismiss();
        if (onDismissListner != null)
        {
            onDismissListner.onDismiss();
        }
        if (isbind)
        {
            context.unbindService(serviceConn);
            isbind = false;
        }
        if (dlManagerRef != null && dlManagerRef.get() != null)
        {
            dlManagerRef.get().removeOnDownloadProgressListener(dlListener);
        }
    }

    private onDismissListner onDismissListner;

    public void setOnDismissListner(onDismissListner l)
    {
        onDismissListner = l;
    }


    public interface onDismissListner
    {
        void onDismiss();
    }
}
