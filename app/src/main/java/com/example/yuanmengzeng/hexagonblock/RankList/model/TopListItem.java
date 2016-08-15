package com.example.yuanmengzeng.hexagonblock.RankList.model;

import java.io.Serializable;

/**
 * Created by yuanmengzeng on 2016/8/15.
 */
public class TopListItem implements Serializable
{
    private static final long serialVersionUID = 201608151137L;

    /**
     * 账号Id
     */
    String openid;

    String name;

    int score;

    /**
     * 头像url
     */
    String avatar;

    String addTime;

    /**
     * 排名
     */
    int rank;
}
