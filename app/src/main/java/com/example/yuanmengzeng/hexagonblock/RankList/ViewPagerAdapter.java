package com.example.yuanmengzeng.hexagonblock.RankList;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;







import com.example.yuanmengzeng.hexagonblock.ZYMLog;

/**
 * Created by yuanmengzeng on 2016/8/12.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter
{

    private List<Fragment> rankFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public void addFragment(Fragment fragment)
    {
        rankFragments.add(fragment);
    }


    @Override
    public Fragment getItem(int position)
    {
        if (position < rankFragments.size())
        {
            return rankFragments.get(position);
        }
        else
        {
            return null;
        }
    }

    @Override
    public int getCount()
    {
        return rankFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return "" + position;
    }
}
