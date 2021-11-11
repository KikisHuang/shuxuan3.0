package com.gxdingo.sg.utils.emotion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * @author: Kikis
 * @date: 2020/12/29
 * @page: 产生fragment的工厂类
 */
public class FragmentFactory {

    public static final String EMOTION_MAP_TYPE = "EMOTION_MAP_TYPE";
    public static final String PAGE_FLAG = "PAGE_FLAG";

    private static FragmentFactory factory;

    private FragmentFactory() {

    }

    /**
     * 双重检查锁定，获取工厂单例对象
     *
     * @return
     */
    public static FragmentFactory getSingleFactoryInstance() {
        if (factory == null) {
            synchronized (FragmentFactory.class) {
                if (factory == null) {
                    factory = new FragmentFactory();
                }
            }
        }
        return factory;
    }

    /**
     * 获取fragment的方法
     *
     * @param emotionType 表情类型，用于判断使用哪个map集合的表情
     * @param pageFlag
     */
    public Fragment getFragment(int emotionType, String pageFlag) {
        Bundle bundle = new Bundle();

        bundle.putInt(FragmentFactory.EMOTION_MAP_TYPE, emotionType);
        bundle.putString(PAGE_FLAG, pageFlag);

        EmotiomComplateFragment fragment = EmotiomComplateFragment.newInstance(EmotiomComplateFragment.class, bundle);

        return fragment;
    }
}
