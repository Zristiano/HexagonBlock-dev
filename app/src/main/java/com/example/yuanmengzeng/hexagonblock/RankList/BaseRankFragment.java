package com.example.yuanmengzeng.hexagonblock.RankList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
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
    private ListView mRankList;

    /**
     * 适配器
     */
    private RankListItemAdapter mAdapter;

    /**
     * 内容区
     */
    private View mContentView;

    /**
     * loading界面
     */
    private TextView mLoadingTv;

    /**
     * 是否正在加载数据中
     */
    protected boolean isLoading = false;

    private BaseApi.BaseRequestListener baseRequestListener;

    protected RespModel<T> mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.rank_list_view, container, false);
        initRequesListener();
        initView(root);
        return root;
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
        mRankList = (ListView) container.findViewById(R.id.rank_list);
        mAdapter = new RankListItemAdapter(getActivity());
        mRankList.setAdapter(mAdapter);
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

    private void initRequesListener()
    {
        baseRequestListener = new BaseApi.BaseRequestListener(getActivity(), new IUiListener()
        {
            @Override
            public void onComplete(String result)
            {
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
                    }

                }
                catch (JSONException e)
                {
                    ZYMLog.info("json解析错误");
                }
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

    protected BaseApi.BaseRequestListener genRequesListener()
    {
        // 结果回调
        return new BaseApi.BaseRequestListener(getActivity(), new IUiListener()
        {
            @Override
            public void onComplete(String result)
            {
                isLoading = false;
                showLoading(false, "");
                try
                {
                    JSONObject json = new JSONObject(result);
                    int errorCode = json.optInt("errorCode", -1);
                    String message = json.optString("message", getString(R.string.load_fail));
                    if (errorCode != 0)
                    {
                        onError(message);
                        return;
                    }
                    JSONArray list = new JSONArray(json.optString("result"));
                    ArrayList<HexFrvr> hexList = new ArrayList<>();
                    for (int i = 0; i < list.length(); i++)
                    {
                        HexFrvr item = HexFrvr.paserFromJson(list.getString(i));
                        if (item != null)
                        {
                            item.setRank(i + 1);
                            hexList.add(item);
                        }
                    }
                    mAdapter.setDatas(hexList);
                    return;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                onError(getString(R.string.parse_data_error));
            }

            @Override
            public void onError(String error)
            {
                isLoading = false;
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
        loadData();
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
        HttpUtils.requestAsync(getActivity(), url, null, null, HttpUtils.GET, 5000, baseRequestListener);
    }

    /**
     * 生成请求的url
     *
     * @return
     */
    protected abstract String genUrl();

    public class RankListItemAdapter extends BaseAdapter
    {

        private List<HexFrvr> mDatas = new ArrayList<HexFrvr>();

        private Context mContext;

        public RankListItemAdapter(Context context)
        {
            this.mContext = context;
        }

        /**
         * 设置数据
         *
         * @param datas 数据源
         */
        public void setDatas(List<HexFrvr> datas)
        {
            mDatas.clear();
            if (datas != null)
            {
                mDatas.addAll(datas);
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return mDatas.size();
        }

        @Override
        public HexFrvr getItem(int position)
        {
            if (position < 0 || position >= mDatas.size())
            {
                return null;
            }
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_list_item, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }
            showDatas(getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        /**
         * 设置数据
         *
         * @param frvr 数据
         * @param holder holder
         */
        private void showDatas(HexFrvr frvr, ViewHolder holder)
        {
            if (frvr == null || holder == null)
            {
                return;
            }
            holder.mNameTv.setText(frvr.getName());
            holder.mScoreTv.setText(frvr.getScore() + "");
            holder.mRankTv.setText(frvr.getRank() + "");
        }

        protected class ViewHolder
        {
            private ImageView mAvatarIv;

            private TextView mNameTv;

            private TextView mScoreTv;

            private TextView mRankTv;

            public ViewHolder(View view)
            {
                mAvatarIv = (ImageView) view.findViewById(R.id.hexfrvr_avatar_iv);
                mNameTv = (TextView) view.findViewById(R.id.hexfrvr_name_tv);
                mScoreTv = (TextView) view.findViewById(R.id.hexfrvr_score_tv);
                mRankTv = (TextView) view.findViewById(R.id.hexfrvr_rank_tv);
            }
        }
    }

}
