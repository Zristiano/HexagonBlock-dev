
package com.example.yuanmengzeng.hexagonblock.RankList.model;

import org.json.JSONObject;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * hexfrvr游戏数据
 *
 * @author wentaoli
 */
public class HexFrvr implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1726203007497918882L;

    /**
     * openId
     */
    private String openid = "";

    /**
     * 用户名
     */
    private String name = "";

    /**
     * 分数
     */
    private int score = 0;

    /**
     * 头像URL
     */
    private String url = "";

    /**
     * 添加时间
     */
    private String addTime = "";

    /**
     * 名次
     */
    private int rank = 0;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "HexFrvr [name=" + name + ", score=" + score + ", url=" + url + ", addTime=" + addTime + "]";
    }

    public static HexFrvr paserFromJson(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(jsonStr);
            HexFrvr hexFrvr = new HexFrvr();
            hexFrvr.openid = json.optString("openid");
            hexFrvr.url = json.optString("url");
            hexFrvr.score = json.optInt("score");
            hexFrvr.name = json.optString("name");
            hexFrvr.addTime = json.optString("add_time");
            return hexFrvr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
