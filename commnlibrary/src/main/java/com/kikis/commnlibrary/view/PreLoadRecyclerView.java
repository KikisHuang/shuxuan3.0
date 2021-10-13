package com.kikis.commnlibrary.view;

import android.content.Context;
import android.util.AttributeSet;

import com.kikis.commnlibrary.biz.PreloadListener;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;

/**
 * Created by Kikis on 2018/7/17
 */

public class PreLoadRecyclerView extends RecyclerView {

    private static final String TAG = getTAG(PreLoadRecyclerView.class);

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    private LAYOUT_MANAGER_TYPE layoutManagerType;

    private PreloadListener preLoadListener = null;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;
    private long oldTime = 0;
    private long btoldTime = 0;
    private long loadInterval = 800;
    private boolean prestrain = true;

    public PreLoadRecyclerView(Context context) {
        super(context);
    }

    public PreLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPreLoadListener(PreloadListener loadMoreListener) {
        this.preLoadListener = loadMoreListener;
    }

    /**
     * 关闭掉滑动中加载;
     *
     * @param prestrain
     */
    public void setPrestrain(boolean prestrain) {
        this.prestrain = prestrain;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
//        Logger.i("onScrolled", dx + "   " + dy);

        if (preLoadListener != null) {

            if (!prestrain)
                return;

            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                } else {
                    throw new RuntimeException(
                            "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (layoutManagerType) {
                case LINEAR:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case GRID:
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
            }


            if (canScrollVertically(1)) {
            } else {
                if (System.currentTimeMillis() - btoldTime > loadInterval) {
                    btoldTime = System.currentTimeMillis();
                    preLoadListener.onBottomt();//滑动到底部
                }
            }

            int totalItemCount = layoutManager.getItemCount();

            int size = totalItemCount / 2;
            //如果数据总数大于9就开启预加载，剩下5个item实现预加载
            if (dy > 0 && totalItemCount > 9 && preLoadListener != null && lastVisibleItemPosition > size && System.currentTimeMillis() - oldTime > loadInterval) {
                oldTime = System.currentTimeMillis();
                preLoadListener.onPreloadMore();
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    @Override
    public void onScrollStateChanged(int newState) {
        super.onScrollStateChanged(newState);
    }
}
