package com.example.yuanmengzeng.hexagonblock.RankList;

import java.util.Random;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuanmengzeng.hexagonblock.Account.AccountInfo;
import com.example.yuanmengzeng.hexagonblock.Account.AccountUtils;
import com.example.yuanmengzeng.hexagonblock.Encipher.ThirdDESUtils;
import com.example.yuanmengzeng.hexagonblock.Http.BaseApi;
import com.example.yuanmengzeng.hexagonblock.Http.HttpUtils;
import com.example.yuanmengzeng.hexagonblock.Http.IUiListener;
import com.example.yuanmengzeng.hexagonblock.QQ.LoginListner;
import com.example.yuanmengzeng.hexagonblock.R;
import com.example.yuanmengzeng.hexagonblock.URL;
import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * 排行榜dialog Created by yuanmengzeng on 2016/8/12.
 */
public class RankDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener
{
    private View mRoot;

    private ViewPager viewPager;

    private ViewPagerAdapter pagerAdapter;

    private TabLayout tabLayout;

    /**
     * 当前得分
     */
    private int score;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRoot = inflater.inflate(R.layout.rank_dialog, container, false);

        ViewGroup.LayoutParams lp = mRoot.findViewById(R.id.rank_layout).getLayoutParams();
        lp.width = getContext().getResources().getDisplayMetrics().widthPixels * 3 / 4;
        lp.height = getContext().getResources().getDisplayMetrics().heightPixels * 3 / 4;
        mRoot.findViewById(R.id.rank_layout).setLayoutParams(lp);

        viewPager = (ViewPager) mRoot.findViewById(R.id.rank_viewpager);
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        tabLayout = (TabLayout) mRoot.findViewById(R.id.rank_tablayout);

        initFragment();
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.Golden));

        // viewPager.addOnPageChangeListener(new
        // TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // tabLayout.setOnTabSelectedListener(new
        // TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        tabLayout.setupWithViewPager(viewPager);

        mRoot.findViewById(R.id.left_btn).setOnClickListener(this);
        mRoot.findViewById(R.id.right_btn).setOnClickListener(this);

        return mRoot;
    }

    private void initFragment()
    {
        // tabLayout.removeAllTabs();

        pagerAdapter.addFragment(new AllTopListFragment());
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setCustomView(R.layout.tab_item);
        // tabLayout.addTab(tab1);

        pagerAdapter.addFragment(new PersonalTopListFragment());
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setCustomView(R.layout.tab_item);
        // tabLayout.addTab(tab2);
    }

    @Override
    public void dismiss()
    {
//        super.dismiss();
        ZYMLog.info("ZYM dismiss");
        dismissAllowingStateLoss();
    }


    public void setScore(int score)
    {
        this.score = score;
    }

    @Override
    public void show(FragmentManager manager, String tag)
    {
        super.show(manager, tag);
        setStyle(STYLE_NO_TITLE, R.style.rank_list_dialog);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.left_btn:
                dismiss();
                break;
            case R.id.right_btn:
                uploadScore(score);
                break;
        }
    }

    private void uploadScore(final int score)
    {
        AccountInfo accountInfo = AccountUtils.getAccountInfo(getActivity());

        if (accountInfo != null)
        {
            Bundle bundle = new Bundle();
            int index = new Random().nextInt(10) + 1;
            String data = accountInfo.getOpenId() + ThirdDESUtils.THIRD_DES_SPLIT + accountInfo.getNickname()
                    + ThirdDESUtils.THIRD_DES_SPLIT + score + ThirdDESUtils.THIRD_DES_SPLIT + accountInfo.getAvatar();
            bundle.putString("index", index + "");
            try
            {
                bundle.putString("data", ThirdDESUtils.encode(data, index));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            HttpUtils.requestAsync(getActivity(), URL.UPLOAD_SCORE, bundle, null, HttpUtils.GET, 0,
                    new BaseApi.BaseRequestListener(getActivity(), new IUiListener()
                    {

                        @Override
                        public void onComplete(String result)
                        {
                            ZYMLog.info("result is " + result);
                        }

                        @Override
                        public void onError(String error)
                        {

                        }

                        @Override
                        public void onCancel()
                        {

                        }
                    }));
            return;
        }
        // 未登录，则先登录
        AccountUtils.Login(getActivity(), new LoginListner()
        {
            @Override
            public void onLoginSuccess(AccountInfo accountInfo)
            {
                uploadScore(score);
            }

            @Override
            public void onLoginFail(String message)
            {
                ZYMLog.info("onLoginFail : " + message);
            }
        });

    }

}
