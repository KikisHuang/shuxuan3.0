package com.gxdingo.sg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gxdingo.sg.R;
import com.gxdingo.sg.fragment.store.StoreBusinessDistrictFragment;
import com.kikis.commnlibrary.view.RoundAngleImageView;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

/**
 * 商圈评论适配器
 */
public class BusinessDistrictCommentAdapter extends PullRecyclerView.PullAdapter<BusinessDistrictCommentAdapter.CommentViewHolder> {

    private Context mContext;
    private StoreBusinessDistrictFragment.TestValue testValue;

    public BusinessDistrictCommentAdapter(Context context,StoreBusinessDistrictFragment.TestValue testValue) {
        mContext = context;
        this.testValue=testValue;
    }

    @NonNull
    @Override
    public CommentViewHolder onPullCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_item_business_district_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onPullBindViewHolder(@NonNull CommentViewHolder holder, int position) {

    }

    @Override
    public int getPullItemCount() {
        return testValue.c;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        RoundAngleImageView imageView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
