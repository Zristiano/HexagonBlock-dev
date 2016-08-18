package com.example.yuanmengzeng.hexagonblock.RankList;

import com.example.yuanmengzeng.hexagonblock.Account.AccountInfo;
import com.example.yuanmengzeng.hexagonblock.Account.AccountUtils;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.URL;

/**
 * 个人榜 Created by yuanmengzeng on 2016/8/16.
 */
public class PersonalTopListFragment extends BaseRankFragment {

    @Override
    protected String genUrl() {
        if (getActivity() == null) {
            return null;
        }
        AccountInfo accountInfo = AccountUtils.getAccountInfo(getActivity());
        if (accountInfo == null) {
            return null;
        }
        return String.format(URL.GET_PERSONAL_LIST, accountInfo.getOpenId());
    }

    @Override
    protected void loadData() {
        if (getActivity() == null) {
            return;
        }
        AccountInfo accountInfo = AccountUtils.getAccountInfo(getActivity());
        if (accountInfo == null) {
            showLoading(true, getString(R.string.not_login));
            return;
        }
        super.loadData();
    }
}
