package com.example.yuanmengzeng.hexagonblock.RankList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanmengzeng.hexagonblock.Http.BaseApi;
import com.example.yuanmengzeng.hexagonblock.Http.IUiListener;
import com.example.yuanmengzeng.hexagonblock.URL;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 全榜 Created by yuanmengzeng on 2016/8/16.
 */
public class AllTopListFragment extends BaseRankFragment
{

    @Override
    protected void initView()
    {

    }

    @Override
    protected String genUrl()
    {
        return URL.GET_TOP_LIST;
    }

    @Override
    protected BaseApi.BaseRequestListener genRequesListener()
    {
        return new BaseApi.BaseRequestListener(getActivity(), new IUiListener()
        {
            @Override
            public void onComplete(String result)
            {
                ZYMLog.info("result is " + result);
            }

            @Override
            public void onError(String error)
            {
                ZYMLog.info("error is " + error);
            }

            @Override
            public void onCancel()
            {

            }
        });
    }
}
