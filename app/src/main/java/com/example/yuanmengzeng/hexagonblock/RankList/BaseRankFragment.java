package com.example.yuanmengzeng.hexagonblock.RankList;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanmengzeng.hexagonblock.Http.BaseApi;
import com.example.yuanmengzeng.hexagonblock.Http.HttpUtils;
import com.example.yuanmengzeng.hexagonblock.Http.IRequestListener;
import com.example.yuanmengzeng.hexagonblock.Http.IUiListener;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 排行榜基础Fragment Created by yuanmengzeng on 2016/8/12.
 */
public abstract class BaseRankFragment extends Fragment
{
    protected View mRoot;

    protected View content;

    protected View loading;

    protected View loadFail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRoot = inflater.inflate(R.layout.rank_list_view, null);
        content = mRoot.findViewById(R.id.fragment_content);
        loading = mRoot.findViewById(R.id.loading);
        loadFail = mRoot.findViewById(R.id.load_fail);

        content.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loadFail.setVisibility(View.GONE);

        initView();

        return mRoot;
    }

    protected abstract void initView();

    protected abstract String genUrl();

    protected abstract BaseApi.BaseRequestListener genRequesListener();

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData()
    {
        if (getActivity() == null)
        {
            return;
        }
        String url = genUrl();
        HttpUtils.requestAsync(getActivity(), url, null, null, HttpUtils.GET, 5000, genRequesListener());
    }
}
