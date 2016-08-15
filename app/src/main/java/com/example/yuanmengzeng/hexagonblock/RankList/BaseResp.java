package com.example.yuanmengzeng.hexagonblock.RankList;

/**
 * 服务器端返回数据基类
 *
 * @author <a href="mailto:wentaoli@pptv.com">wentaoli</a>
 */
public abstract class BaseResp
{
    /**
     * errorCode
     */
    public int errorCode = -1;

    /**
     * message
     */
    public String message;
}
