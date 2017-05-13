package com.example.yuanmengzeng.hexagonblock.Http;

import android.content.Context;
import android.os.Bundle;

import com.example.yuanmengzeng.hexagonblock.URL;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;
import com.example.yuanmengzeng.hexagonblock.model.UpgradeModel;
import com.example.yuanmengzeng.hexagonblock.util.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * 数据管理类，包括各种基础的数据请求
 * </p>
 * Created by yuanmengzeng on 2017/5/12.
 */

public class DataManager
{
    private DataManager()
    {

    }

    private static class SingleHolder
    {
        static DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance()
    {
        return SingleHolder.INSTANCE;
    }

    public void reqUpdateInfo(Context context)
    {
        BaseApi.BaseRequestListener requestListener = new BaseApi.BaseRequestListener(context, new IUiListener()
        {
            @Override
            public void onComplete(String result)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("errorCode") != 0)
                    {
                        String str = jsonObject.optString("message");
                        ZYMLog.error("解析失败：" + str);
                        return;
                    }
                    UpgradeModel upgradeModel = new UpgradeModel();
                    JSONObject resultObj = jsonObject.getJSONObject("result");
                    upgradeModel.id = resultObj.optInt("id");
                    upgradeModel.modelLevel = resultObj.optInt("model");
                    upgradeModel.versionCode = resultObj.optInt("version_code");
                    upgradeModel.versionName = resultObj.optString("version_name");
                    upgradeModel.apkUrl = resultObj.optString("apk");
                    upgradeModel.tips = resultObj.optString("tips");
                    FileUtil.writeModelToFile(UpgradeModel.class, upgradeModel);
                }
                catch (JSONException e)
                {
                    ZYMLog.error("" + e);
                }
            }

            @Override
            public void onError(String error)
            {
                ZYMLog.error("" + error);
            }

            @Override
            public void onCancel()
            {

            }
        });
        Bundle params = new Bundle();
        params.putString("versionCode", "1");
        // TODO: 2017/5/12
        HttpUtils.requestAsync(context, URL.GET_UPDATE_INFO, params, null, "GET", 10000, requestListener);
    }
}
