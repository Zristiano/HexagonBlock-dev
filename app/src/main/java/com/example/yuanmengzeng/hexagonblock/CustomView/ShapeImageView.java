package com.example.yuanmengzeng.hexagonblock.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 各种形状imageview的基类 Created by yuanmengzeng on 2016/8/12.
 */
public abstract class ShapeImageView extends ImageView
{
    private Bitmap mBitmap;

    private Paint bitmapPaint;

    private float width, height; // 当前View的宽高

    private BitmapShader bitmapShader;

    public ShapeImageView(Context context)
    {
        super(context);
        init();
    }

    public ShapeImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setScaleType(ScaleType scaleType)
    {
        super.setScaleType(ScaleType.CENTER_CROP); // 强制按比例缩放至对应尺寸
    }

    private void init()
    {
        Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable))
        {
            return;
        }
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        handleBitmap(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        width = figureOutWidth(widthMeasureSpec);
        height = figureOutHeight(heightMeasureSpec);
        scaleBitmapMatrix();
        setMeasuredDimension((int) width, (int) height);
    }

    /**
     * 计算出视图的宽度
     * 
     * @param widthMeasureSpec wSpec
     * @return view的宽度
     */
    protected abstract float figureOutWidth(int widthMeasureSpec);

    /**
     * 计算出视图的高度
     *
     * @param heightMeasureSpec hSpec
     * @return view的高度
     */
    protected abstract float figureOutHeight(int heightMeasureSpec);

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
        handleBitmap(drawable);
        invalidate();
    }

    @Override
    public void setImageResource(int resId)
    {
        super.setImageResource(resId);
        Drawable drawable = getResources().getDrawable(resId);
        handleBitmap(drawable);
        invalidate();
    }

    private void handleBitmap(Drawable drawable)
    {
        if (drawable == null || !(drawable instanceof BitmapDrawable))
        {
            return;
        }
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        scaleBitmapMatrix();
    }

    private void scaleBitmapMatrix()
    {
        if (mBitmap == null || bitmapPaint == null)
            return;
        float bitmapWidth = mBitmap.getWidth();
        float bitmapHeight = mBitmap.getHeight();
        float dx = (width - bitmapWidth) / 2;
        float dy = (height - bitmapHeight) / 2;
        Matrix shaderMatrix = new Matrix();
        // bitmapShader.getLocalMatrix(shaderMatrix);
        // //不能通过改变bitmapShader里原有的Matrix来设置矩阵，因为onMeasure会被多次调用，导致bitmapShader里的Matrix被多次修改
        shaderMatrix.set(null);
        float scale;
        if (width / bitmapWidth > height / bitmapHeight) // 按最大比例缩放
        {
            scale = width / bitmapWidth;
        }
        else
        {
            scale = height / bitmapHeight;
        }
        shaderMatrix.postScale(scale, scale, bitmapWidth / 2, bitmapHeight / 2); // 以原中点为中心缩放scale倍
        shaderMatrix.postTranslate(dx, dy); // 移动图片中点至视图中点
        bitmapShader.setLocalMatrix(shaderMatrix);
        bitmapPaint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawShapeImage(canvas, bitmapPaint);
    }

    /**
     * 绘制指定形状的图形
     * 
     * @param canvas 画布
     * @param bitmapPaint 画笔
     */
    protected abstract void drawShapeImage(Canvas canvas, Paint bitmapPaint);

    protected float getImageViewWidth()
    {
        return width;
    }

    protected float getImageViewHeight()
    {
        return height;
    }

    protected Bitmap getmBitmap()
    {
        return mBitmap;
    }
}
