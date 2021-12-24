package com.gxdingo.sg.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.MyFragmentPagerAdapter;
import com.gxdingo.sg.utils.EmotionsUtils;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.page_indicator.CirclePageIndicator;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IM表情Fragment
 *
 * @author JM
 */
public class IMEmotionFragment extends Fragment {

    @BindView(R.id.view_pager)
    public ViewPager viewPager;

    @BindView(R.id.indicator)
    public CirclePageIndicator indicator;

    public MyFragmentPagerAdapter adapter;


    public IMEmotionFragment() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.module_fragment_im_emotion, null);
        ButterKnife.bind(this, view);

        adapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        //表情Item一
        LinkedHashMap<String, Integer> emotion1 = getEmotionGroupByIndex(0, 30);
        emotion1.put("[Backspace]", R.drawable.compose_emotion_delete);//在最后追加删除退格键

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SERIALIZABLE + 0, emotion1);

        IMEmotionItemFragment mMessageFragment1 = new IMEmotionItemFragment();
        mMessageFragment1.setArguments(bundle);

        //表情Item二
        LinkedHashMap<String, Integer> emotion2 = getEmotionGroupByIndex(31, EmotionsUtils.getCount() - 1);
        emotion2.put("[Backspace]", R.drawable.compose_emotion_delete);//在最后追加删除退格键

        Bundle bundle2 = new Bundle();
        bundle2.putSerializable(Constant.SERIALIZABLE + 0, emotion2);

        IMEmotionItemFragment mMessageFragment2 = new IMEmotionItemFragment();
        mMessageFragment2.setArguments(bundle2);


        adapter.addFragment(mMessageFragment1, "");
        adapter.addFragment(mMessageFragment2, "");
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        //indicator.setPageColor(Color.parseColor("#666666"));
        indicator.setFillColor(Color.parseColor("#ff0000"));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    /**
     * 通过索引获取表情组
     *
     * @param startIndex
     * @param endIndex
     * @return
     */
    private LinkedHashMap<String, Integer> getEmotionGroupByIndex(int startIndex, int endIndex) {
        LinkedHashMap<String, Integer> emptyMap = new LinkedHashMap<>();
        int index = 0;
        for (Map.Entry<String, Integer> map : EmotionsUtils.get().entrySet()) {
            if (index >= startIndex && index <= endIndex) {
                emptyMap.put(map.getKey(), map.getValue());
            }
            index++;
        }
        return emptyMap;
    }
}
