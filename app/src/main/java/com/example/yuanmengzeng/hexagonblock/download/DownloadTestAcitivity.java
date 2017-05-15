package com.example.yuanmengzeng.hexagonblock.download;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanmengzeng.hexagonblock.R;

import java.util.ArrayList;
import java.util.List;
import yuanmengzeng.donwload.DownloadItem;

import static com.tencent.open.utils.Global.getContext;

/**
 * Created by yuanmengzeng on 2017/4/11.
 */

public class DownloadTestAcitivity extends Activity implements View.OnClickListener
{
    private TouchRecyclerView recyclerView;

    private DownloadRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_dialog);

        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        View content = findViewById(R.id.download_layout);
//        ViewGroup.LayoutParams lp = content.getLayoutParams();
//        lp.width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
//        lp.height = getResources().getDisplayMetrics().heightPixels * 3 / 4;
//        content.setLayoutParams(lp);
    }

    private void initView()
    {
        findViewById(R.id.iv_close).setOnClickListener(this);
        recyclerView = (TouchRecyclerView) findViewById(R.id.download_list);
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
        // adapter.addDatas(createItems());
        recyclerView.setAdapter(adapter);
    }

    private List<DownloadItem> createItems()
    {
        List<DownloadItem> items = new ArrayList<>();
        for (int i = 0; i < 6; i++)
        {
            DownloadItem item = new DownloadItem();
            item.url = "item_" + i;
            item.isCompleted = true;
            items.add(item);
        }
        for (int i = 6; i < 12; i++)
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
        }
    }
}
