package com.example.yuanmengzeng.hexagonblock.CustomView;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import java.util.Random;

/**
 * <P>
 *     waveview
 * </P>
 * Created by YuanmengZeng on 2016/3/28.
 */
public class CustomWaveView extends View
{

    /**
     * +------------------------+
     * | wave length - 波长      |__________
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude - 振幅
     * | /    \        | /    \ |  |
     * |/      \       |/      \|  |
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           |__|_______
     * |                        |  |
     * |                        |  |
     * |                        |  |defautBaseHeight - 波浪的基座高度
     * |                        |  |
     * |                        |  |
     * |                        |  |
     * |                        |  |
     * |                        |  |
     * |                        |  |
     * +------------------------+__|_______
     */


    private final static float WAVE_LENGTH_RATIO = 1.0f;

    private final static float AMPLITUDE_RATIO = 0.05f;

    private int width, height;

    private Paint wavePaint;

    private BitmapShader bitmapShader; // 用作设置波浪paint的shader

    private Context context;

    private float mTranslateRatio; // 波浪的横坐标位移比例

    private float horiTranslation; //横向位移距离

    private Matrix shaderMatrix; //

    private boolean enableWave = true;

    private float lastBaseHeight = 0;

    private float defautBaseHeight = 50; // 波浪的基座高度

    public CustomWaveView(Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    public CustomWaveView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomWaveView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init()
    {
        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setColor(context.getResources().getColor(R.color.wave_color));

        shaderMatrix = new Matrix();
        updateTranslateX();
//        valueUpdate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        ZYMLog.info("onMeasure width is " + width);
        ZYMLog.info("onMeasure height is " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        ZYMLog.info("onSizeChanged w is " + w);
        ZYMLog.info("onSizeChanged h is " + h);
        ZYMLog.info("onSizeChanged oldw is " + oldw);
        ZYMLog.info("onSizeChanged oldh is " + oldh);
        setWavePaintShader();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // ZYMLog.info("ondraw getHeight is "+getHeight());
        // ZYMLog.info("ondraw getMeasuredHeight is "+getMeasuredHeight());
        // ZYMLog.info("ondraw getWidth is "+getWidth());
        // ZYMLog.info("ondraw getMeasuredWidth is "+getMeasuredWidth());
        // ZYMLog.info("ondraw mTranslateRatio is "+mTranslateRatio);
        // shaderMatrix.setScale(1, 0.25f, 0, getHeight() - defautBaseHeight);
        // shaderMatrix.setScale(1, 1, 0, getHeight()-defautBaseHeight);
        shaderMatrix.setScale(1, 1, 0, 0);
//        shaderMatrix.postTranslate(horiTranslation, 0);
        shaderMatrix.postTranslate((getWidth() * mTranslateRatio), 0);
        shaderMatrix.postTranslate(0, getHeight()-defautBaseHeight);
        bitmapShader.setLocalMatrix(shaderMatrix);
        wavePaint.setShader(bitmapShader);
        // ZYMLog.info("defautBaseHeight is " + defautBaseHeight);
        canvas.drawRect(0, 0, getWidth(), height, wavePaint);
        if (enableWave)
        {
            invalidate();
        }
    }

    private void setWavePaintShader()
    {
        Paint waveShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        waveShaderPaint.setColor(context.getResources().getColor(R.color.wave_color));
        waveShaderPaint.setStrokeWidth(2);
        Bitmap shaderBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(shaderBitmap);
        for (int w = 0; w < width; w++)
        {
            float h = (float) Math.sin(WAVE_LENGTH_RATIO * w / width * 2 * Math.PI) * AMPLITUDE_RATIO * height;
            float lineTop = h+AMPLITUDE_RATIO*height+1;
            canvas.drawLine(w, height, w,lineTop, waveShaderPaint);
//            ZYMLog.info("w->" + w + "  WAVE_LENGTH_RATIO->" + WAVE_LENGTH_RATIO + "  width->" + width + "  h->" + h
//                    + "   defaultBaseHeight->" + defautBaseHeight +"    lineHeight->"+(lineTop));

        }
        if (bitmapShader == null)
        {
            bitmapShader = new BitmapShader(shaderBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        }
    }

    private void updateTranslateX()
    {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translateRatio", 0.0f,1.0f);
        objectAnimator.setDuration(650);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        ZYMLog.info("updateTranslateX");
        objectAnimator.start();
    }

    public void setTranslateRatio(float mTranslateRatio)
    {
        this.mTranslateRatio = mTranslateRatio;
        // invalidate();
    }
    public float getTranslateRatio()
    {
        return mTranslateRatio;
    }

    public void setDefautBaseHeight(float defautBaseHeight)
    {
        this.defautBaseHeight = defautBaseHeight;
    }
    public float getDefautBaseHeight()
    {
        return defautBaseHeight;
    }

    public void setWaveBaseRatio(float ratio,int periodMillis)
    {
        ZYMLog.info("ratio is " + ratio);
        if (ratio < 0.0f)
        {
            ratio = 0.0f;
        }
        else if (ratio > 1.0f)
        {
            ratio = 1.0f;
        }
        float nowBaseHeight = height*(ratio+2*AMPLITUDE_RATIO);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "defautBaseHeight", lastBaseHeight, nowBaseHeight);
        objectAnimator.setDuration(periodMillis);
        objectAnimator.setInterpolator(new LinearInterpolator());
        ZYMLog.info("updateTranslateX");
        objectAnimator.start();
        lastBaseHeight = nowBaseHeight;
//        defautBaseHeight = height * (ratio+2*AMPLITUDE_RATIO);

    }

    public void startWave(){
        enableWave = true;
        invalidate();
    }
    public void stopWave(){
        enableWave = false;
    }

    private BezierEvaluator evaluator ;
    private void valueUpdate()
    {
        if (evaluator == null)
        {
            evaluator = new BezierEvaluator();
        }
        ValueAnimator valueAnimator = ValueAnimator.ofObject(evaluator,0.0f,5.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (Float)animation.getAnimatedValue();
                horiTranslation =  (Float) animation.getAnimatedValue()*getWidth();
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    public class BezierEvaluator implements TypeEvaluator<Float>
    {
        private float control1=0.6f, control2=0.6f;
        private float lastT = 0.0f;
        Random random = new Random();

        private void setControPoint(float control1, float control2)
        {
            this.control1 = control1;
            this.control2 = control2;
        }

        @Override
        public Float evaluate(float t, Float startValue, Float endValue) {
            if (startValue == null) startValue = 0.0f;
            if (endValue == null) endValue = 1.0f;
            if (lastT>t)
            {
                control1 = random.nextFloat();
                control2 = 1- control1;
            }
            ZYMLog.info("control1 --> "+control1+"   control2 --> "+control2);
            lastT = t;
//            return startValue*(1-t)*(1-t)*(1-t)+ 3*control1*t*(1-t)*(1-t)+3*control2*t*t*(1-t)+endValue*t*t*t;
            return startValue*(1-t)*(1-t)+ 2*control1*t*(1-t)+endValue*t*t;
        }
    }

}
