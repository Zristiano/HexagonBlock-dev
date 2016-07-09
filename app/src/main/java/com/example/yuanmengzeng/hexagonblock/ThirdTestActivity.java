package com.example.yuanmengzeng.hexagonblock;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;







import com.example.yuanmengzeng.hexagonblock.Animation.X3DRotation;

/**
 *
 * Created by yuanmengzeng on 2016/6/29.
 */
public class ThirdTestActivity extends Activity implements View.OnClickListener{

    private View buzzer3D ;
    private View share3D ;
    private View restart3D ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bind_service);
        initView();
    }


    private void initView(){
        buzzer3D = findViewById(R.id.buzzer_3d);
        buzzer3D.setOnClickListener(this);

        share3D = findViewById(R.id.share_3d);
        share3D.setOnClickListener(this);

        restart3D = findViewById(R.id.reboot_game_3d);
        restart3D.setOnClickListener(this);

        findViewById(R.id.rotate_3D_start_btn).setOnClickListener(this);
    }

    private void startBuzzer3DRotation(){
        X3DRotation x3DRotation = new X3DRotation(270,360,0.0f,0.0f,0.0f,false);
        x3DRotation.setDuration(300);
        x3DRotation.setInterpolator(new AccelerateInterpolator());
        x3DRotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                buzzer3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startRestart3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        buzzer3D.startAnimation(x3DRotation);
    }

    private void startShare3DRotation(){
        X3DRotation x3DRotation = new X3DRotation(270,360,0.0f,0.0f,0.0f,false);
        x3DRotation.setDuration(300);
        x3DRotation.setInterpolator(new AccelerateInterpolator());
        x3DRotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                share3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startBuzzer3DRotation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        share3D.startAnimation(x3DRotation);
    }

    private void startRestart3DRotation(){
        X3DRotation x3DRotation = new X3DRotation(270,360,0.0f,0.0f,0.0f,false);
        x3DRotation.setDuration(300);
        x3DRotation.setInterpolator(new AccelerateInterpolator());
        x3DRotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                restart3D.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        restart3D.startAnimation(x3DRotation);
    }


    int location[] = new int[2];
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus){
            buzzer3D.getLocationInWindow(location);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rotate_3D_start_btn:
                buzzer3D.setVisibility(View.GONE);
                share3D.setVisibility(View.GONE);
                restart3D.setVisibility(View.GONE);
                startShare3DRotation();
            break;
            default:break;
        }
    }
}
