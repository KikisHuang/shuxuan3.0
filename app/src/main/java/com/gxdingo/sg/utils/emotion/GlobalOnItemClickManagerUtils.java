package com.gxdingo.sg.utils.emotion;

import android.widget.AdapterView;

import com.gxdingo.sg.bean.GlobalBean;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Kikis on 2019/10/28.
 * 点击表情的全局监听管理类
 */
public class GlobalOnItemClickManagerUtils {

    public static AdapterView.OnItemClickListener getOnItemClickListener(final int emotion_map_type, final String pageFlag) {
        return (parent, view, position, id) -> {
            Object itemAdapter = parent.getAdapter();

            if (itemAdapter instanceof EmotionGridViewAdapter) {
                // 点击的是表情
                EmotionGridViewAdapter emotionGvAdapter = (EmotionGridViewAdapter) itemAdapter;

                //将事件返回中执行，完美解决一些没必要的bug
                if (position == emotionGvAdapter.getCount() - 1)
                    EventBus.getDefault().post(new GlobalBean(0, "", emotion_map_type, pageFlag));
                else {
                    // 如果点击了表情,则添加到输入框中
                    String emotionName = emotionGvAdapter.getItem(position);

                    EventBus.getDefault().post(new GlobalBean(1, emotionName, emotion_map_type, pageFlag));

                }

            }
        };
    }

}
