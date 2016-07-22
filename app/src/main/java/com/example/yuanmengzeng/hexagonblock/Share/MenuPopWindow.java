package com.example.yuanmengzeng.hexagonblock.Share;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.PopupWindow;

import com.example.yuanmengzeng.hexagonblock.Animation.X3DRotation;
import com.example.yuanmengzeng.hexagonblock.R;

/**
 * 菜单 Created by yuanmengzeng on 2016/7/9.
 */
public class MenuPopWindow extends PopupWindow
{
    Context context;

    private View.OnClickListener onClickListener;

    private View buzzer3D;

    private View share3D;

    private View restart3D;

    private View divider_1, divider_2;

    private Interpolator interpolator;

    private int animationTime = 200;

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

        buzzer3D = contentView.findViewById(R.id.buzzer_3d);
        buzzer3D.setOnClickListener(onClickListener);
        buzzer3D.setTag(true);

        share3D = contentView.findViewById(R.id.share_3d);
        share3D.setOnClickListener(onClickListener);

        restart3D = contentView.findViewById(R.id.reboot_game_3d);
        restart3D.setOnClickListener(onClickListener);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff)
    {
        divider_1.setVisibility(View.INVISIBLE);
        divider_2.setVisibility(View.INVISIBLE);
        buzzer3D.setVisibility(View.GONE);
        share3D.setVisibility(View.GONE);
        restart3D.setVisibility(View.GONE);
        // super.showAsDropDown(anchor, xoff, yoff, gravity);
        super.showAsDropDown(anchor, xoff, yoff);
        startShare3DRotation();
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
                startRestart3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        buzzer3D.startAnimation(x3DRotation);
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

    @Override
    public void dismiss()
    {
        super.dismiss();
        if (onDismissListner != null)
        {
            onDismissListner.onDismiss();
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
