package com.example.yuanmengzeng.hexagonblock;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 退出dialog Created by yuanmengzeng on 2016/6/12.
 */
public class ExitDialog extends Dialog
{

    private Activity mActivity;

    private DismissListner dismissListner;

    private Button leftBtn, rightBtn;

    private ImageView imageView;

    private TextView useDiamondHint;

    private int type = CommonData.TYPE_EXIT;

    public ExitDialog(Activity context, DismissListner dismissListner)
    {
        super(context, R.style.dim_back_dialog);
        mActivity = context;
        this.dismissListner = dismissListner;
        initView();
        fillData();
    }


    private void initView()
    {
        View container = LayoutInflater.from(mActivity).inflate(R.layout.exit_dialog, null);
        setContentView(container);
        ViewGroup.LayoutParams lp = container.getLayoutParams();
        lp.width = getContext().getResources().getDisplayMetrics().widthPixels * 3 / 4;
        lp.height = getContext().getResources().getDisplayMetrics().heightPixels / 2;
        container.setLayoutParams(lp);
        // Window w = getWindow();
        // w.getAttributes().width =
        // getContext().getResources().getDisplayMetrics().widthPixels * 3 /
        // 4;
        // w.getAttributes().height =
        // getContext().getResources().getDisplayMetrics().heightPixels / 2;
        // w.setGravity(Gravity.CENTER);
        // w.setWindowAnimations(R.style.dialog_anim_scale_in_out);
        leftBtn = (Button) container.findViewById(R.id.exit);
        rightBtn = (Button) container.findViewById(R.id.continue_game);
        imageView = (ImageView) container.findViewById(R.id.exit_image);
        useDiamondHint = (TextView) container.findViewById(R.id.use_diamond_hint);

    }

    private void fillData()
    {
        switch (type)
        {
            case CommonData.TYPE_EXIT:
                imageView.setImageResource(R.drawable.exit_image);
                useDiamondHint.setVisibility(View.GONE);
                leftBtn.setText(getContext().getString(R.string.exit));
                rightBtn.setText(getContext().getString(R.string.continue_game));
                leftBtn.setOnClickListener(exitOnClickListner);
                rightBtn.setOnClickListener(exitOnClickListner);
                break;
            case CommonData.TYPE_DIAMOND:
                imageView.setImageResource(R.drawable.zhu_tianze);
                useDiamondHint.setVisibility(View.VISIBLE);
                leftBtn.setText(getContext().getString(R.string.cancel));
                rightBtn.setText(getContext().getString(R.string.use_diamon));
                leftBtn.setOnClickListener(diamondOnClickListner);
                rightBtn.setOnClickListener(diamondOnClickListner);
        }
    }

    public void setType(int type)
    {
        this.type = type;
        fillData();
    }

    private View.OnClickListener exitOnClickListner = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.exit:
                    dismiss(true);
                    mActivity.finish();
                    break;
                case R.id.continue_game:
                    dismiss(false);
            }
        }
    };

    private View.OnClickListener diamondOnClickListner = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.exit:
                    dismiss();
                    break;
                case R.id.continue_game:
                    dismiss();
                    if (mActivity instanceof MainActivity)
                    {
                        ((MainActivity) mActivity).showDiamondDialog();
                    }
            }
        }
    };

    @Override
    public void show()
    {
        setCanceledOnTouchOutside(true);
        super.show();
    }

    // @Override
    // public void dismiss()
    // {
    // super.dismiss();
    // if (dismissListner != null)
    // {
    // dismissListner.onDismiss(false);
    // }
    // }

    private void dismiss(boolean exit)
    {
        super.dismiss();
        if (dismissListner != null)
        {
            dismissListner.onDismiss(exit);
        }
    }

    public interface DismissListner
    {
        void onDismiss(boolean exit);
    }
}
