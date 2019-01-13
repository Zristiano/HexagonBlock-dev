package com.example.yuanmengzeng.hexagonblock.RankList;

import org.json.JSONObject;

import com.example.yuanmengzeng.hexagonblock.RankList.model.RankListItem;

/**
 * Created by yuanmengzeng on 2016/8/20.
 */
public class ParseUtils
{

    public static RankListItem parseRankListItem(JSONObject jsonObject)
    {
        if (jsonObject == null)
            return null;
        RankListItem rankListItem = new RankListItem();
        rankListItem.time = jsonObject.optString("ts");
        rankListItem.avatar = jsonObject.optString("avatar");
        rankListItem.username = jsonObject.optString("username");
        rankListItem.score = jsonObject.optInt("score");
        rankListItem.platform =jsonObject.optInt("platform");
        return rankListItem;
    }
}
