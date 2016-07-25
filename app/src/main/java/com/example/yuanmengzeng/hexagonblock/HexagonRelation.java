package com.example.yuanmengzeng.hexagonblock;

import com.example.yuanmengzeng.hexagonblock.CustomView.HexagonView;

/**
 * 每个小六边形的邻接矩阵关系 Created by yuanmengzeng on 2016/7/22.
 */
public class HexagonRelation
{

    public static int LT = 0; // 左上邻接

    public static int RT = 1; // 右上邻接

    public static int R = 2; // 右邻接

    public static int RB = 3; // 右下邻接

    public static int LB = 4; // 左下邻接

    public static int L = 5; // 左邻接

    public HexagonView[] adjacentMatrix = new HexagonView[6]; // 邻接数组
                                                              // 每个小六边形都有六个边，最多有六个邻接六边形

    public int[] path = new int[4]; // block内部小六边形从左上角到右下角的路径

}
