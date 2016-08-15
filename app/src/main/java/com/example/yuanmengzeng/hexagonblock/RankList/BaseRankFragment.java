package com.example.yuanmengzeng.hexagonblock.RankList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 排行榜基础Fragment Created by yuanmengzeng on 2016/8/12.
 */
public class BaseRankFragment extends Fragment
{
    View mRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRoot = inflater.inflate(R.layout.rank_list_view, null);
        return mRoot;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        ZYMLog.info("BaseRankFragment context is " + context);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ZYMLog.info("BaseRankFragment activity is " + activity);
    }
}
