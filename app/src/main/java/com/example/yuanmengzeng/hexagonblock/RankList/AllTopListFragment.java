package com.example.yuanmengzeng.hexagonblock.RankList;

import com.example.yuanmengzeng.hexagonblock.URL;

/**
 * 全榜 Created by yuanmengzeng on 2016/8/16.
 */
public class AllTopListFragment extends BaseRankFragment
{

    @Override
    protected String genUrl()
    {
        return URL.GET_TOP_LIST;
    }

}
