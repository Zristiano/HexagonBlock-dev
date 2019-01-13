package com.example.yuanmengzeng.hexagonblock.RankList.model;

import java.io.Serializable;

/**
 * Created by yuanmengzeng on 2016/8/15.
 */
public class RankListItem implements Serializable
{
    private static final long serialVersionUID = 201608151137L;


    public String username;

    public int score;

    /**
     * 头像url
     */
    public String avatar;

    public String time;

    /**
     * 排名
     */
    public int platform;
}
