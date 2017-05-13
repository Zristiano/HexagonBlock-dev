package com.example.yuanmengzeng.hexagonblock.download;

import android.os.Handler;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanmengzeng.hexagonblock.ZYMLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import yuanmengzeng.donwload.DownloadItem;

/**
 * <P>
 * 下载管理dialog的adapter
 * </P>
 * Created by yuanmengzeng on 2017/4/5.
 */

public class DownloadRecyclerAdapter extends RecyclerView.Adapter<DownloadRecyclerAdapter.DownloadViewHolder>
        implements OnDownloadItemListener
{

    private List<DownloadItem> downloadItems;

    private RecyclerView.ItemAnimator animator;

    private DownloadItemView.OnDeleteBtnShowListener onDeleteBtnShowListener;

    public DownloadRecyclerAdapter(RecyclerView.ItemAnimator animator,
            DownloadItemView.OnDeleteBtnShowListener listener)
    {
        downloadItems = new ArrayList<>();
        this.animator = animator;
        onDeleteBtnShowListener = listener;
    }

    public void addDatas(List<DownloadItem> items)
    {
        downloadItems.addAll(items);
    }

    public void clearData()
    {
        downloadItems.clear();
    }

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        DownloadItemView itemView = new DownloadItemView(parent.getContext(), this);
        itemView.setOnDeleteBtnShowListener(onDeleteBtnShowListener);
        return new DownloadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DownloadViewHolder holder, int position)
    {

        holder.fillData(downloadItems.get(position), position);

    }

    @Override
    public int getItemCount()
    {
        return downloadItems.size();
    }

    @Override
    public void onDelete(String urlString)
    {
        ZYMLog.info("position is " + urlString);
        int idx = findItemIdx(urlString);
        if (idx < 0)
        {
            return;
        }
        // DownloadItem item = downloadItems.get(idx);
        notifyItemRemoved(idx);
        downloadItems.remove(idx);
        // notifyItemInserted(idx - 2);
        // downloadItems.add(idx - 2, item);
        /*
         * long duration = animator.getRemoveDuration() +
         * animator.getMoveDuration(); new Handler().postDelayed(new
         * Runnable() {
         * @Override public void run() { notifyDataSetChanged(); } },
         * duration);
         */
    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }

    @Override
    public void onStartDownload(String urlString)
    {
        int idx = findItemIdx(urlString);
        DownloadItem downloadItem = new DownloadItem();
        downloadItem.url = downloadItems.get(downloadItems.size() - 1).url + new Random().nextInt(100);
        downloadItems.add(idx, downloadItem);
        notifyItemInserted(idx);
    }

    @Override
    public void onPauseDownload(String urlString)
    {
        int idx = findItemIdx(urlString);
        int to = downloadItems.size() - 1;
        notifyItemMoved(idx, to);
        DownloadItem item = downloadItems.get(idx);
        downloadItems.remove(idx);
        downloadItems.add(to, item);
    }

    private int findItemIdx(String url)
    {
        for (int i = 0; i < downloadItems.size(); i++)
        {
            DownloadItem item = downloadItems.get(i);
            if (item.url.equals(url))
            {
                return i;
            }
        }
        return -1;
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder
    {

        DownloadViewHolder(View itemView)
        {
            super(itemView);
        }

        void fillData(DownloadItem item, int position)
        {
            if (itemView instanceof DownloadItemView)
            {
                ((DownloadItemView) itemView).setData(item, position);
            }
        }
    }
}
