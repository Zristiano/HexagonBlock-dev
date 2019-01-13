package com.example.yuanmengzeng.hexagonblock.RankList;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.yuanmengzeng.hexagonblock.RankList.model.WorldRankModel;
import com.example.yuanmengzeng.hexagonblock.URL;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 全榜 Created by yuanmengzeng on 2016/8/16.
 */
public class AllTopListFragment extends BaseRankFragment<WorldRankModel>
{

    @Override
    protected String genUrl()
    {
        return URL.SCORE_RANK;
    }

    @Override
    protected Bundle genParams() {
        return null;
    }

    /**
     * 解析总榜
     * 
     * @param result 榜单数据
     * @return 总榜
     */
    @Override
    protected WorldRankModel parseRankInfo(String result)
    {
        WorldRankModel worldRankModel = new WorldRankModel();
        try
        {
            JSONObject json = new JSONObject(result);
            JSONArray rankItemJsonArray = json.getJSONArray("rankList");
            for (int i = 0; i < rankItemJsonArray.length(); i++)
            {
                worldRankModel.allTopList.add(ParseUtils.parseRankListItem(rankItemJsonArray.getJSONObject(i)));
            }
            worldRankModel.scoreRank = json.optInt("individualRank",-1);
        }
        catch (JSONException e)
        {
            ZYMLog.info("解析榜单数据错误");
            return null;
        }

        return worldRankModel;
    }

    @Override
    protected void showRankList(WorldRankModel data) {
        mAdapter.setData(data.allTopList);
    }

}
