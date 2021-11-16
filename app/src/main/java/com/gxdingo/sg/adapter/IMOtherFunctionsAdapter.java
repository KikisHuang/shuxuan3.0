package com.gxdingo.sg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IMOtherFunctionsAdapter extends PullRecyclerView.PullAdapter<IMOtherFunctionsAdapter.ViewHolder> {
    public static final int TYPE_USER = 10;
    public static final int TYPE_STORE = 11;
    public static final int TYPE_ROLE = 12;

    private Context mContext;
    private int[] mIconItems;
    private String[] mTitleItems;
    private int mType;//用户类型，10用户，11商家 12 客服

    public IMOtherFunctionsAdapter(Context context, int type) {
        mContext = context;
        mType = type;

        if (mType == TYPE_USER) {
            mIconItems = new int[]{R.drawable.module_svg_im_other_functions_photo_album
                    , R.drawable.module_svg_im_other_functions_camera
                    , R.drawable.module_svg_im_other_functions_address
                    , R.drawable.module_svg_im_other_functions_transfer_money
                    , R.drawable.module_svg_im_other_functions_phone
                    , R.drawable.module_svg_im_other_functions_complaint
            };
            mTitleItems = new String[]{"相册", "拍照", "地址", "转账", "电话", "投诉"};
        } else if (mType == TYPE_STORE) {
            mIconItems = new int[]{R.drawable.module_svg_im_other_functions_photo_album
                    , R.drawable.module_svg_im_other_functions_camera
                    , R.drawable.module_svg_im_other_functions_phone
                    , R.drawable.module_svg_im_other_functions_transfer_money
            };
            mTitleItems = new String[]{"相册", "拍照", "电话", "转账"};
        } else if (mType == TYPE_ROLE) {
            mIconItems = new int[]{R.drawable.module_svg_im_other_functions_photo_album
                    , R.drawable.module_svg_im_other_functions_camera
            };
            mTitleItems = new String[]{"相册", "拍照"};
        }
    }

    public int[] getIconItems() {
        return mIconItems;
    }

    public String[] getTitleItems() {
        return mTitleItems;
    }

    @Override
    public int getPullItemCount() {
        return mTitleItems.length;
    }

    @Override
    public ViewHolder onPullCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_item_im_other_functions, viewGroup, false);//一定传false，否则布局可能出现不填充问题
        return new ViewHolder(view);
    }

    @Override
    public void onPullBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.ivIcon.setImageResource(mIconItems[position]);
        viewHolder.tvTitle.setText(mTitleItems[position]);
    }


    public class ViewHolder extends PullRecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
