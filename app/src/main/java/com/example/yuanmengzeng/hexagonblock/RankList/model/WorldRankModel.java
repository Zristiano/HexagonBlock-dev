package com.example.yuanmengzeng.hexagonblock.RankList.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * world rank model Created by yuanmengzeng on 2016/8/15.
 */
public class WorldRankModel implements Serializable
{
    private static final long serialVersionUID = 201608151138L;

    /**
     * world rank list
     */
    public List<RankListItem> allTopList = new ArrayList<>();

    /**
     * rank of current score in the world ranking list
     */
    public int scoreRank;

}
