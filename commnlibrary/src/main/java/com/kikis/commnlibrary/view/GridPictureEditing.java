package com.kikis.commnlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;
import com.kikis.commnlibrary.view.recycler_view.PullGridLayoutManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 网格图片编辑
 * 使用该控件需要调用init()初始化参数，比如最大数每行多少列等
 * （在PullRecyclerView基础上编写的）
 * Created by JM on 2020/10/22.
 */

public class GridPictureEditing extends RelativeLayout {
    public static final int TYPE_NETWORK_FILE = 0;//类型-网络图片/视频
    public static final int TYPE_LOCAL_FILE = 1;//类型-本地图片/视频

    public static final int FILE_TYPE_PICTURE = 0;//文件类型-图片
    public static final int FILE_TYPE_VIDEO = 1;//文件类型-视频

    private PullRecyclerView mPullRecyclerView;
    private GridPictureAdapter mGridPictureAdapter;
    private ArrayList<PictureValue> mPictureValues = new ArrayList<>();
    private OnGridPictureEditingClickListener mOnGridPictureEditingClickListener;
    private int mMaxCount;
    private View mAddPicture;//添加图片视图
    private int mAddPictureId;//添加图片Id
    private boolean mShowAddButton;//显示添加按钮

    private int mGridWidthHeight;//网格大小
    private CustomGridPictureEditViewHolder mCustomGridPictureEditViewHolder;//自定义网格图片编辑ViewHolder


    /**
     * 点击监听接口
     */
    public interface OnGridPictureEditingClickListener {
        void onItem(View view, int position, PictureValue pictureValue);

        void onAddButton();
    }

    public GridPictureEditing(Context context) {
        super(context);
    }

    public GridPictureEditing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridPictureEditing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GridPictureEditing(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 先调用初始化方法
     *
     * @param context
     * @param spanCount                         列数
     * @param maxCount                          最大数
     * @param addPictureId                      点击添加图片/视频的按钮图片ID
     * @param verticalSpacing                   垂直（竖）间距
     * @param horizontalSpacing                 水平（横）间距
     * @param onGridPictureEditingClickListener 点击监听接口
     */
    public void init(Context context, int spanCount, int maxCount, int addPictureId, int verticalSpacing, int horizontalSpacing, final OnGridPictureEditingClickListener onGridPictureEditingClickListener) {
        init(context, spanCount, maxCount, null, addPictureId, verticalSpacing, horizontalSpacing, 0, onGridPictureEditingClickListener);
    }

    /**
     * 先调用初始化方法
     *
     * @param context
     * @param spanCount                         列数
     * @param maxCount                          最大数
     * @param addPicture                        点击添加图片/视频的按钮图片视图
     * @param verticalSpacing                   垂直（竖）间距
     * @param horizontalSpacing                 水平（横）间距
     * @param onGridPictureEditingClickListener 点击监听接口
     */
    public void init(Context context, int spanCount, int maxCount, View addPicture, int verticalSpacing, int horizontalSpacing, final OnGridPictureEditingClickListener onGridPictureEditingClickListener) {
        init(context, spanCount, maxCount, addPicture, 0, verticalSpacing, horizontalSpacing, 0, onGridPictureEditingClickListener);
    }

    /**
     * 先调用初始化方法
     *
     * @param context
     * @param spanCount                         列数
     * @param maxCount                          最大数
     * @param addPicture                        点击添加图片/视频的按钮图片视图
     * @param addPictureId                      点击添加图片/视频的按钮图片ID（addPictureId等于0就通过addPicture设置，二者只能其一）
     * @param verticalSpacing                   垂直（竖）间距
     * @param horizontalSpacing                 水平（横）间距
     * @param gridWidthHeight                   设置Item的宽高度
     * @param onGridPictureEditingClickListener 点击监听接口
     */
    private void init(Context context, int spanCount, int maxCount, View addPicture, int addPictureId, int verticalSpacing, int horizontalSpacing, int gridWidthHeight, final OnGridPictureEditingClickListener onGridPictureEditingClickListener) {
        mMaxCount = maxCount + 1;
        mPictureValues.clear();
        if (mGridPictureAdapter != null) {
            mGridPictureAdapter.notifyDataSetChanged();
        }

        if (spanCount <= 0) {
            throw new IllegalArgumentException("spanCount 必须大于0");
        }

        mAddPictureId = addPictureId;
        mAddPicture = addPicture;

        mGridPictureAdapter = new GridPictureAdapter(context, mPictureValues, gridWidthHeight);
        mPullRecyclerView = new PullRecyclerView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPullRecyclerView.setLayoutParams(params);
        mPullRecyclerView.setStartPulldownAnimation(false);
        mPullRecyclerView.setLayoutManager(new PullGridLayoutManager(context, spanCount));
        mPullRecyclerView.addItemDecoration(new PullDividerItemDecoration(context, verticalSpacing, horizontalSpacing));
        mPullRecyclerView.setPullAdapter(mGridPictureAdapter);
        mPullRecyclerView.setOnItemClickListener(new PullRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PictureValue pictureValue = mPictureValues.get(position);
                if (onGridPictureEditingClickListener != null) {
                    if (pictureValue.type == -1) {
                        onGridPictureEditingClickListener.onAddButton();
                    } else {
                        onGridPictureEditingClickListener.onItem(view, position, pictureValue);
                    }
                }
            }
        });
        addView(mPullRecyclerView);

