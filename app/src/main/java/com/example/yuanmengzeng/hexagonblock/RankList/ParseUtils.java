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
        rankListItem.addTime = jsonObject.optString("addTime");
        rankListItem.avatar = jsonObject.optString("url");
        rankListItem.name = jsonObject.optString("name");
        rankListItem.openid = jsonObject.optString("openid");
        rankListItem.score = jsonObject.optInt("score");
        return rankListItem;
    }
}
