package com.gxdingo.sg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.gxdingo.sg.fragment.IMEmotionItemFragment;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IM表情适配器
 */
public class IMEmotionItemAdapter extends PullRecyclerView.PullAdapter<IMEmotionItemAdapter.ViewHolder> {
    Context mContext;
    ArrayList<IMEmotionItemFragment.EmotionData> mEmotionDatas;

    public IMEmotionItemAdapter(Context context, ArrayList<IMEmotionItemFragment.EmotionData> emotionDatas) {
        mContext = context;
        mEmotionDatas = emotionDatas;
    }

    @Override
    public int getPullItemCount() {
        if (mEmotionDatas == null) {
            return 0;
        }
        return mEmotionDatas.size();
    }

    @Override
    public ViewHolder onPullCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_emotion_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onPullBindViewHolder(ViewHolder viewHolder, int position) {
        IMEmotionItemFragment.EmotionData data = mEmotionDatas.get(position);
        viewHolder.image.setImageResource(data.value);
    }

    public class ViewHolder extends PullRecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