        //设置第一条数据（即添加按钮）
        if (mAddPictureId > 0) {
            //按钮是图片
            addValue(-1, -1, null, null, mAddPictureId);
        } else if (mAddPicture != null) {
            //按钮是视图布局
            addValue(-1, -1, null, null, mAddPicture);
        } else {
            throw new IllegalArgumentException("没有设置“添加图片”按钮资源，请通过init()方法给addPictureId或者addPicture设置参数！");
        }
    }


    /**
     * 设置最大数量
     *
     * @param maxCount
     */
    public void setMaxCount(int maxCount) {
        mMaxCount = maxCount;
    }

    /**
     * 获取数据数量
     *
     * @return
     */
    public int getValueCount() {
        int count = 0;
        for (int i = 0; i < mPictureValues.size(); i++) {
            PictureValue pictureValue = mPictureValues.get(i);
            //添加按钮不算入
            if (pictureValue.type != -1) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取数据(包含网络和本地图片/视频)
     *
     * @return
     */
    public ArrayList<PictureValue> getValues() {
        ArrayList<PictureValue> tempPictureValues = new ArrayList<>();
        for (int i = 0; i < mPictureValues.size(); i++) {
            GridPictureEditing.PictureValue pictureValue = mPictureValues.get(i);
            //添加按钮不传入
            if (pictureValue.type != -1) {
                tempPictureValues.add(pictureValue);
            }
        }
        return tempPictureValues;
    }

    /**
     * 获取本地数据(不包含网络图片/视频)
     *
     * @return
     */
    public ArrayList<PictureValue> getLocalValues() {
        ArrayList<PictureValue> tempPictureValues = new ArrayList<>();
        for (int i = 0; i < mPictureValues.size(); i++) {
            GridPictureEditing.PictureValue pictureValue = mPictureValues.get(i);
            //添加按钮不传入
            if (pictureValue.type != -1 && pictureValue.type == GridPictureEditing.TYPE_LOCAL_FILE) {
                tempPictureValues.add(pictureValue);
            }
        }
        return tempPictureValues;
    }

    /**
     * 获取网络数据(不包含本地图片/视频)
     *
     * @return
     */
    public ArrayList<PictureValue> getNetworkValues() {
        ArrayList<PictureValue> tempPictureValues = new ArrayList<>();
        for (int i = 0; i < mPictureValues.size(); i++) {
            GridPictureEditing.PictureValue pictureValue = mPictureValues.get(i);
            //添加按钮不传入
            if (pictureValue.type != -1 && pictureValue.type == GridPictureEditing.TYPE_NETWORK_FILE) {
                tempPictureValues.add(pictureValue);
            }
        }
        return tempPictureValues;
    }

    /**
     * 添加数据
     *
     * @param type
     * @param fileType
     * @param thumbnailUrl
     * @param url
     */
    public void addValue(int type, int fileType, String thumbnailUrl, String url) {
        addValue(type, fileType, thumbnailUrl, url, null);
    }

    private void addValue(int type, int fileType, String thumbnailUrl, String url, Object addButtonView) {
        if (mPictureValues.size() == 0) {
            mPictureValues.add(new PictureValue(type, fileType, thumbnailUrl, url, addButtonView));
            mShowAddButton = true;
        } else {
            mPictureValues.add(mPictureValues.size() - 1, new PictureValue(type, fileType, thumbnailUrl, url));
        }
        //当图片数量等于最大数时删掉添加按钮（最后一个Item）
        if (mMaxCount == mPictureValues.size()) {
            mPictureValues.remove(mPictureValues.size() - 1);
            mShowAddButton = false;
        }
        mGridPictureAdapter.notifyDataSetChanged();
    }


    /**
     * 删除数据
     *
     * @param position
     */
    public void removeValue(int position) {
        mPictureValues.remove(position);
        if (mPictureValues.size() < mMaxCount && !mShowAddButton) {
            if (mAddPictureId > 0) {
                mPictureValues.add(new PictureValue(-1, -1, null, null, mAddPictureId));
            } else if (mAddPicture != null) {
                mPictureValues.add(new PictureValue(-1, -1, null, null, mAddPicture));
            }
            mShowAddButton = true;
        }
        mGridPictureAdapter.notifyDataSetChanged();
    }

    /**
     * 删除所有数据
     */
    public void removeAllValue() {
        mPictureValues.clear();
        if (mAddPictureId > 0) {
            mPictureValues.add(new PictureValue(-1, -1, null, null, mAddPictureId));
        } else if (mAddPicture != null) {
            mPictureValues.add(new PictureValue(-1, -1, null, null, mAddPicture));
        }
        mShowAddButton = true;
        mGridPictureAdapter.notifyDataSetChanged();
    }

    /**
     * 图片数据类
     */
    public class PictureValue implements Serializable {

        public PictureValue(int type, int fileType, String thumbnailUrl, String url) {
            this.type = type;
            this.fileType = fileType;
            this.url = url;//图片/视频URL（路径）
            this.thumbnailUrl = thumbnailUrl;//图片/视频缩略图URL（路径）
        }

        public PictureValue(int type, int fileType, String thumbnailUrl, String url, Object addButtonResource) {
            this.type = type;
            this.fileType = fileType;
            this.url = url;//图片/视频URL（路径）
            this.thumbnailUrl = thumbnailUrl;//图片/视频缩略图URL（路径）
            this.addButtonResource = addButtonResource;
        }

        public int type;//-1添加按钮图片，0网络图片/视频，1本地图片/视频
        public int fileType;//-1添加按钮图片,0 图片，1视频
        public String thumbnailUrl;
        public String url;
        public Object addButtonResource;//添加图片按钮资源
    }

    /**
     * 自定义抽象网格图片编辑ViewHolder
     */
    public abstract static class CustomGridPictureEditViewHolder {
        /**
         * 视图Id
         *
         * @return
         */
        public abstract int getViewId();

        /**
         * 绑定视图
         *
         * @param viewHolder
         * @param position     item位置
         * @param pictureValue 图片数据
         */
        public abstract void bindView(RecyclerView.ViewHolder viewHolder, int position, PictureValue pictureValue);

        public RecyclerView.ViewHolder createView(Context context) {
            View view = LayoutInflater.from(context).inflate(getViewId(), null);
            return new CustomGridPictureEditHolder(view);
        }

        class CustomGridPictureEditHolder extends RecyclerView.ViewHolder {
            public CustomGridPictureEditHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


    /**
     * 设置自定义网格图片编辑ViewHolder
     *
     * @param customGridPictureEditViewHolder
     */
    public void setCustomGridPictureEditViewHolder(CustomGridPictureEditViewHolder customGridPictureEditViewHolder) {
        mCustomGridPictureEditViewHolder = customGridPictureEditViewHolder;
    }


    /**
     * 网格图片适配器
     */
    class GridPictureAdapter extends PullRecyclerView.PullAdapter {
        private Context mContent;
        private ArrayList<PictureValue> mPictureValues;
        private int mGridWidthHeight;

        public GridPictureAdapter(Context context, ArrayList<PictureValue> pictureValues, int gridWidthHeight) {
            mContent = context;
            mPictureValues = pictureValues;
            mGridWidthHeight = gridWidthHeight;
        }

        @Override
        public int getPullItemCount() {
            if (mPictureValues != null) {
                return mPictureValues.size();
            }
            return 0;
        }

        @Override
        public int getPullItemViewType(int position) {
            return mPictureValues.get(position).type;
        }

        @Override
        public PullRecyclerView.ViewHolder onPullCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == -1) {
                View view = LayoutInflater.from(mContent).inflate(R.layout.item_grid_add_button_view, viewGroup, false);
                return new AddButtonViewHolder(view);
            } else {
                //等于null则说明使用默认
                if (mCustomGridPictureEditViewHolder == null) {
                    View view = LayoutInflater.from(mContent).inflate(R.layout.item_grid_picture_view, viewGroup, false);
                    return new PictureViewHolder(view);
                }
                return mCustomGridPictureEditViewHolder.createView(mContent);
            }
        }

        @Override
        public void onPullBindViewHolder(PullRecyclerView.ViewHolder viewHolder, int position) {
            PictureValue pictureValue = mPictureValues.get(position);
            //添加图片按钮
            if (getPullItemViewType(position) == -1) {
                AddButtonViewHolder addButtonViewHolder = (AddButtonViewHolder) viewHolder;
                if (pictureValue.addButtonResource instanceof View) {
                    ViewGroup viewGroup = (ViewGroup) addButtonViewHolder.view;
                    viewGroup.removeAllViews();
                    viewGroup.addView((View) pictureValue.addButtonResource);
                } else {
                    addButtonViewHolder.btn.setImageResource((Integer) pictureValue.addButtonResource);
                }

                if (mGridWidthHeight > 0) {
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mGridWidthHeight, mGridWidthHeight);
                    addButtonViewHolder.view.setLayoutParams(params);
                }
            }
            //图片
            else {
                if (mCustomGridPictureEditViewHolder == null) {
                    PictureViewHolder pictureViewHolder = (PictureViewHolder) viewHolder;
                    if (mGridWidthHeight > 0) {
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mGridWidthHeight, mGridWidthHeight);
                        pictureViewHolder.view.setLayoutParams(params);
                    }
                    RequestOptions options = new RequestOptions();
                    options.placeholder(R.mipmap.news_default_icon);    //加载成功之前占位图
                    options.error(R.mipmap.news_default_icon);    //加载错误之后的错误图

                    //判断是本地图片/视频还是网络图片/视频
                    if (pictureValue.type == 0) {
                        Glide.with(mContent).load(pictureValue.thumbnailUrl).apply(options).into(pictureViewHolder.image);
                    } else {
                        //本地文件
                        File file = new File(pictureValue.thumbnailUrl);
                        Glide.with(mContent).load(file).apply(options).into(pictureViewHolder.image);
                    }
                    //如果是视频的则在缩略图上面显示多一个播放图标
                    pictureViewHolder.ivVideoFile.setVisibility(pictureValue.fileType == GridPictureEditing.FILE_TYPE_VIDEO ? VISIBLE : GONE);
                } else {
                    mCustomGridPictureEditViewHolder.bindView(viewHolder, position, pictureValue);
                }
            }
        }

        class PictureViewHolder extends PullRecyclerView.ViewHolder {
            View view;
            ImageView image;
            ImageView ivVideoFile;

            public PictureViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView.findViewById(R.id.view);
                image = itemView.findViewById(R.id.image);
                ivVideoFile = itemView.findViewById(R.id.iv_video_file);
            }
        }

        class AddButtonViewHolder extends PullRecyclerView.ViewHolder {
            View view;
            ImageView btn;

            public AddButtonViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView.findViewById(R.id.view);
                btn = itemView.findViewById(R.id.btn);
            }
        }
    }
}
