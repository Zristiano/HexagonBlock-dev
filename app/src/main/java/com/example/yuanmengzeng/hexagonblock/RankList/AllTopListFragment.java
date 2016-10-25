package com.example.yuanmengzeng.hexagonblock.RankList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import com.example.yuanmengzeng.hexagonblock.Http.BaseApi;
import com.example.yuanmengzeng.hexagonblock.Http.IUiListener;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.RankList.model.AllTopModel;
import com.example.yuanmengzeng.hexagonblock.RankList.model.HexFrvr;
import com.example.yuanmengzeng.hexagonblock.RankList.model.TopResModel;
import com.example.yuanmengzeng.hexagonblock.RespModel;
import com.example.yuanmengzeng.hexagonblock.URL;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 全榜 Created by yuanmengzeng on 2016/8/16.
 */
public class AllTopListFragment extends BaseRankFragment<AllTopModel>
{

    @Override
    protected String genUrl()
    {
        return URL.GET_TOP_LIST;
    }

    /**
     * 解析总榜
     * 
     * @param result 榜单数据
     * @return 总榜
     */
    @Override
    protected AllTopModel parseRankInfo(String result)
    {
        AllTopModel allTopModel = new AllTopModel();
        try
        {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                allTopModel.allTopList.add(ParseUtils.parseRankListItem(jsonArray.getJSONObject(i)));
            }
        }
        catch (JSONException e)
        {
            ZYMLog.info("解析榜单数据错误");
            return null;
        }

        return allTopModel;
    }

}
