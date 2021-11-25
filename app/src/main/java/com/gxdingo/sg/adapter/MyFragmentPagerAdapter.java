package com.gxdingo.sg.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment Tab适配器
 *
 * @author pjw
 * @date 2016-01-28
 * @since JDK1.7
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    private boolean mRepeatedLoading;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    public void remove() {
        mFragments.clear();
        mFragmentTitles.clear();
    }

    /**
     * 设置是否重复加载Fragment（在ViewPager切换回来是否重新加载onCreateView()）
     */
    public void setRepeatedLoading(boolean repeatedLoading) {
        mRepeatedLoading = repeatedLoading;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //是否重复加载，默认不重复加载，需要重复加载调用setRepeatedLoading()
        if (mRepeatedLoading)
            super.destroyItem(container, position, object);
    }
}