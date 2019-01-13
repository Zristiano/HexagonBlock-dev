package com.example.yuanmengzeng.hexagonblock.RankList;

import java.util.ArrayList;
import java.util.List;

import com.example.fresco.AsyncImageView;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.CustomView.OvalImageView;
import com.example.yuanmengzeng.hexagonblock.RankList.model.RankListItem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RankRecyclerViewAdapter extends RecyclerView.Adapter<RankRecyclerViewAdapter.ViewHolder> {

    private List<RankListItem> mItems;
    protected RankRecyclerViewAdapter(){
        mItems = new ArrayList<>();
    }

    public void setData(List<RankListItem> items){
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_list_item,parent,true));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RankListItem item = mItems.get(position);
        holder.mAvatarIv.setImageUrl(item.avatar);
        holder.mNameTv.setText(item.username);
        holder.mRankTv.setText(position+"");
        holder.mScoreTv.setText(item.score);
        switch (item.platform){
            case 0 :
                holder.platformIv.setImageResource(R.mipmap.platform_qq);
                break;
            case 1:
                holder.platformIv.setImageResource(R.mipmap.platform_weixin);
                break;
            case 2:
                holder.platformIv.setImageResource(R.mipmap.platform_facebook);
                break;
            case 3:
                holder.platformIv.setImageResource(R.mipmap.platform_instagram);
                break;
            case 4:
                holder.platformIv.setImageResource(R.mipmap.platform_twitter);
                break;
                default:
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder
    {
        private AsyncImageView mAvatarIv;

        private OvalImageView platformIv;

        private TextView mNameTv;

        private TextView mScoreTv;

        private TextView mRankTv;

        public ViewHolder(View view)
        {
            super(view);
            mAvatarIv = (AsyncImageView) view.findViewById(R.id.avatar);
            platformIv = (OvalImageView) view.findViewById(R.id.platform);
            mNameTv = (TextView) view.findViewById(R.id.username);
            mScoreTv = (TextView) view.findViewById(R.id.score);
            mRankTv = (TextView) view.findViewById(R.id.rank);
        }
    }
}
