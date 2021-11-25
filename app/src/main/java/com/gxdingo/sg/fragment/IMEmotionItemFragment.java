package com.gxdingo.sg.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMEmotionItemAdapter;
import com.gxdingo.sg.adapter.IMOtherFunctionsAdapter;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullGridLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IM表情Item
 *
 * @author JM
 */
public class IMEmotionItemFragment extends Fragment {
    Activity mActivity;
    Context mContext;
    LinkedHashMap<String, Integer> mEmotionMap;
    @BindView(R.id.prv_emotion)
    PullRecyclerView prvEmotion;
    ArrayList<EmotionData> mEmotionDatas = new ArrayList<>();
    IMEmotionItemAdapter mIMEmotionItemAdapter;

    public IMEmotionItemFragment(LinkedHashMap<String, Integer> emotionMap) {
        mEmotionMap = emotionMap;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (Activity) mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_fragment_im_emotion_item, null);
        ButterKnife.bind(this, view);

        if (mEmotionMap != null) {
            for (Map.Entry<String, Integer> map : mEmotionMap.entrySet()) {
                mEmotionDatas.add(new EmotionData(map.getKey(), map.getValue()));
            }
            mIMEmotionItemAdapter = new IMEmotionItemAdapter(mContext, mEmotionDatas);
            prvEmotion.addItemDecoration(new PullDividerItemDecoration(mContext, (int) getResources().getDimension(R.dimen.dp6), (int) getResources().getDimension(R.dimen.dp6)));
            prvEmotion.setStartPulldownAnimation(false);
            prvEmotion.setLayoutManager(new PullGridLayoutManager(mContext, 8));
            prvEmotion.setPullAdapter(mIMEmotionItemAdapter);
            prvEmotion.setOnItemClickListener(new PullRecyclerView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    EmotionData emotionData = mEmotionDatas.get(position);
                    EventBus.getDefault().post(emotionData);

                }
            });
        }

        return view;
    }


    /**
     * 表情数据
     */
    public static class EmotionData {
        public String key;
        public Integer value;

        public EmotionData(String key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }
}
