package com.example.yuanmengzeng.hexagonblock.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 圆形imageView Created by yuanmengzeng on 2016/8/12.
 */
public class OvalImageView extends ShapeImageView
{

    public OvalImageView(Context context)
    {
        super(context);
    }

    public OvalImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public OvalImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected float figureOutWidth(int widthMeasureSpec)
    {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
        {
            return getmBitmap().getWidth();
        }
        else
        {
            return MeasureSpec.getSize(widthMeasureSpec);
        }
    }

    @Override
    protected float figureOutHeight(int heightMeasureSpec)
    {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)
        {
            return getmBitmap().getWidth();
        }
        else
        {
            return MeasureSpec.getSize(heightMeasureSpec);
        }
    }

    @Override
    protected void drawShapeImage(Canvas canvas, Paint bitmapPaint)
    {
        canvas.drawOval(new RectF(0, 0, getImageViewWidth(), getImageViewHeight()), bitmapPaint);
    }
}
