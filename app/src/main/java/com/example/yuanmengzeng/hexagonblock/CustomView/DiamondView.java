package com.example.yuanmengzeng.hexagonblock.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuanmengzeng.hexagonblock.R;

/**
 * 钻石显示view Created by yuanmengzeng on 2016/8/1.
 */
public class DiamondView extends LinearLayout
{

    int diamondCount;

    ImageView defaultImg;

    ImageView diamondImg[] = new ImageView[3];

    TextView textView;

    public DiamondView(Context context)
    {
        super(context);
        init();
    }

    public DiamondView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    public DiamondView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init();
    }

    private void getAttrs(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiamondView);
        diamondCount = typedArray.getInt(R.styleable.DiamondView_diamondCount, 0);
        typedArray.recycle();
    }

    private void init()
    {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        defaultImg = new ImageView(getContext());
        defaultImg.setImageResource(R.drawable.diamond_off_48);
        int dimen = (int) getContext().getResources().getDimension(R.dimen.diamond_size);
        LayoutParams lp = new LayoutParams(dimen, dimen);
        addView(defaultImg, lp);

        for (int i = 0; i < diamondImg.length; i++)
        {
            diamondImg[i] = new ImageView(getContext());
            diamondImg[i].setImageResource(R.drawable.diamond_48);
            addView(diamondImg[i], lp);
        }

        textView = new TextView(getContext());
        textView.setText("…");
        textView.setTextColor(getResources().getColor(R.color.white));
        lp.height = -2;
        lp.width = dimen;
        textView.setGravity(Gravity.CENTER);
        textView.getPaint().setFakeBoldText(true); // 字体加粗
        addView(textView, lp);
        showDiamond();
    }

    public void addDiamond(int num)
    {
        diamondCount += num;
        showDiamond();
    }

    private void showDiamond()
    {
        diamondCount = diamondCount <= 0 ? 0 : diamondCount;
        if (diamondCount <= 0)
        {
            defaultImg.setVisibility(VISIBLE);
        }
        else
        {
            defaultImg.setVisibility(GONE);
        }

        for (int i = 0; i < diamondImg.length; i++)
        {
            if (i < diamondCount)
            {
                diamondImg[i].setVisibility(VISIBLE);
            }
            else
            {
                diamondImg[i].setVisibility(GONE);
            }
        }
        if (diamondCount > 3)
        {
            textView.setVisibility(VISIBLE);
        }
        else
        {
            textView.setVisibility(GONE);
        }
    }

    public int getDiamondCount()
    {
        return diamondCount;
    }

    public void setDiamondCount(int diamondCount)
    {
        this.diamondCount = diamondCount;
        showDiamond();
    }
}
