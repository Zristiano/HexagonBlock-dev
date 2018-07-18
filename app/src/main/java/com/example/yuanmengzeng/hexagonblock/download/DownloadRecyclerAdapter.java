package com.example.yuanmengzeng.hexagonblock.download;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.example.yuanmengzeng.hexagonblock.download.data.BaseDownloadData;
import com.example.yuanmengzeng.hexagonblock.download.data.ListDownloadedData;
import com.example.yuanmengzeng.hexagonblock.download.data.ListDownloadingData;
import com.example.yuanmengzeng.imageloader.ImageLoader;

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
    public static final int TYPE_DOWNLOADING = 1;

    public static final int TYPE_TO_BE_DOWNLOAD = 2;

    private List<BaseDownloadData> mDatas;

    private RecyclerView.ItemAnimator animator;

    private DownloadItemView.OnDeleteBtnShowListener onDeleteBtnShowListener;

    public DownloadRecyclerAdapter(RecyclerView.ItemAnimator animator,
            DownloadItemView.OnDeleteBtnShowListener listener)
    {
        this.animator = animator;
        onDeleteBtnShowListener = listener;
        mDatas = new ArrayList<>();
    }

    public void setDatas(List<BaseDownloadData> items)
    {
        mDatas.clear();
        mDatas.addAll(items);
    }

    public void clearData()
    {
        mDatas.clear();
    }

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_DOWNLOADING)
        {
            DownloadItemView itemView = new DownloadItemView(parent.getContext(), this);
            itemView.setOnDeleteBtnShowListener(onDeleteBtnShowListener);
            return new DownloadViewHolder(itemView);
        }
        else if (viewType == TYPE_TO_BE_DOWNLOAD)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_list_item_prospective,
                    parent, false);
            DownloadViewHolder holder = new DownloadViewHolder(view);
            holder.titleTx = (TextView) view.findViewById(R.id.title);
            holder.imgView = (ImageView) view.findViewById(R.id.img);
            holder.btn = view.findViewById(R.id.download_btn);
            holder.tipTx = (TextView) view.findViewById(R.id.tips);
            return holder;
        }
        return new DownloadViewHolder(new LinearLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final DownloadViewHolder holder, int position)
    {
        switch (holder.getItemViewType())
        {
            case TYPE_DOWNLOADING:
                holder.fillData((ListDownloadedData) mDatas.get(position));
                break;
            case TYPE_TO_BE_DOWNLOAD:
                final ListDownloadingData data = (ListDownloadingData) mDatas.get(position);
                holder.titleTx.setText(data.title);
                holder.tipTx.setText(data.tips);
                if (TextUtils.isEmpty(data.img))
                {
                    holder.imgView.setImageResource(R.drawable.niubility);
                }
                else
                {
                    ImageLoader.loadPic(holder.imgView, data.img);
                }
                holder.btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        goToDownload(holder.itemView.getContext(), data);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
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
        notifyItemRemoved(idx);
        mDatas.remove(idx);
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
        if (mDatas.get(position) instanceof ListDownloadedData)
        {
            return TYPE_DOWNLOADING;
        }
        else if (mDatas.get(position) instanceof ListDownloadingData)
        {
            return TYPE_TO_BE_DOWNLOAD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onStartDownload(String urlString)
    {
        int idx = findItemIdx(urlString);
        BaseDownloadData item = new ListDownloadedData();
        mDatas.add(idx, item);
        notifyItemInserted(idx);
    }

    @Override
    public void onPauseDownload(String urlString)
    {
        int idx = findItemIdx(urlString);
        int to = mDatas.size() - 1;
        notifyItemMoved(idx, to);
        BaseDownloadData item = mDatas.get(idx);
        mDatas.remove(idx);
        mDatas.add(to, item);
    }

    private void goToDownload(Context context, ListDownloadingData data)
    {
        int idx = findItemIdx(data.downloadUrl);
        ListDownloadedData item = new ListDownloadedData();
        item.title = data.title;
        item.img = data.img;
        item.downloadUrl = "http://res.wx.qq.com/voice/getvoice?mediaid=MzA4MTAxMzcxNl8yNjQ5NTkzNjI1";
        item.isCompleted = false;
        mDatas.remove(idx);
        notifyItemRemoved(idx);
        mDatas.add(0, item);
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.URL, item.downloadUrl);
        context.startService(intent);
        notifyItemInserted(0);
    }

    private int findItemIdx(String url)
    {
        for (int i = 0; i < mDatas.size(); i++)
        {
            BaseDownloadData item = mDatas.get(i);
            if (item.downloadUrl.equals(url))
            {
                return i;
            }
        }
        return -1;
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleTx;

        TextView tipTx;

        ImageView imgView;

        View btn;

        DownloadViewHolder(View itemView)
        {
            super(itemView);
        }

        void fillData(ListDownloadedData data)
        {
            if (itemView instanceof DownloadItemView)
            {
                ((DownloadItemView) itemView).setData(data);
            }
        }
    }
}
