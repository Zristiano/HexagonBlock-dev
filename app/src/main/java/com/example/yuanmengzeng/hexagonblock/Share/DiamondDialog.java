package com.example.yuanmengzeng.hexagonblock.Share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yuanmengzeng.hexagonblock.CommonData;
import com.example.yuanmengzeng.hexagonblock.CustomView.BaseBlock;
import com.example.yuanmengzeng.hexagonblock.CustomView.HorizontalLineBlock;
import com.example.yuanmengzeng.hexagonblock.R;

/**
 * 使用钻石弹窗 Created by yuanmengzeng on 2016/8/2.
 */
public class DiamondDialog extends Dialog implements View.OnClickListener
{

    private int diamondCount; // 钻石数

    private int blockTypes[];

    private int whickBlock = CommonData.BLOCK_LEFT; // 默认变形的是左棋子

    private int newForm; // 新形状

    private OnDiamondCsmListner onDiamondCsmListner; // 钻石消耗监听器

    private View leftBlockContainer, centerBlockContainer, rightBlockContainer;

    private HorizontalLineBlock transformBlock;

    /**
     * 构造函数
     * 
     * @param context c
     * @param diamondCount 钻石数量
     * @param leftBlockType 左棋子形状
     * @param centerBlockType 中棋子形状
     * @param rightBlockType 右棋子形状
     */
    public DiamondDialog(Context context, int diamondCount, int leftBlockType, int centerBlockType, int rightBlockType)
    {
        super(context, R.style.diamond_dialog);
        this.diamondCount = diamondCount;
        blockTypes = new int[3];
        blockTypes[CommonData.BLOCK_LEFT] = leftBlockType;
        blockTypes[CommonData.BLOCK_CENTER] = centerBlockType;
        blockTypes[CommonData.BLOCK_RIGHT] = rightBlockType;
    }

    public void setOnDiamondCsmListner(OnDiamondCsmListner l)
    {
        onDiamondCsmListner = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.diamond_dialog, null);
        setContentView(view);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = getContext().getResources().getDisplayMetrics().widthPixels * 4 / 5;
        view.setLayoutParams(lp);

        initView();
        fillData();
    }

    private void initView()
    {
        transformBlock = (HorizontalLineBlock) findViewById(R.id.transform_block);
        leftBlockContainer = findViewById(R.id.left_block_container);
        centerBlockContainer = findViewById(R.id.center_block_container);
        rightBlockContainer = findViewById(R.id.right_block_container);
    }

    private void fillData()
    {
        ((TextView) findViewById(R.id.diamond_sum)).setText("x" + diamondCount);
        ((TextView) findViewById(R.id.diamond_desc)).setText(
                getContext().getString(R.string.diamond_desc, CommonData.STATE_SCORE_LEVEl, diamondCount));

        HorizontalLineBlock block;
        block = ((HorizontalLineBlock) findViewById(R.id.left_block));
        block.setBlockType(blockTypes[CommonData.BLOCK_LEFT]);
        block.expandSize(0, 0);
        block = ((HorizontalLineBlock) findViewById(R.id.center_block));
        block.setBlockType(blockTypes[CommonData.BLOCK_CENTER]);
        block.expandSize(0, 0);
        block = ((HorizontalLineBlock) findViewById(R.id.right_block));
        block.setBlockType(blockTypes[CommonData.BLOCK_RIGHT]);
        block.expandSize(0, 0);

        leftBlockContainer.setOnClickListener(this);
        centerBlockContainer.setOnClickListener(this);
        rightBlockContainer.setOnClickListener(this);

        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.select_trangle_down).setOnClickListener(this);
        findViewById(R.id.select_trangle_top).setOnClickListener(this);

        TextView confirmView = (TextView) findViewById(R.id.confirm);
        if (diamondCount > 0)
        {
            confirmView.setTextColor(getContext().getResources().getColorStateList(R.color.selector_btn_blue));
            confirmView.setOnClickListener(this);
        }
        else
        {
            confirmView.setTextColor(getContext().getResources().getColor(R.color.confirm_disable));
            confirmView.setOnClickListener(null);
        }
        setBlockSelected(leftBlockContainer, CommonData.BLOCK_LEFT); // 默认选中左棋子
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.left_block_container:
                if (!v.isSelected())
                {
                    setBlockSelected(v, CommonData.BLOCK_LEFT);
                }
                break;
            case R.id.center_block_container:
                if (!v.isSelected())
                {
                    setBlockSelected(v, CommonData.BLOCK_CENTER);
                }
                break;
            case R.id.right_block_container:
                if (!v.isSelected())
                {
                    setBlockSelected(v, CommonData.BLOCK_RIGHT);
                }
                break;
            case R.id.select_trangle_top:
                setBlockTransform(true);
                break;
            case R.id.select_trangle_down:
                setBlockTransform(false);
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                dismiss();
                if (onDiamondCsmListner != null)
                {
                    onDiamondCsmListner.onBlockTransform(whickBlock, newForm);
                }
                break;
        }
    }

    /**
     * 将棋子变换到需要的形状
     * 
     * @param isUp 类型是否是上一个
     */
    private void setBlockTransform(boolean isUp)
    {
        int type = isUp ? -1 : 1;
        newForm = (2 * BaseBlock.TYPE_COUNT + newForm + type) % BaseBlock.TYPE_COUNT; // (2*25+0-1)%25=24
        transformBlock.setBlockType(newForm);
        transformBlock.expandSize(0, 0);
        transformBlock.invalidate();
    }

    /**
     * 选择哪个棋子（block）用于变形
     * 
     * @param which 哪个棋子(block)
     */
    private void setBlockSelected(View v, int which)
    {
        whickBlock = which;
        leftBlockContainer.setSelected(false);
        centerBlockContainer.setSelected(false);
        rightBlockContainer.setSelected(false);
        v.setSelected(true);
        newForm = blockTypes[which];
        transformBlock.setBlockType(newForm);
        transformBlock.expandSize(0, 0); // 恢复正常大小
        transformBlock.invalidate();
    }

    public void refreshData(int diamondCount, int leftBlockType, int centerBlockType, int rightBlockType)
    {
        this.diamondCount = diamondCount;
        blockTypes[CommonData.BLOCK_LEFT] = leftBlockType;
        blockTypes[CommonData.BLOCK_CENTER] = centerBlockType;
        blockTypes[CommonData.BLOCK_RIGHT] = rightBlockType;
        fillData();
    }

    public interface OnDiamondCsmListner
    {
        void onBlockTransform(int whickBlock, int newForm);
    }
}
