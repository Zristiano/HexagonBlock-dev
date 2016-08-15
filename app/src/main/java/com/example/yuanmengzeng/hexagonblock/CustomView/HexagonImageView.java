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
 * 六边形imageView Created by yuanmengzeng on 2016/6/9.
 */
public class HexagonImageView extends ShapeImageView
{
    private float sideLength; // 六边形的边长

    private Path linePath = new Path();

    public HexagonImageView(Context context)
    {
        super(context);
    }

    public HexagonImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public HexagonImageView(Context context, AttributeSet attrs, int defStyleAttr)
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
        return (float) (getImageViewWidth() * Math.sqrt(3) / 2);
    }

    @Override
    protected void drawShapeImage(Canvas canvas, Paint bitmapPaint)
    {
        int width = (int) getImageViewWidth();
        int height = (int) getImageViewHeight();
        sideLength = width / 2;
        linePath.reset();
        linePath.moveTo(sideLength / 2, 0);
        linePath.lineTo(sideLength * 3 / 2, 0);
        linePath.lineTo(width, height / 2);
        linePath.lineTo(sideLength * 3 / 2, height);
        linePath.lineTo(sideLength / 2, height);
        linePath.lineTo(0, height / 2);
        linePath.lineTo(sideLength / 2, 0);
        linePath.close();
        canvas.drawPath(linePath, bitmapPaint);
    }
}
