package com.example.yuanmengzeng.hexagonblock.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.CommonUtils;
import com.example.yuanmengzeng.hexagonblock.HexagonRelation;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * Created by yuanmengzeng on 2016/5/18.
 */
public class HexagonHeap extends ViewGroup
{

    private int width; // 六边形堆的宽

    private int height; // 六边形堆的高

    private float childWidth; // 子六边形的宽

    private float childHeight; // 子六边形的高

    private int HEXAGON_LAYER = 5; // 六边形层数 小六边形个数= 3*n^2-3*n+1 n为六边形层数

    private int hexCount; // 小六边形个数

    private Context context;

    private int heapBgResId = -1;

    private Paint imagePaint;

    private Path imagePath;

    private Bitmap heapBgBitmap;

    private BitmapShader heapBgBitmapShader;

    public HexagonHeap(Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    public HexagonHeap(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    public HexagonHeap(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HexagonHeap);
        heapBgResId = typedArray.getResourceId(R.styleable.HexagonHeap_HexagonHeap_bg, -1);
        heapBgResId = CommonUtils.getHeapBg(); // 获取大六边形的底案背景
        typedArray.recycle();
    }

    private void init()
    {
        hexCount = 3 * HEXAGON_LAYER * (HEXAGON_LAYER - 1) + 1;
        for (int i = 0; i < hexCount; i++)
        {
            addView(new HexagonView(context));
        }
        if (heapBgResId > 0)
        {
            initImageConfig();
        }
    }

    public void setHeapBgResourse(int resId)
    {
        heapBgResId = resId;
        if (heapBgResId > 0)
        {
            initImageConfig();
        }
        invalidate();
    }

    /**
     * 初始化底案大六边形的背景配置
     */
    private void initImageConfig()
    {
        Drawable drawable = context.getResources().getDrawable(heapBgResId);
        if (drawable != null && drawable instanceof BitmapDrawable)
        {
            heapBgBitmap = ((BitmapDrawable) drawable).getBitmap();
            heapBgBitmapShader = new BitmapShader(heapBgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            imagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            imagePath = new Path();
        }
    }

    public int getChildHeight()
    {
        return (int) childHeight;
    }

    public int getChildWidth()
    {
        return (int) childWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        width = MeasureSpec.getSize(widthMeasureSpec);

        childWidth = (width - getPaddingLeft() - getPaddingRight()) / (HEXAGON_LAYER * 2 - 1); // 最宽的一行有n*2-1个六边形
        childHeight = (float) (childWidth * 2 / Math.sqrt(3));
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((int) childWidth,
                MeasureSpec.getMode(widthMeasureSpec));
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec((int) childHeight,
                MeasureSpec.getMode(widthMeasureSpec));
        for (int i = 0; i < getChildCount(); i++)
        {
            View childView = getChildAt(i);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        height = (int) (childHeight * HEXAGON_LAYER + (childHeight / 2) * (HEXAGON_LAYER - 1)) + getPaddingTop()
                + getPaddingBottom(); // 整体大六边形的高 （算法看实际图效果）
        scaleBitmapMatrix();
        setMeasuredDimension(width, height);
        ZYMLog.info("HexagonHeap width is " + width + "  height is " + height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    { // left,top,right,bottom为viewgroup在屏幕中的位置
        int startLeft; // 每一行第一个小六边形的起始左边距（距parent的距离）
        int startHeight = getPaddingTop(); // 每一行第一个小六边形的起始高（距parent的距离）
        int hexagonOrder = 0;
        for (int i = 0; i < 2 * HEXAGON_LAYER - 1; i++)
        {
            int count = (2 * HEXAGON_LAYER - 1) - Math.abs(i - (HEXAGON_LAYER - 1)); // 每一层小六边形的个数
                                                                                     // 例：若大六边形有5层，则每一行(从第0行开始算起)的六边形个数
                                                                                     // 为：
                                                                                     // 9-|i-4|
            startLeft = (int) (((2 * HEXAGON_LAYER - 1) - count) * childWidth / 2) + getPaddingLeft(); // 每一行第一个小六边形的起始左边距（距parent的距离）
            for (int k = 0; k < count; k++)
            {
                View childView = getChildAt(hexagonOrder);
                setChildPosition(childView, startLeft, startHeight, childView.getMeasuredWidth(),
                        childView.getMeasuredHeight());
                startLeft += childView.getWidth();
                setAdjacentRelation((HexagonView) childView, i, k, count, hexagonOrder); // 设置小六边形的邻接关系
                hexagonOrder++;
            }
            startHeight += (int) (childHeight * 0.75);
        }
    }

    /**
     * 设置小六边形的邻接关系
     * 
     * @param hexagonView 小六边形
     * @param layer 小六边形所处的层
     * @param layOrder 小六边形在该层的序数
     * @param layCount 该层小六边形个数
     * @param hexagonOrder 该小六边形在整个大六边形中的序数
     */
    private void setAdjacentRelation(HexagonView hexagonView, int layer, int layOrder, int layCount, int hexagonOrder)
    {
        if (layer < HEXAGON_LAYER - 1) // 上半部分的小六边形
        {
            int lastLayCount = layCount - 1;
            if (layOrder == 0) // 每行第一个
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.RT, getPosHexagonView(hexagonOrder - lastLayCount)); // 右上
                hexagonView.setAdjacentHexagon(HexagonRelation.R, getPosHexagonView(hexagonOrder + 1)); // 右
            }
            else if (layOrder == layCount - 1) // 每行最后一个
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.L, getPosHexagonView(hexagonOrder - 1)); // 左
                hexagonView.setAdjacentHexagon(HexagonRelation.LT, getPosHexagonView(hexagonOrder - layCount)); // 左上
            }
            else // 每行非首尾位置的小六边形
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.L, getPosHexagonView(hexagonOrder - 1)); // 左
                hexagonView.setAdjacentHexagon(HexagonRelation.LT, getPosHexagonView(hexagonOrder - layCount)); // 左上
                hexagonView.setAdjacentHexagon(HexagonRelation.RT, getPosHexagonView(hexagonOrder - lastLayCount)); // 右上
                hexagonView.setAdjacentHexagon(HexagonRelation.R, getPosHexagonView(hexagonOrder + 1)); // 右
            }
            hexagonView.setAdjacentHexagon(HexagonRelation.LB, getPosHexagonView(hexagonOrder + layCount)); // 左下
            hexagonView.setAdjacentHexagon(HexagonRelation.RB, getPosHexagonView(hexagonOrder + layCount + 1)); // 右下
        }
        else if (layer == HEXAGON_LAYER - 1) // 中轴层的小六边形
        {
            int lastLayCount = layCount - 1;
            if (layOrder == 0) // 每行第一个
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.RT, getPosHexagonView(hexagonOrder - lastLayCount)); // 右上
                hexagonView.setAdjacentHexagon(HexagonRelation.RB, getPosHexagonView(hexagonOrder + layCount)); // 右下
                hexagonView.setAdjacentHexagon(HexagonRelation.R, getPosHexagonView(hexagonOrder + 1)); // 右
            }
            else if (layOrder == layCount - 1) // 每行最后一个
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.L, getPosHexagonView(hexagonOrder - 1)); // 左
                hexagonView.setAdjacentHexagon(HexagonRelation.LT, getPosHexagonView(hexagonOrder - layCount)); // 左上
                hexagonView.setAdjacentHexagon(HexagonRelation.LB, getPosHexagonView(hexagonOrder + layCount - 1)); // 左下
            }
            else // 每行非首尾位置的小六边形
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.L, getPosHexagonView(hexagonOrder - 1)); // 左
                hexagonView.setAdjacentHexagon(HexagonRelation.LT, getPosHexagonView(hexagonOrder - layCount)); // 左上
                hexagonView.setAdjacentHexagon(HexagonRelation.LB, getPosHexagonView(hexagonOrder + layCount - 1)); // 左下
                hexagonView.setAdjacentHexagon(HexagonRelation.RT, getPosHexagonView(hexagonOrder - lastLayCount)); // 右上
                hexagonView.setAdjacentHexagon(HexagonRelation.RB, getPosHexagonView(hexagonOrder + layCount)); // 右下
                hexagonView.setAdjacentHexagon(HexagonRelation.R, getPosHexagonView(hexagonOrder + 1)); // 右
            }
        }
        else // 下半部分的小六边形
        {
            int lastLayCount = layCount + 1;
            if (layOrder == 0) // 每行第一个
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.R, getPosHexagonView(hexagonOrder + 1)); // 右
                hexagonView.setAdjacentHexagon(HexagonRelation.RB, getPosHexagonView(hexagonOrder + layCount)); // 右下
            }
            else if (layOrder == layCount - 1) // 每行最后一个
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.L, getPosHexagonView(hexagonOrder - 1)); // 左
                hexagonView.setAdjacentHexagon(HexagonRelation.LB, getPosHexagonView(hexagonOrder + layCount - 1)); // 左下
            }
            else // 每行非首尾位置的小六边形
            {
                hexagonView.setAdjacentHexagon(HexagonRelation.L, getPosHexagonView(hexagonOrder - 1)); // 左
                hexagonView.setAdjacentHexagon(HexagonRelation.LB, getPosHexagonView(hexagonOrder + layCount - 1)); // 左下
                hexagonView.setAdjacentHexagon(HexagonRelation.R, getPosHexagonView(hexagonOrder + 1)); // 右
                hexagonView.setAdjacentHexagon(HexagonRelation.RB, getPosHexagonView(hexagonOrder + layCount)); // 右下
            }
            hexagonView.setAdjacentHexagon(HexagonRelation.LT, getPosHexagonView(hexagonOrder - lastLayCount)); // 左上
            hexagonView.setAdjacentHexagon(HexagonRelation.RT, getPosHexagonView(hexagonOrder - lastLayCount + 1)); // 右上
        }
    }

    private HexagonView getPosHexagonView(int order)
    {
        if (order >= 0 && order < getChildCount())
        {
            return (HexagonView) getChildAt(order);
        }
        else
        {
            return null;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        if (imagePaint != null)
        {
            float childLength = childHeight / 2;
            float pathLeft = getPaddingLeft() + (childWidth / 2) * (HEXAGON_LAYER - 1); // 第一行的小六边形左上角的起始x坐标
            float pathTop = getPaddingTop() + childLength / 2; // 第一行的小六边形左上角的起始y坐标
            imagePath.reset();
            imagePath.moveTo(pathLeft, pathTop);
            for (int i = 0; i < HEXAGON_LAYER * 2; i++)
            { // 大六边形顶边
                float stepTop = i % 2 == 0 ? -childLength / 2 : childLength / 2;
                pathLeft += childWidth / 2;
                pathTop += stepTop;
                imagePath.lineTo(pathLeft, pathTop);
            }
            for (int i = 0; i < HEXAGON_LAYER * 2 - 1; i++)
            { // 大六边形右上边
                float stepTop = i % 2 == 0 ? childLength : childLength / 2;
                float stepLeft = i % 2 == 0 ? 0 : childWidth / 2;
                pathLeft += stepLeft;
                pathTop += stepTop;
                imagePath.lineTo(pathLeft, pathTop);
            }
            for (int i = 0; i < HEXAGON_LAYER * 2 - 1; i++)
            { // 大六边形右下边
                float stepTop = i % 2 == 0 ? childLength / 2 : childLength;
                float stepLeft = i % 2 == 0 ? -childWidth / 2 : 0;
                pathLeft += stepLeft;
                pathTop += stepTop;
                imagePath.lineTo(pathLeft, pathTop);
            }
            for (int i = 0; i < HEXAGON_LAYER * 2 - 1; i++)
            { // 大六边形底边
                float stepTop = i % 2 == 0 ? -childLength / 2 : childLength / 2;
                float stepLeft = -childWidth / 2;
                pathLeft += stepLeft;
                pathTop += stepTop;
                imagePath.lineTo(pathLeft, pathTop);
            }
            for (int i = 0; i < HEXAGON_LAYER * 2 - 1; i++)
            { // 大六边形左下边
                float stepTop = i % 2 == 0 ? -childLength : -childLength / 2;
                float stepLeft = i % 2 == 0 ? 0 : -childWidth / 2;
                pathLeft += stepLeft;
                pathTop += stepTop;
                imagePath.lineTo(pathLeft, pathTop);
            }
            for (int i = 0; i < HEXAGON_LAYER * 2 - 1; i++)
            { // 大六边形左上边
                float stepTop = i % 2 == 0 ? -childLength / 2 : -childLength;
                float stepLeft = i % 2 == 0 ? childWidth / 2 : 0;
                pathLeft += stepLeft;
                pathTop += stepTop;
                imagePath.lineTo(pathLeft, pathTop);
            }
            imagePath.close();
            imagePaint.setShader(heapBgBitmapShader);
            canvas.drawPath(imagePath, imagePaint);
        }
        super.dispatchDraw(canvas);
    }

    public View getHexagon(float centerX, float centerY)
    {
        float y = centerY - getPaddingTop();
        float x = centerX - getPaddingLeft();
        if (y <= 0)
            return null;
        float layerHeight = childHeight * 0.75f;
        float layer = y / layerHeight;
        int layerInteger = (int) layer;
        float layerFloat = layer - layerInteger;
        if (layerInteger >= 0 && layerFloat >= 0.25f)
        {
            int count = (2 * HEXAGON_LAYER - 1) - Math.abs(layerInteger - (HEXAGON_LAYER - 1)); // 第layerInteger层有多少个小六边形
            int startLeft = (int) (((2 * HEXAGON_LAYER - 1) - count) * childWidth / 2) + getPaddingLeft(); // 每一层的起始x位置
            float position = (centerX - startLeft) / childWidth;
            // ZYMLog.info("position is "+position+" startLeft is
            // "+startLeft+" centerX is "+centerX);
            if (position >= 0 && position < count)
            {
                int sum = 0;
                for (int i = 0; i < layerInteger; i++)
                { // 计算layerInteger层之前有多少个小六边形
                    sum += (2 * HEXAGON_LAYER - 1) - Math.abs(i - (HEXAGON_LAYER - 1));
                }
                return getChildAt((int) position + sum);
            }
        }
        return null;
    }

    /**
     * 设置大六边形背景图片的配置矩阵，使其居中显示
     */
    private void scaleBitmapMatrix()
    {
        if (heapBgBitmap == null)
            return;
        float heapWidth = width - getPaddingLeft() - getPaddingRight();
        float heapHeight = height - getPaddingTop() - getPaddingBottom();
        float bitmapWidth = heapBgBitmap.getWidth();
        float bitmapHeight = heapBgBitmap.getHeight();
        float dx = (heapWidth - bitmapWidth) / 2;
        float dy = (heapHeight - bitmapHeight) / 2;
        Matrix shaderMatrix = new Matrix();
        // bitmapShader.getLocalMatrix(shaderMatrix);
        // //不能通过改变bitmapShader里原有的Matrix来设置矩阵，因为onMeasure会被多次调用，导致bitmapShader里的Matrix被多次修改
        shaderMatrix.set(null);
        float scale;
        if (heapWidth / bitmapWidth < heapHeight / bitmapHeight)
        {
            scale = heapWidth / bitmapWidth;
        }
        else
        {
            scale = heapHeight / bitmapHeight;
        }
        shaderMatrix.postScale(scale, scale, bitmapWidth / 2, bitmapHeight / 2);
        shaderMatrix.postTranslate(dx, dy);
        heapBgBitmapShader.setLocalMatrix(shaderMatrix);
    }

    /**
     * 设置六边形层数
     * 
     * @param HEXAGON_LAYER 六边形层数
     */
    public void setHEXAGON_LAYER(int HEXAGON_LAYER)
    {
        this.HEXAGON_LAYER = HEXAGON_LAYER;
        removeAllViews();
        init();
        requestLayout();
    }

    public int getHEXAGON_LAYER()
    {
        return HEXAGON_LAYER;
    }

    /**
     * @param child 子view
     * @param left 相对于parent的左边距
     * @param top 相对于parent的上边距
     * @param width 子view的宽
     * @param height 子view的长
     */
    private void setChildPosition(View child, int left, int top, int width, int height)
    {
        child.layout(left, top, left + width, top + height);
    }

    /**
     * 将HexagonHeap状态初始化
     */
    public void reset()
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            ((HexagonView) getChildAt(i)).reset();
        }
    }
}
