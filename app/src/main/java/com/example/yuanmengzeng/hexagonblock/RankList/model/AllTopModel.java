package com.example.yuanmengzeng.hexagonblock.RankList.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 总榜数据结构 Created by yuanmengzeng on 2016/8/15.
 */
public class AllTopModel implements Serializable
{
    private static final long serialVersionUID = 201608151138L;

    /**
     * 总榜
     */
    public List<RankListItem> allTopList = new ArrayList<>();

    /**
     * 个人最好成绩
     */
    public RankListItem personalBestRank ;


}
