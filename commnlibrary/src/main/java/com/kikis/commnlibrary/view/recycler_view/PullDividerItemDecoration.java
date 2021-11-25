package com.kikis.commnlibrary.view.recycler_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kikis.commnlibrary.R;


/**
 * PullRecylerView分割线
 *
 * @author JM
 */
public class PullDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private int mVerticalSpacing, mHorizontalSpacing;//垂直间距(行)，水平间距（列）
    private Paint mContentColorPaint;
    protected boolean mIsChangeSpacing;//是否改变内间距

    /**
     * PullLinearLayoutManager 构造函数
     *
     * @param context
     * @param verticalSpacing 垂直间距（每行间距）
     */
    public PullDividerItemDecoration(Context context, int verticalSpacing) {
        this(context, verticalSpacing, 0);
    }

    /**
     * PullGridLayoutManager 构造函数
     *
     * @param context
     * @param verticalSpacing   垂直间距（每行间距）
     * @param horizontalSpacing 水平间距（每列间距）
     */
    public PullDividerItemDecoration(Context context, int verticalSpacing, int horizontalSpacing) {
        mContext = context;
        mVerticalSpacing = verticalSpacing;
        mHorizontalSpacing = horizontalSpacing;
        mContentColorPaint = null;
    }

    /**
     * PullLinearLayoutManager 构造函数
     *
     * @param context
     * @param verticalSpacing 垂直间距（每行间距）
     * @param str             未用参数，暂时传空
     * @param color           颜色值
     */
    public PullDividerItemDecoration(Context context, int verticalSpacing, String str, int color) {
        this(context, verticalSpacing, 0, str, color);
    }

    /**
     * PullGridLayoutManager 构造函数
     *
     * @param context
     * @param verticalSpacing   垂直间距（每行间距）
     * @param horizontalSpacing 水平间距（每列间距）
     * @param str               未用参数，暂时传空
     * @param color             颜色值
     */
    public PullDividerItemDecoration(Context context, int verticalSpacing, int horizontalSpacing, String str, int color) {
        mContext = context;
        mVerticalSpacing = verticalSpacing;
        mHorizontalSpacing = horizontalSpacing;

        mContentColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentColorPaint.setColor(color);
        mContentColorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (mContentColorPaint != null) {
            PullRecyclerView pullRecyclerView = (PullRecyclerView) parent;
            boolean enablePullLoad = pullRecyclerView.mConfigData.getEnablePullLoad();//获取加载更多是否开启（不开启的则加载更多布局会被隐藏）
            int childCount = parent.getChildCount();
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

            if (layoutManager instanceof PullLinearLayoutManager) {
                int mOrientation = ((PullLinearLayoutManager) layoutManager).getOrientation();
                if (mOrientation == LinearLayoutManager.VERTICAL) {
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getChildAt(i);
                        //头（下拉刷新）和尾（加载更多）不绘制颜色分割线
                        if (child.getId() != R.id.view_head_layout && child.getId() != R.id.footer_content) {
                            int position = parent.getChildAdapterPosition(child);
                            int contentItemCount = parent.getAdapter().getItemCount() - 2;

                            /**
                             * 【绘制水平分割线颜色】
                             */
                            if (position != contentItemCount || enablePullLoad) {
                                int left = child.getLeft();
                                int right = child.getRight();
                                int top = child.getBottom();
                                int bottom = top + mVerticalSpacing;
                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getChildAt(i);
                        //尾（加载更多）不绘制颜色分割线
                        if (child.getId() != R.id.footer_content) {
                            int position = parent.getChildAdapterPosition(child);
                            int contentItemCount = parent.getAdapter().getItemCount() - 1;

                            /**
                             * 【绘制垂直分割线颜色】
                             */
                            if ((position + 1) != contentItemCount || enablePullLoad) {
                                int left = child.getRight();
                                int top = child.getTop();
                                int bottom = child.getBottom();
                                int right = child.getRight() + mVerticalSpacing;
                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
                            }
                        }
                    }
                }
            } else if (layoutManager instanceof PullGridLayoutManager) {
                //列数（跨度）
                int spanCount = ((PullGridLayoutManager) layoutManager).getSpanCount();
                //获取布局方向
                int mOrientation = ((PullGridLayoutManager) layoutManager).getOrientation();

                if (mOrientation == LinearLayoutManager.VERTICAL) {
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getChildAt(i);
                        //头（下拉刷新）和尾（加载更多）不绘制颜色分割线
                        if (child.getId() != R.id.view_head_layout && child.getId() != R.id.footer_content) {
                            int position = parent.getChildAdapterPosition(child);
                            //得到item所在行数
                            int itemRow = getItemRow(position, spanCount);
                            int contentItemCount = parent.getAdapter().getItemCount() - 2;

                            /**
                             * 【绘制水平分割线颜色】不是最后一行或者"加载更多"没有禁用的绘制底部分割线颜色
                             */
                            if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
                                int left = child.getLeft();
                                int right = child.getRight();
                                int top = child.getBottom();
                                int bottom = top + mVerticalSpacing;
                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
                            }

                            /**
                             * 【绘制垂直分割线颜色】不是行最后一个item的绘制垂直右边分割线颜色
                             */
                            if (getItemRow(position + 1, spanCount) == itemRow) {
                                int left = child.getRight();
                                int right = left + mHorizontalSpacing;
                                int top = child.getTop();
                                int bottom = 0;
                                if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
                                    bottom = child.getBottom() + mVerticalSpacing;
                                } else {
                                    bottom = child.getBottom();
                                }
                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getChildAt(i);
                        //尾（加载更多）不绘制颜色分割线
                        if (child.getId() != R.id.footer_content) {
                            int position = parent.getChildAdapterPosition(child);
                            //得到item所在行数
                            int itemRow = getItemRow(position + 1, spanCount);
                            int contentItemCount = parent.getAdapter().getItemCount() - 1;

                            /**
                             * 【绘制水平分割线颜色】不是最后一行或者"加载更多"没有禁用的绘制底部分割线颜色
                             */
                            if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
                                int left = child.getRight();
                                int right = left + mVerticalSpacing;
                                int top = child.getTop();
                                int bottom = child.getBottom();
                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
                            }

                            /**
                             * 【绘制垂直分割线颜色】不是行最后一个item的绘制垂直右边分割线颜色
                             */
                            if (getItemRow(position + 2, spanCount) == itemRow) {
                                int left = child.getLeft();
                                int right = 0;
                                int top = child.getBottom();
                                int bottom = top + mHorizontalSpacing;
                                if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
                                    right = child.getRight() + mVerticalSpacing;
                                } else {
                                    right = child.getRight();
                                }
                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
                            }
                        }
                    }
                }
            } else if (layoutManager instanceof PullStaggeredGridLayoutManager) {

                // TODO PullStaggeredGridLayoutManager暂不支持分割线颜色


//                //列数（跨度）
//                int spanCount = ((PullStaggeredGridLayoutManager) layoutManager).getSpanCount();
//                //获取布局方向
//                int mOrientation = ((PullStaggeredGridLayoutManager) layoutManager).getOrientation();
//
//                if (mOrientation == LinearLayoutManager.VERTICAL) {
//                    for (int i = 0; i < childCount; i++) {
//                        View child = parent.getChildAt(i);
//                        //头（下拉刷新）和尾（加载更多）不绘制颜色分割线
//                        if (child.getId() != R.id.view_head_layout && child.getId() != R.id.footer_content) {
//                            int position = parent.getChildAdapterPosition(child);
//                            //得到item所在行数
//                            int itemRow = getItemRow(position, spanCount);
//                            int contentItemCount = parent.getAdapter().getItemCount() - 2;
//
//                            /**
//                             * 【绘制水平分割线颜色】不是最后一行或者"加载更多"没有禁用的绘制底部分割线颜色
//                             */
//                            if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
//                                int left = child.getLeft() - (mHorizontalSpacing / 2);
//                                int right = child.getRight()+ (mHorizontalSpacing / 2);
//                                int top = child.getTop();
//                                int bottom = child.getBottom() + mVerticalSpacing;
//                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
//                            }
//
//                            /**
//                             * 【绘制垂直分割线颜色】不是行最后一个item的绘制垂直右边分割线颜色
//                             */
//                            if (getItemRow(position + 1, spanCount) == itemRow) {
//                                int left = child.getRight();
//                                int right = left + mHorizontalSpacing;
//                                int top = child.getTop();
//                                int bottom = 0;
//                                if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
//                                    bottom = child.getBottom() + mVerticalSpacing;
//                                } else {
//                                    bottom = child.getBottom();
//                                }
//                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
//                            }
//                        }
//                    }
//                } else {
//                    for (int i = 0; i < childCount; i++) {
//                        View child = parent.getChildAt(i);
//                        //尾（加载更多）不绘制颜色分割线
//                        if (child.getId() != R.id.footer_content) {
//                            int position = parent.getChildAdapterPosition(child);
//                            //得到item所在行数
//                            int itemRow = getItemRow(position + 1, spanCount);
//                            int contentItemCount = parent.getAdapter().getItemCount() - 1;
//
//                            /**
//                             * 【绘制水平分割线颜色】不是最后一行或者"加载更多"没有禁用的绘制底部分割线颜色
//                             */
//                            if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
//                                int left = child.getRight();
//                                int right = left + mVerticalSpacing;
//                                int top = child.getTop();
//                                int bottom = child.getBottom();
//                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
//                            }
//
//                            /**
//                             * 【绘制垂直分割线颜色】不是行最后一个item的绘制垂直右边分割线颜色
//                             */
//                            if (getItemRow(position + 2, spanCount) == itemRow) {
//                                int left = child.getLeft();
//                                int right = 0;
//                                int top = child.getBottom();
//                                int bottom = top + mHorizontalSpacing;
//                                if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
//                                    right = child.getRight() + mVerticalSpacing;
//                                } else {
//                                    right = child.getRight();
//                                }
//                                canvas.drawRect(left, top, right, bottom, mContentColorPaint);
//                            }
//                        }
//                    }
//                }
            }
        }
    }


    /**
     * 设置ItemView的内嵌偏移长度
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, final View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        PullRecyclerView pullRecyclerView = (PullRecyclerView) parent;
        boolean enablePullLoad = pullRecyclerView.mConfigData.getEnablePullLoad();//获取加载更多是否开启（不开启的则加载更多布局会被隐藏）
        PullRecyclerView.LayoutManager layoutManager = pullRecyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();//总item数
        final int position = parent.getChildAdapterPosition(view);//得到当前item位置

        /**
         * 根据对应布局管理器做对应的间距调整
         */
        if (layoutManager instanceof PullLinearLayoutManager) {
            //获取布局方向
            int mOrientation = ((PullLinearLayoutManager) layoutManager).getOrientation();
            //根据垂直还是水平方向设置对应的间距
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                //内容的item数
                int contentItemCount = totalItemCount - 2;
                //下拉刷新和加载更多不需要调整间距
                if (position > 0 && (position + 1) < totalItemCount) {
                    //最后一行不显示下间距
                    if (position != contentItemCount || enablePullLoad) {
                        //不禁用说明还有下一页数据，当前最后的item显示下间距（要不然下间距会和加载更多布局有重叠现象）
                        outRect.set(0, 0, 0, mVerticalSpacing);
                    } else {
                        //禁用说明最后一页，当前最后的item不显示下间距
                        outRect.set(0, 0, 0, 0);
                    }
                }
            } else {
                //内容的item数
                int contentItemCount = totalItemCount - 1;
                //加载更多item不需要调整间距
                if ((position + 1) < totalItemCount) {
                    //最后一列不显示右间距
                    if ((position + 1) != contentItemCount || enablePullLoad) {
                        outRect.set(0, 0, mVerticalSpacing, 0);
                    } else {
                        outRect.set(0, 0, 0, 0);
                    }
                }
            }
        } else if (layoutManager instanceof PullGridLayoutManager || layoutManager instanceof PullStaggeredGridLayoutManager) {
            //列数（跨度）
            int spanCount = 0;
            //获取布局方向
            int mOrientation = 0;
            if (layoutManager instanceof PullGridLayoutManager) {
                spanCount = ((PullGridLayoutManager) layoutManager).getSpanCount();
                mOrientation = ((PullGridLayoutManager) layoutManager).getOrientation();
            } else if (layoutManager instanceof PullStaggeredGridLayoutManager) {
                spanCount = ((PullStaggeredGridLayoutManager) layoutManager).getSpanCount();
                mOrientation = ((PullStaggeredGridLayoutManager) layoutManager).getOrientation();
            }

            //根据垂直还是水平方向设置对应的间距
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                //内容的item数
                int contentItemCount = totalItemCount - 2;
                //下拉刷新和加载更多不需要调整间距
                if (position > 0 && (position + 1) < totalItemCount) {
                    //得到item所在行数
                    int itemRow = getItemRow(position, spanCount);
                    //是否是最后一行最后一页（enablePullLoad等于false）？,最后一行最后一页不显示下间距
                    if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
                        //不禁用说明还有下一页数据，当前最后的item显示下间距（要不然下间距会和加载更多布局有重叠现象）
                        outRect.set(mHorizontalSpacing / 2, 0, mHorizontalSpacing / 2, mVerticalSpacing);
                    } else {
                        //禁用说明最后一页，当前最后的item不显示下间距
                        outRect.set(mHorizontalSpacing / 2, 0, mHorizontalSpacing / 2, 0);
                    }

                    if (!mIsChangeSpacing) {
                        mIsChangeSpacing = true;
                        PullRecyclerView recyclerView = (PullRecyclerView) parent;
                        recyclerView.setSpacing(mIsChangeSpacing, mHorizontalSpacing, mVerticalSpacing);
                        recyclerView.adjustmentSpacing();
                    }
                }
            } else {
                //内容的item数
                int contentItemCount = totalItemCount - 1;
                //加载更多item不需要调整间距
                if ((position + 1) < totalItemCount) {
                    //得到item所在行数
                    int itemRow = getItemRow(position + 1, spanCount);
                    //是否是最后一行最后一页（enablePullLoad等于false）？,最后一行最后一页不显示右间距
                    if (!itemIsLastRow(itemRow, contentItemCount, spanCount) || enablePullLoad) {
                        outRect.set(0, mHorizontalSpacing / 2, mVerticalSpacing, mHorizontalSpacing / 2);
                    } else {
                        outRect.set(0, mHorizontalSpacing / 2, 0, mHorizontalSpacing / 2);
                    }

                    if (!mIsChangeSpacing) {
                        mIsChangeSpacing = true;
                        PullRecyclerView recyclerView = (PullRecyclerView) parent;
                        recyclerView.setSpacing(mIsChangeSpacing, mHorizontalSpacing, mVerticalSpacing);
                        recyclerView.adjustmentSpacing();
                    }
                }
            }
        }
    }

    /**
     * 获取item所在行
     *
     * @param position  item位置(从1开始)
     * @param spanCount 列数
     * @return
     */

    private int getItemRow(int position, int spanCount) {
        int row = 0;//当前item所在行
        if (position % spanCount == 0) {
            row = position / spanCount;
        } else {
            row = position / spanCount + 1;
        }
        return row;
    }

    /**
     * item是否是最后一行
     *
     * @param itemRow
     * @param contentItemCount
     * @param spanCount
     * @return
     */
    private boolean itemIsLastRow(int itemRow, int contentItemCount, int spanCount) {
        int lastRow = 0;//最后行数
        if (contentItemCount % spanCount == 0) {
            lastRow = contentItemCount / spanCount;
        } else {
            lastRow = contentItemCount / spanCount + 1;
        }
        //判断当前item是否是最后一行
        if (itemRow == lastRow) {
            return true;
        }
        return false;
    }
}
