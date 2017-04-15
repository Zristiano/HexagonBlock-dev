package com.example.yuanmengzeng.hexagonblock.download;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import java.util.ArrayList;
import java.util.List;
import yuanmengzeng.donwload.DownloadItem;
import yuanmengzeng.donwload.DownloadUtil;

/**
 * <P>
 * 下载管理弹窗
 * </P>
 * Created by yuanmengzeng on 2017/4/1.
 */

public class DownloadDialog extends DialogFragment implements View.OnClickListener
{
    private View mRoot;

    private TouchRecyclerView recyclerView;

    private DownloadRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.download_dialog);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        if (mRoot != null)
        {
            return mRoot;
        }
        mRoot = inflater.inflate(R.layout.download_dialog, container, false);
        View content = mRoot.findViewById(R.id.download_layout);
        ViewGroup.LayoutParams lp = content.getLayoutParams();
        lp.width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
        lp.height = getResources().getDisplayMetrics().heightPixels * 3 / 4;
        content.setLayoutParams(lp);
        initView();
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume()
    {
        ZYMLog.info("onResume()");
        super.onResume();
    }

    private void initView()
    {
        mRoot.findViewById(R.id.iv_close).setOnClickListener(this);
        recyclerView = (TouchRecyclerView) mRoot.findViewById(R.id.download_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);
        adapter = new DownloadRecyclerAdapter(animator, new DownloadItemView.OnDeleteBtnShowListener()
        {
            @Override
            public void onShowHide(DownloadItemView view, boolean isShow)
            {
                recyclerView.setDeleteBtnShowView(view, isShow);
            }
        });
        // List<DownloadItem> downloadItems =
        // DownloadUtil.getCompleteDownload(getContext());
        adapter.addDatas(createItems());
        recyclerView.setAdapter(adapter);
    }

    private List<DownloadItem> createItems()
    {
        List<DownloadItem> items = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            DownloadItem item = new DownloadItem();
            item.url = "item_" + i;
            item.isCompleted = true;
            items.add(item);
        }
        for (int i = 4; i < 8; i++)
        {
            DownloadItem item = new DownloadItem();
            item.url = "item_" + i;
            item.isCompleted = false;
            items.add(item);
        }
        return items;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_close:
                dismiss();
        }
    }
}
