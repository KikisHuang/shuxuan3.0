package com.gxdingo.sg.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gxdingo.sg.fragment.store.BusinessDistrictFragment;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.utils.Constant;

import java.util.List;

import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;


/**
 * Created by Kikis on 2021/12/10.
 * TabViewPager适配器
 */
public class TabPageAdapter<T> extends FragmentStatePagerAdapter {


    public static final int STORE_BUSINESS_DISTRICT_TAB = 1;
    public static final int RANKING_TAB = 2;

    public static final String TAB = "tab";

    private List<T> list;

    private int FLAG;

    public TabPageAdapter(FragmentManager fm, List<T> urlList, int hometab) {
        super(fm);
        this.list = urlList;
        this.FLAG = hometab;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt(TAB, position);
        switch (FLAG) {
            case STORE_BUSINESS_DISTRICT_TAB: {
                args.putInt(Constant.PARAMAS + 0, position + 1);
                if (position == 1) {
                    if (UserInfoUtils.getInstance().getUserInfo() != null && UserInfoUtils.getInstance().isLogin() && !isEmpty(UserInfoUtils.getInstance().getUserInfo().getIdentifier()))
                        //我的商圈，传自己的Identifier
                        args.putString(Constant.SERIALIZABLE + 0, UserInfoUtils.getInstance().getUserInfo().getIdentifier());
                }
                return BusinessDistrictFragment.newInstance(BusinessDistrictFragment.class, args);
            }
            case RANKING_TAB: {
                //todo 写到了排行榜子类
                return BusinessDistrictFragment.newInstance(BusinessDistrictFragment.class, args);
            }

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (String) list.get(position);
    }

    // 动态设置我们标题的方法
    public void setPageTitle(int position, String title) {
        if (position >= 0 && position < list.size()) {
            list.set(position, (T) title);
//            notifyDataSetChanged();
        }
    }

    // 必须重写此方法，不然，数据源改变，fragment数据不刷新
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return list.size();//指定ViewPager的总页数
    }


}
