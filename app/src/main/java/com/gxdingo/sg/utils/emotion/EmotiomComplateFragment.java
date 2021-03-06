package com.gxdingo.sg.utils.emotion;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gxdingo.sg.R;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;

/**
 * @author: Kikis
 * @date: 2020/12/29
 * @page:
 */
public class EmotiomComplateFragment extends Fragment {

    private EmotionPagerAdapter emotionPagerGvAdapter;

    private int emotion_map_type;

    private ViewPager vp_complate_emotion_layout;

    private EmojiIndicatorView ll_point_group;

    private View view;
    private String pageFlag = "";
    protected Bundle args;


    /**
     * 创建fragment的静态方法，方便传递参数
     *
     * @param args 传递的参数
     * @return
     */
    public static <T extends Fragment> T newInstance(Class clazz, Bundle args) {
        T mFragment = null;
        try {
            mFragment = (T) clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (args != null)
            mFragment.setArguments(args);
        return mFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.module_fragment_complate_emotion, null);
        viewInit();
        init();
        return view;
    }

    private void viewInit() {
        vp_complate_emotion_layout = view.findViewById(R.id.vp_complate_emotion_layout);
        ll_point_group = view.findViewById(R.id.ll_point_group);
    }


    protected void init() {
        //获取map的类型
        emotion_map_type = args.getInt(FragmentFactory.EMOTION_MAP_TYPE);
        pageFlag = args.getString(FragmentFactory.PAGE_FLAG);

        initEmotion();
        initListener();
    }


    /**
     * 初始化监听器
     */
    protected void initListener() {

        vp_complate_emotion_layout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ll_point_group.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化表情面板
     * 思路：获取表情的总数，按每行存放7个表情，动态计算出每个表情所占的宽度大小（包含间距），
     * 而每个表情的高与宽应该是相等的，这里我们约定只存放3行
     * 每个面板最多存放7*3=21个表情，再减去一个删除键，即每个面板包含20个表情
     * 根据表情总数，循环创建多个容量为20的List，存放表情，对于大小不满20进行特殊
     * 处理即可。
     */
    private void initEmotion() {
        // 获取屏幕宽度
        int screenWidth = getScreenWidth();
        // item的间距
        int spacing = dp2px(6);
        // 动态计算item的宽度和高度
        int itemWidth = (int) ((screenWidth - spacing * 8) / 7);
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 4 + spacing * 7;

        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情的key
        for (String emojiName : EmotionUtils.getEmojiMap(emotion_map_type).keySet()) {

            emotionNames.add(emojiName);
            // 每27个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 31) {
                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                emotionViews.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }

        // 判断最后是否有不足27个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(gv);
        }

        //初始化指示器
        ll_point_group.initIndicator(emotionViews.size());
        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(emotionViews);
        vp_complate_emotion_layout.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        vp_complate_emotion_layout.setLayoutParams(params);

    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        // 创建GridView
        GridView gv = new GridView(getActivity());
        //设置点击背景透明
        gv.setSelector(android.R.color.transparent);
        //设置7列
        gv.setNumColumns(8);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);

        gv.setVerticalSpacing(padding);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setGravity(Gravity.CENTER);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, itemWidth, emotion_map_type);
        gv.setAdapter(adapter);
        //设置全局点击事件
        gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getOnItemClickListener(emotion_map_type, pageFlag));
        return gv;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
