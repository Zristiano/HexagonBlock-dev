package com.example.yuanmengzeng.hexagonblock.RankList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yuanmengzeng.hexagonblock.Http.BaseApi;
import com.example.yuanmengzeng.hexagonblock.Http.HttpUtils;
import com.example.yuanmengzeng.hexagonblock.Http.IUiListener;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.RankList.model.HexFrvr;
import com.example.yuanmengzeng.hexagonblock.RespModel;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 排行榜基础Fragment Created by yuanmengzeng on 2016/8/12.
 */
public abstract class BaseRankFragment<T> extends Fragment
{

    /**
     * 分数列表
     */
    protected RecyclerView mRecyclerView;

    /**
     * 适配器
     */
    protected RankRecyclerViewAdapter mAdapter;

    /**
     * 内容区
     */
    protected View mContentView;

    /**
     * loading界面
     */
    protected TextView mLoadingTv;

    /**
     * 是否正在加载数据中
     */
    protected boolean isLoading = false;

    private BaseApi.BaseRequestListener baseRequestListener;

    protected RespModel<T> mData;

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (rootView==null){
            rootView = inflater.inflate(R.layout.rank_list_view, container, false);
            initRequestListener();
            initView(rootView);
            loadData();
        }
        return rootView;
    }

    /**
     * 初始化布局
     *
     * @param container 容器
     */
    protected void initView(View container)
    {
        mContentView = container.findViewById(R.id.fragment_content);
        mLoadingTv = (TextView) container.findViewById(R.id.loading);
        mRecyclerView = (RecyclerView) container.findViewById(R.id.rank_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new RankRecyclerViewAdapter();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        // 允许重试
        mLoadingTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isLoading)
                {
                    return;
                }
                loadData();
            }
        });
    }

    private void initRequestListener()
    {
        baseRequestListener = new BaseApi.BaseRequestListener(getActivity(), new IUiListener()
        {
            @Override
            public void onComplete(String result)
            {
                ZYMLog.info(result);
                isLoading = false;
                try
                {
                    if (mData == null)
                    {
                        mData = new RespModel<>();
                    }
                    JSONObject obj = new JSONObject(result);
                    mData.errorCode = obj.getInt("errorCode");
                    mData.message = obj.getString("message");
                    if (mData.errorCode != 0)
                    {
                        onError(mData.message);
                    }
                    else
                    {
                        mData.result = parseRankInfo(obj.getString("result"));
                        if (mData.result==null){
                            onError(getString(R.string.data_error));
                        }else{
                            showRankList(mData.result);
                        }
                    }
                }
                catch (JSONException e)
                {
                    ZYMLog.info("json解析错误");
                }
                showLoading(false, "");
            }

            @Override
            public void onError(String error)
            {
                isLoading = false;
                ZYMLog.info(error);
                showLoading(true, error);
            }

            @Override
            public void onCancel()
            {
                isLoading = false;
                showLoading(true, getString(R.string.load_fail) + "," + getString(R.string.retry));
            }
        });

    }

    /**
     * 解析榜单数据
     *
     * @param result 榜单数据
     * @return 榜单结构体
     */
    protected abstract T parseRankInfo(String result);

    /**
     * show the data in View
     * @param data including ranking list
     */
    protected abstract void showRankList(T data);

    /**
     * 显示or隐藏loading界面
     *
     * @param show 显示or隐藏
     * @param text 显示的文案
     */
    protected void showLoading(boolean show, String text)
    {
        if (mContentView == null)
        {
            return;
        }
        mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoadingTv.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoadingTv.setText(text);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    /**
     * 加载数据
     */
    protected void loadData()
    {
        if (getActivity() == null)
        {
            return;
        }
        isLoading = true;
        showLoading(true, getString(R.string.loading));
        String url = genUrl();
        HttpUtils.requestAsync(getActivity(), url, genParams(), null, HttpUtils.GET, 5000, baseRequestListener);
    }

    /**
     * 生成请求的url
     *
     * @return
     */
    protected abstract String genUrl();

    protected abstract Bundle genParams();

}
