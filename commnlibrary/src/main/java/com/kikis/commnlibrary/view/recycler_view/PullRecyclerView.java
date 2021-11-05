package com.kikis.commnlibrary.view.recycler_view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kikis.commnlibrary.R;


/**
 * 可下拉刷新和加载更多的RecyclerView
 * <p>
 * 注意：以下3点问题，用法跟RecyclerView一样，只是适配器等相关方法前都加了Pull
 * <p>
 * 1、要调用setPullAdapter(PullAdapter adapter)来实例化适配器，不要调用setAdapter(RecyclerView.Adapter adapter),
 * 由PullRecyclerView控件自己内部调用setAdapter(RecyclerView.Adapter adapter)即可。
 * <p>
 * 2、setLayoutManager()传递PullGridLayoutManager、PullLinearLayoutManager或者PullStaggeredGridLayoutManager作为参数，
 * 这些类分别继承GridLayoutManager和LinearLayoutManager和StaggeredGridLayoutManager。
 * <p>
 * 3、默认情况下是不禁用下拉上拉功能的，需要调用setPullRefreshEnable(true)和setPullLoadEnable(true)才开启，具体可以查看两个方法的实现。
 * <p>
 * Created by JM on 2018/1/26.
 */

public class PullRecyclerView extends RecyclerView {
    private final String TAG = "PullRecyclerView";
    private Context mContext;
    private Activity mActivity;

    private OnItemClickListener mOnItemClickListener;
    private OnLongItemClickListener mOnLongItemClickListener;
    private OnTouchScrollListener mOnTouchScrollListener;
    private OnRecyclerViewListener mRecyclerViewListener;//下拉刷新、加载更多监听接口
    private HeadViewHolder mHeadViewHolder;
    private FootViewHolder mFootViewHolder;
    private PullAdapter mPullAdapter;
    private int mLastVisibleItem;
    private boolean mIsRefresh = false;//是否已是下拉刷新
    private boolean mIsPullLoad = false;//是否已是加载更多
    private boolean mIsFirst = true;
    ConfigData mConfigData;//配置参数
    private boolean mIsInternalCalls = false;//是否内部调用
    private boolean mStartPulldownAnimation = true;//是否启动下拉动画
    private int mCurY;//当前的Y轴
    private boolean mIsRollback, mIsRollback1 = true;//下拉布局高度是否在回滚
    private int mPullDownTop, mPullDownTop1 = 0;
    private int mMaxPullDownTop;//最大下拉顶部间距
    private float mPullDwnResist = 0.3f;//下拉阻力值(值越小下拉越慢)
    private boolean mIsRollbackComplete, mIsRollbackComplete1 = false;//是否回滚完成
    private int mMinusTop = 5;//减去顶部
    private int mMinusTop1 = 5;//减去顶部
    private boolean mIsPullDowStatus = false;//是否处在下拉状态
    private int mOrientation;//方向
    private boolean mIsChangeSpacing;//是否改变内间距
    private int mHorizontalSpacing;//水平间距
    private int mVerticalSpacing;//垂直间距
    private boolean mIsTouchScroll;//是否是触摸滚动
    private boolean mIsInterceptEvent;//是否拦截事件

    /**
     * 下拉刷新、加载更多监听接口
     */
    public interface OnRecyclerViewListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * Item 点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * Item长点击事件
     */
    public interface OnLongItemClickListener {
        boolean onLongClick(View view, int position);
    }

    /**
     * 触摸滚动事件
     */
    public interface OnTouchScrollListener {
        /**
         * 滚动改变
         *
         * @param recyclerView
         * @param type         0 表示下拉刷新滚动 ，1向上滚动
         */
        void onScrollChanged(RecyclerView recyclerView, int type);
    }

    public PullRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mActivity = (Activity) mContext;
        mConfigData = new ConfigData();
        mConfigData.setContext(mContext);
        mMaxPullDownTop = (int) mContext.getResources().getDimension(R.dimen.max_pull_down_top);//下拉触发刷新的最大下拉值

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {//触摸滚动
                    mIsTouchScroll = true;
                } else if (RecyclerView.SCROLL_STATE_IDLE == newState) {//滚动停止
                    mIsTouchScroll = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager params = recyclerView.getLayoutManager();
                if (params instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) params;
                    mLastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();//获取加载的最后一个可见视图在适配器的位置。
                } else if (params instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) params;
                    mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();//获取加载的最后一个可见视图在适配器的位置。
                } else if (params instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) params;
                    int[] into = staggeredGridLayoutManager.findLastVisibleItemPositions(null);// 返回当前屏幕可见最后一行的item位置（数组长度根据设置列数定）
                    if (into != null && into.length > 0) {
                        mLastVisibleItem = into[into.length - 1];//获取加载的最后一个可见视图在适配器的位置。
                    }
                }

                int itemCount = recyclerView.getLayoutManager().getItemCount();
                if (!mIsFirst && (mLastVisibleItem + 1) >= itemCount && mIsTouchScroll && mConfigData.getEnablePullLoad()) {
                    loadMoreTouchScroll();
                } else if (!mIsFirst && (mLastVisibleItem + 1) >= itemCount && !mIsTouchScroll && mConfigData.getEnablePullLoad()) {
                    loadMoreNonTouchScroll();
                }
                mIsFirst = false;
            }
        });
    }

    /**
     * 加载更多-触摸滚动
     */
    private void loadMoreTouchScroll() {
        if (mFootViewHolder != null) {
            mFootViewHolder.getProgressBar().setVisibility(VISIBLE);
            mFootViewHolder.getHintTextview().setText("加载中...");
            mFootViewHolder.getLoadMoreProgressBarLayout().setOnClickListener(null);
        }
        //如果正在下拉刷新，则不执行加载更多（下拉刷新与加载更多只能同时一个在执行，当触发两个时，后面触发的则只示但不会回调）
        if (!mIsRefresh) {
            if (!mIsPullLoad) {
                mIsPullLoad = true;
                if (mRecyclerViewListener != null) {
                    mRecyclerViewListener.onLoadMore();// 回调加载更多
                }
            }
        }
    }

    /**
     * 加载更多-非触摸滚动
     */
    private void loadMoreNonTouchScroll() {
        if (mFootViewHolder != null) {
            mFootViewHolder.getProgressBar().setVisibility(GONE);
            mFootViewHolder.getHintTextview().setText("点击这里加载更多");
            mFootViewHolder.getLoadMoreProgressBarLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //如果正在下拉刷新，则不执行加载更多（下拉刷新与加载更多只能同时一个在执行，当触发两个时，后面触发的则只显示但不会回调）
                    if (!mIsRefresh) {
                        if (!mIsPullLoad) {
                            mIsPullLoad = true;
                            if (mFootViewHolder != null) {
                                mFootViewHolder.getProgressBar().setVisibility(VISIBLE);
                                mFootViewHolder.getHintTextview().setText("加载中...");
                            }
                            loadMoreTouchScroll();
                            if (mRecyclerViewListener != null) {
                                mRecyclerViewListener.onLoadMore();// 回调加载更多
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 添加头部视图
     *
     * @param headView
     */
    public final void addHeadViewLayout(View headView) {
        addHeadViewLayout(headView, null);
    }

    public final void addHeadViewLayout(View headView, RecyclerView.LayoutParams params) {
        if (headView != null) {
            mConfigData.setHeadView(headView);
            mConfigData.setHeadParams(params);
            if (mHeadViewHolder != null) {
                mHeadViewHolder.addHeadView(headView, params);
            }
        }
    }

    /**
     * 删除头部视图（不含下拉动画布局）
     */
    public final void removeHeadViewLayout() {
        if (mHeadViewHolder != null) {
            mConfigData.setHeadView(null);
            mConfigData.setHeadParams(null);
            mHeadViewHolder.removeHeadViewLayout();
        }
    }

    /**
     * 删除页脚视图
     */
    public final void removeFooterLayout() {
        if (mFootViewHolder != null) {
            mConfigData.setFooterView(null);
            mConfigData.setFooterParams(null);
            mFootViewHolder.removeFooterLayout();
        }
    }

    /**
     * 添加页脚布局（不含上来加载更多布局）
     *
     * @param footerView
     */
    public final void addFooterLayout(View footerView) {
        addFooterLayout(footerView, null);
    }

    public final void addFooterLayout(View footerView, RecyclerView.LayoutParams params) {
        if (footerView != null) {
            mConfigData.setFooterView(footerView);
            mConfigData.setFooterParams(params);
            if (mFootViewHolder != null) {
                mFootViewHolder.addFootView(footerView, params);
            }
        }
    }

    /**
     * 设置页脚布局下面的加载更多布局背景颜色
     * 注意：一定在调用setPullAdapter()方法后调用
     *
     * @param color
     */
    public void setFootLayoutLoadMoreViewBgColor(int color) {
        if (mFootViewHolder != null) {
            mFootViewHolder.getLoadMoreLayout().setBackgroundColor(color);
        }
    }

    /**
     * 设置RecyclerView适配器
     */
    public final void setPullAdapter(final PullAdapter adapter) {
        if (adapter != null) {
            if (mPullAdapter != null) {
                mPullAdapter.getHeadViewHolder().removeHeadViewLayout();
                mPullAdapter.getFootViewHolder().removeFooterLayout();
            }
            mPullAdapter = adapter;
            mPullAdapter.setConfigData(mConfigData);
            mPullAdapter.setPullRecyclerView(this);
            mPullAdapter.setOrientation(mOrientation);
            mPullAdapter.setOnItemClickListener(mOnItemClickListener);
            mPullAdapter.setOnLongItemClickListener(mOnLongItemClickListener);
            mIsInternalCalls = true;//标记是内部调用setAdapter()
            setAdapter(mPullAdapter);

            mHeadViewHolder = mPullAdapter.getHeadViewHolder();//在setAdapter()后执行
            mFootViewHolder = mPullAdapter.getFootViewHolder();//在setAdapter()后执行
        }
    }

    /**
     * 设置RecyclerView监听器
     *
     * @param mRecyclerViewListener
     */
    public final void setOnRecyclerViewListener(OnRecyclerViewListener mRecyclerViewListener) {
        this.mRecyclerViewListener = mRecyclerViewListener;
    }

    /**
     * Item点击事件
     *
     * @param onItemClickListener
     */
    public final void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        if (mPullAdapter != null) {
            mPullAdapter.setOnItemClickListener(mOnItemClickListener);
        }
    }

    /**
     * Item长点击事件
     *
     * @param onLongItemClickListener
     */
    public final void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.mOnLongItemClickListener = onLongItemClickListener;
        if (mPullAdapter != null) {
            mPullAdapter.setOnLongItemClickListener(mOnLongItemClickListener);
        }
    }

    /**
     * 设置拦截事件防止传递到子View
     * 说明：用于PullRecyclerView内嵌PullRecyclerView的时候，内PullRecyclerView只用来展示数据不消费(如点击)事件的时候用，内PullRecyclerView调用
     *
     * @param isInterceptEvent
     */
    public void setInterceptEventPreventPassingToSubviews(boolean isInterceptEvent) {
        mIsInterceptEvent = isInterceptEvent;
    }

    /**
     * 查找第一个显示的item位置（第一个item就是headView视图）
     */
    private int findFirstVisibleItemPosition() {
        int position = 0;
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof PullGridLayoutManager) {
            position = ((PullGridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof PullLinearLayoutManager) {
            position = ((PullLinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof PullStaggeredGridLayoutManager) {
            int[] into = ((PullStaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            if (into != null && into.length > 0) {
                position = into[0];
            }
        }
        return position;
    }

    /**
     * 停止下拉刷新 (注意：加载更多数据之后，必须调用stopRefresh()方法，否则下次不会再触发下拉刷新)
     */
    public final void stopRefresh() {
        mIsRefresh = false;
        new Thread(mNoTriggerRefresh).start();
    }

    /**
     * 停止加载更多 (注意：加载更多数据之后，必须调用stopLoadMore()方法，否则下次不会再触发加载更多)
     */
    public final void stopLoadMore() {
        mIsPullLoad = false;
        loadMoreNonTouchScroll();//停止加载更多后执行非触摸滚动操作
    }

    /**
     * 设置是否禁用下拉刷新
     *
     * @param enable 默认禁用， true 不禁用(启用,显示下拉刷新布局)， false 禁用(不启用，隐藏下拉刷新布局)
     */
    public final void setPullRefreshEnable(boolean enable) {
        mConfigData.setEnablePullRefresh(enable);
        if (mHeadViewHolder != null) {
            mHeadViewHolder.setPullRefreshEnable(enable);
        }
    }

    /**
     * 设置是否禁用加载更多 (一般建议没有下一页数据的时候就设置false,有下一页就设置true)
     *
     * @param enable 默认禁用，true 不禁用(启用,显示加载更多布局)， false 禁用(不启用，隐藏加载更多布局)
     */
    public final void setPullLoadEnable(boolean enable) {
        mConfigData.setEnablePullLoad(enable);
        if (mFootViewHolder != null) {
            mFootViewHolder.setPullLoadEnable(enable);
        }
    }

    /**
     * 是否启动下拉动画
     * 【建议】:PullRecyclerView的item内嵌有PullRecyclerView时设置为false 不启动,防止滚动卡住
     *
     * @param start true启动（默认），false不启动
     */
    public final void setStartPulldownAnimation(boolean start) {
        mStartPulldownAnimation = start;
    }

    /**
     * 设置RecyclerView是否可以滚动
     *
     * @param flag true 可以滚动,false 禁止滚动
     */
    public final void setRecyclerViewScrollEnabled(boolean flag) {
        RecyclerView.LayoutManager layout = getLayoutManager();
        if (layout instanceof PullGridLayoutManager) {
            ((PullGridLayoutManager) getLayoutManager()).setScrollEnabled(flag);
        } else if (layout instanceof PullLinearLayoutManager) {
            ((PullLinearLayoutManager) getLayoutManager()).setScrollEnabled(flag);
        } else if (layout instanceof PullStaggeredGridLayoutManager) {
            ((PullStaggeredGridLayoutManager) getLayoutManager()).setScrollEnabled(flag);
        }
    }

    /**
     * 触摸滚动监听
     *
     * @param onTouchScrollListener
     */
    public void setOnTouchScrollListener(OnTouchScrollListener onTouchScrollListener) {
        mOnTouchScrollListener = onTouchScrollListener;
    }

    int mTriggerItem = -100;

    public void setTriggerItem(int position) {
        System.out.println("id:" + position);
        if (mTriggerItem != position) {
            mTriggerItem = position;
            if (mOnTouchScrollListener != null) {
                mOnTouchScrollListener.onScrollChanged(this, 0);
            }

        }
    }


    private int dpTopx(int dp) {
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        return (int) (dp * dm.density + 0.5f);
    }


    /************************ 下面是重写RecyclerView的方法 ************************/

    /**
     * 不要调用setAdapter(RecyclerView.Adapter adapter),要调用setPullAdapter(PullAdapter adapter)来实例化适配器，
     * 由PullRecyclerView控件自己内部调用setAdapter(RecyclerView.Adapter adapter)即可
     *
     * @param adapter
     * @throws
     */
    @Deprecated
    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            if (mIsInternalCalls) {
                mIsInternalCalls = false;
                super.setAdapter(adapter);
            } else {
                String mRecyclerViewName = getClass().getName();
                String mAdapterName = PullAdapter.class.getName();
                throw new PullRecyclerViewException(mRecyclerViewName + "的setAdapter(RecyclerView.Adapter adapter)由" + mRecyclerViewName + "调用，请调用" + mRecyclerViewName + "的setPullAdapter(PullAdapter adapter)，并传递" + mAdapterName + "的子类");
            }
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        //PullGridLayoutManager或者PullStaggeredGridLayoutManager并且mIsChangeSpacing等于true，才会执行下面
        if ((getLayoutManager() instanceof PullGridLayoutManager || getLayoutManager() instanceof PullStaggeredGridLayoutManager)
                && mIsChangeSpacing) {
            adjustmentSpacing();//调整内间距（由于每个item通过设置了左右间距来实现网格分割线，所以需要调整）
        }
    }

    @Override
    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        if (decor instanceof PullDividerItemDecoration) {
            PullDividerItemDecoration pullDividerItemDecoration = (PullDividerItemDecoration) decor;
            pullDividerItemDecoration.mIsChangeSpacing = mIsChangeSpacing = false;//恢复初始值
        }
        super.addItemDecoration(decor);
    }

    /**
     * 设置间距参数
     * 说明：PullGridLayoutManager或者PullStaggeredGridLayoutManager出现在两列及以上才被PullDividerItemDecoration调用
     *
     * @param isChangeSpacing   是否改变内间距
     * @param horizontalSpacing 水平间距
     * @param verticalSpacing   垂直间距
     */
    final void setSpacing(boolean isChangeSpacing, int horizontalSpacing, int verticalSpacing) {
        mIsChangeSpacing = isChangeSpacing;
        mHorizontalSpacing = horizontalSpacing;
        mVerticalSpacing = verticalSpacing;
    }

    /**
     * 调整内间距
     * 说明：PullGridLayoutManager或者PullStaggeredGridLayoutManager出现在两列及以上才被当前类和PullDividerItemDecoration调用
     */
    final void adjustmentSpacing() {
        /**
         * 水平布局：每个item的左右间距都是一样的，每行的第一个item和最后一个item根据getPaddingLeft和getPaddingRight做出对应的调整
         * 垂直布局：每个item的上下间距都是一样的，每行的第一个item和最后一个item根据getPaddingTop和getPaddingBottom做出对应的调整
         */
        int tempOrientation = 0;
        if (getLayoutManager() instanceof PullGridLayoutManager) {
            tempOrientation = ((PullGridLayoutManager) getLayoutManager()).getOrientation();
        } else if (getLayoutManager() instanceof PullStaggeredGridLayoutManager) {
            tempOrientation = ((PullStaggeredGridLayoutManager) getLayoutManager()).getOrientation();
        }
        //根据水平或者垂直布局做对应间距处理
        if (tempOrientation == PullGridLayoutManager.VERTICAL) {
            int paddingLeft = 0;
            int tempPaddingLeft = getPaddingLeft();
            if (tempPaddingLeft > 0) {
                paddingLeft = tempPaddingLeft - (mHorizontalSpacing / 2);
            } else {
                paddingLeft = -(mHorizontalSpacing / 2);
            }

            int paddingRight = 0;
            int tempPaddingRight = getPaddingRight();
            if (tempPaddingRight > 0) {
                paddingRight = tempPaddingRight - (mHorizontalSpacing / 2);
            } else {
                paddingRight = -(mHorizontalSpacing / 2);
            }

            //直接调用父类的setPadding()
            super.setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());

            //头ViewHolder的左右内间距为了不受网格内间距影响，需要做下面调整，否则头ViewHolder会出现内间距问题
            if (mHeadViewHolder != null) {
                int left = Math.abs(paddingLeft);
                int right = Math.abs(paddingRight);
                mHeadViewHolder.getHeadView().setPadding(left, getPaddingTop(), right, getPaddingBottom());
            }
            //尾ViewHolder的左右内间距为了不受网格内间距影响，需要做下面调整，否则尾ViewHolder会出现内间距问题
            if (mFootViewHolder != null) {
                int left = Math.abs(paddingLeft);
                int right = Math.abs(paddingRight);
                mFootViewHolder.getFootView().setPadding(left, getPaddingTop(), right, getPaddingBottom());
            }

        } else {
            int paddingTop = 0;
            int tempPaddingTop = getPaddingTop();
            if (tempPaddingTop > 0) {
                paddingTop = tempPaddingTop - (mHorizontalSpacing / 2);
            } else {
                paddingTop = -(mHorizontalSpacing / 2);
            }

            int paddingBottom = 0;
            int tempPaddingBottom = getPaddingBottom();
            if (tempPaddingBottom > 0) {
                paddingBottom = tempPaddingBottom - (mHorizontalSpacing / 2);
            } else {
                paddingBottom = -(mHorizontalSpacing / 2);
            }
            //直接调用父类的setPadding()
            super.setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);

            //头ViewHolder的上下内间距为了不受网格内间距影响，需要做下面调整，否则头ViewHolder会出现内间距问题
            if (mHeadViewHolder != null) {
                int top = Math.abs(paddingTop);
                int bottom = Math.abs(paddingBottom);
                mHeadViewHolder.getHeadView().setPadding(getPaddingLeft(), top, getPaddingRight(), bottom);
            }
            //尾ViewHolder的上下内间距为了不受网格内间距影响，需要做下面调整，否则尾ViewHolder会出现内间距问题
            if (mFootViewHolder != null) {
                int top = Math.abs(paddingTop);
                int bottom = Math.abs(paddingBottom);
                mFootViewHolder.getFootView().setPadding(getPaddingLeft(), top, getPaddingRight(), bottom);
            }
        }


    }

    /**
     * setLayoutManager()传参为PullGridLayoutManager或者PullLinearLayoutManager或者PullStaggeredGridLayoutManager
     *
     * @param layout
     */
    @Override
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        if (layout != null) {
            if (layout instanceof PullGridLayoutManager) {
                super.setLayoutManager(layout);
                mOrientation = ((PullGridLayoutManager) getLayoutManager()).getOrientation();
                if (mPullAdapter != null) {
                    mPullAdapter.setOrientation(mOrientation);
                }

                final PullGridLayoutManager pullGridLayoutManager = (PullGridLayoutManager) layout;
                GridLayoutManager.SpanSizeLookup spanSizeLookup = pullGridLayoutManager.getSpanSizeLookup();
                //判断是否设置了SpanSizeLookup，如果没有设置就在这里设置，否则下拉和上拉布局位置会显示不正常，DefaultSpanSizeLookup为默认的表示没有设置
                if (spanSizeLookup instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                    //设置头（下拉）尾（下拉）布局列数
                    pullGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int i) {
                            return 1;//其它item占一个位置
                        }
                    });
                }
            } else if (layout instanceof PullLinearLayoutManager) {
                super.setLayoutManager(layout);
                mOrientation = ((LinearLayoutManager) getLayoutManager()).getOrientation();
                if (mPullAdapter != null) {
                    mPullAdapter.setOrientation(mOrientation);
                }
            } else if (layout instanceof PullStaggeredGridLayoutManager) {
                super.setLayoutManager(layout);
                mOrientation = ((PullStaggeredGridLayoutManager) getLayoutManager()).getOrientation();
                if (mPullAdapter != null) {
                    mPullAdapter.setOrientation(mOrientation);
                }
            } else {
                String mRecyclerViewName = getClass().getName();
                if (layout instanceof GridLayoutManager) {
                    String mMyGridLayoutManagerName = PullGridLayoutManager.class.getName();
                    throw new PullRecyclerViewException(mRecyclerViewName
                            + "的setLayoutManager(RecyclerView.LayoutManager layout)只能传入" + mMyGridLayoutManagerName + "类及其子类");
                } else if (layout instanceof LinearLayoutManager) {
                    String mMyLinearLayoutManagerName = PullLinearLayoutManager.class.getName();
                    throw new PullRecyclerViewException(mRecyclerViewName
                            + "的setLayoutManager(RecyclerView.LayoutManager layout)只能传入" + mMyLinearLayoutManagerName + "类及其子类");
                } else if (layout instanceof StaggeredGridLayoutManager) {
                    String mMyStaggeredGridLayoutManagerName = PullStaggeredGridLayoutManager.class.getName();
                    throw new PullRecyclerViewException(mRecyclerViewName
                            + "的setLayoutManager(RecyclerView.LayoutManager layout)只能传入" + mMyStaggeredGridLayoutManagerName + "类及其子类");
                } else {
                    throw new PullRecyclerViewException(mRecyclerViewName
                            + "的setLayoutManager(RecyclerView.LayoutManager layout)目前只能传入PullGridLayoutManager.java、PullLinearLayoutManager.java和PullStaggeredGridLayoutManager.java类及其子类");
                }
            }
        }
    }

    private int t = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (mStartPulldownAnimation) {
            //垂直列表才有下拉刷新操作
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                if (mHeadViewHolder != null && mFootViewHolder != null) {
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
//                            System.out.println("触摸了1" + e.getAction());
                            t = t + 1;
                            if (!mIsRefresh) {
                                mIsRollback = false;
                                mIsRollback1 = false;
                                mCurY = (int) e.getRawY();
                                mHeadViewHolder.setHeadLineVisibility(true);//由于头布局初始化时位置完全在屏幕外面，下拉刷新无效。所以头布局要显示一点大小才能下拉刷新。
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (!mIsRefresh) {
                                int rawY = (int) e.getRawY();
                                int distanceTop = (int) ((rawY - mCurY) * mPullDwnResist);
                                int addTop = mHeadViewHolder.getHeadLayout().getPaddingTop() + distanceTop;//计算要增加的上间距
                                //如果显示第一个item(即下拉刷新布局)并且RecyclerView是处在置顶状态下，则改变下拉刷新布局顶部间距
                                if (findFirstVisibleItemPosition() == 0 && !canScrollVertically(-1)) {
                                    if (addTop >= 0) {
                                        int pullDownRefreshLayoutHeight = mHeadViewHolder.getPullDownRefreshLayout().getHeight();
                                        int pullDownRefreshLayoutWidth = mHeadViewHolder.getPullDownRefreshLayout().getWidth();

                                        //改变下拉视图状态（例：改变进度图像和进度内容）
                                        mHeadViewHolder.getPullDownRefreshLayout().changePullDownViewStatus((int) ((float) addTop / mMaxPullDownTop * 100));
                                        //下拉刷新布局通过手势拖动设置顶部间距实现拖动效果
                                        mHeadViewHolder.getPullDownRefreshLayout().setRelativeViewLocation(0, addTop - pullDownRefreshLayoutHeight, pullDownRefreshLayoutWidth, addTop);
                                        //头部布局通过手势拖动设置顶部间距实现拖动效果
                                        mHeadViewHolder.getHeadLayout().setPadding(0, addTop, 0, 0);

                                    }

                                }

                                if (mHeadViewHolder.getHeadLayout().getPaddingTop() > 0 && !mIsRefresh) {
                                    mIsPullDowStatus = true;
                                    setRecyclerViewScrollEnabled(false);//禁止滚动
                                } else {
                                    mIsPullDowStatus = false;
                                    setRecyclerViewScrollEnabled(true);//可以滚动
                                }

                                mCurY = rawY;
                            }

                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:

                            t = t + 1;
                            //System.out.println("触摸了2=" + t);
                            if (t == 1) {
                                mOnTouchScrollListener.onScrollChanged(this, 0);
                            }
                            t = 0;
                            if (!mIsRefresh) {
                                mHeadViewHolder.setHeadLineVisibility(false);
                                mIsPullDowStatus = false;
                                if (findFirstVisibleItemPosition() == 0) {
                                    //开启下拉刷新并且不是处在加载更多（下拉刷新与加载更多只能同时一个在执行，当触发两个时，后面触发的则只示但不会回调）
                                    if (mConfigData.getEnablePullRefresh() && !mIsPullLoad) {
                                        //当前顶部间距大于或者等于指定的下拉顶部间距时触发下拉刷新
                                        int top = mHeadViewHolder.getHeadLayout().getPaddingTop();
                                        if (top >= mMaxPullDownTop) {
                                            new Thread(mTriggeredRefresh).start();
                                        } else if (top > 0) {
                                            new Thread(mNoTriggerRefresh).start();
                                        }
                                    } else {
                                        mMinusTop = 10;//减去顶部间距快点
                                        new Thread(mNoTriggerRefresh).start();
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        //如果处在下拉状态或者mIsInterceptEvent为true则拦截事件，防止事件传递到子View
        if (mIsPullDowStatus || mIsInterceptEvent) {
            return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //拦截事件防止事件传递到子View
        if (mIsInterceptEvent) {
            return false;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 下拉刷新时未拉到触发刷新时回滚线程
     */
    private Runnable mNoTriggerRefresh = new Runnable() {
        @Override
        public void run() {
            mIsRollback = true;
            mIsRollbackComplete = false;
            mPullDownTop = mHeadViewHolder.getHeadLayout().getPaddingTop();//当前已下拉的顶部间距

            if (mPullDownTop > 0) {
                //通过循环让headLayout的paddingTop回滚的0位置
                while (mIsRollback) {
                    synchronized (this) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPullDownTop = mPullDownTop - mMinusTop;
                                if (mPullDownTop >= 0) {
                                    //下拉刷新布局通过线程设置顶部间距实现拖动效果
                                    mHeadViewHolder.getPullDownRefreshLayout().setRelativeViewLocation(0, mPullDownTop - mHeadViewHolder.getPullDownRefreshLayout().getHeight(), mHeadViewHolder.getPullDownRefreshLayout().getWidth(), mPullDownTop);
                                    //头部布局通过线程设置顶部间距实现拖动效果
                                    mHeadViewHolder.getHeadLayout().setPadding(0, mPullDownTop, 0, 0);

                                } else {
                                    if (!mIsRollbackComplete) {
                                        mIsRollbackComplete = true;//回滚完成
                                        mIsRollback = false;//停止回滚
                                        mMinusTop = 5;//恢复原值

                                        //下拉刷新布局通过线程设置顶部间距实现拖动效果
                                        mHeadViewHolder.getPullDownRefreshLayout().setRelativeViewLocation(0, -mHeadViewHolder.getPullDownRefreshLayout().getHeight(), mHeadViewHolder.getPullDownRefreshLayout().getWidth(), 0);
                                        //头部布局通过线程设置顶部间距实现拖动效果
                                        mHeadViewHolder.getHeadLayout().setPadding(0, 0, 0, 0);
                                        setRecyclerViewScrollEnabled(true);//可以滚动
                                        mHeadViewHolder.getPullDownRefreshLayout().changePullDownViewStatus(0);//改变下拉视图状态（例：改变进度图像和进度内容），恢复初始化内容
                                        //滚回到顶部位置
                                        post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollToPosition(0);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 下拉刷新时已拉到触发刷新时回滚线程
     */
    private Runnable mTriggeredRefresh = new Runnable() {
        @Override
        public void run() {
            mIsRollback1 = true;
            mIsRollbackComplete1 = false;
            mPullDownTop1 = mHeadViewHolder.getHeadLayout().getPaddingTop();

            if (mPullDownTop1 >= mMaxPullDownTop) {
                //通过循环让headLayout的paddingTop回滚到maxPullDownTop位置
                while (mIsRollback1) {
                    synchronized (this) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPullDownTop1 = mPullDownTop1 - mMinusTop1;
                                if (mPullDownTop1 >= mMaxPullDownTop) {
                                    //下拉刷新布局通过线程设置顶部间距实现拖动效果
                                    mHeadViewHolder.getPullDownRefreshLayout().setRelativeViewLocation(0, mPullDownTop1 - mHeadViewHolder.getPullDownRefreshLayout().getHeight(), mHeadViewHolder.getPullDownRefreshLayout().getWidth(), mPullDownTop1);
                                    //头部布局通过线程设置顶部间距实现拖动效果
                                    mHeadViewHolder.getHeadLayout().setPadding(0, mPullDownTop1, 0, 0);
                                } else {
                                    if (!mIsRollbackComplete1) {
                                        mIsRollbackComplete1 = true;//回滚完成
                                        mIsRollback1 = false;//停止回滚
                                        mIsRefresh = true;//标记在执行刷新操作

                                        //下拉刷新布局通过线程设置顶部间距实现拖动效果
                                        mHeadViewHolder.getPullDownRefreshLayout().setRelativeViewLocation(0, mMaxPullDownTop - mHeadViewHolder.getPullDownRefreshLayout().getHeight(), mHeadViewHolder.getPullDownRefreshLayout().getWidth(), mMaxPullDownTop);
                                        //头部布局通过线程设置顶部间距实现拖动效果
                                        mHeadViewHolder.getHeadLayout().setPadding(0, mMaxPullDownTop, 0, 0);
                                        setRecyclerViewScrollEnabled(true);//可以滚动
                                        mHeadViewHolder.getPullDownRefreshLayout().changePullDownViewStatus(-1);//改变下拉视图状态（例：改变进度图像和进度内容），更改为“正在加载中”内容

                                        mIsTouchScroll = false;
                                        loadMoreNonTouchScroll();//下拉刷新操作时的加载更多布局为非触摸滚动布局

                                        if (mRecyclerViewListener != null) {
                                            mRecyclerViewListener.onRefresh();
                                        }
                                    }
                                }
                            }
                        });
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 内置适配器
     * 【注意：适配器都继承PullAdapter,例如extents PullRecyclerView.PullAdapter，不要继承Adapter】
     *
     * @param <VH>
     */
    public abstract static class PullAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int HEAD = -1001;//头（含下拉刷新）
        public static final int NORMAL = -1002;//中间Item
        public static final int FOOT = -1003;//尾（含上拉加载更多）
        private PullRecyclerView mPullRecyclerView;
        private int mOrientation;
        private HeadViewHolder mHeadViewHolder;
        private FootViewHolder mFootViewHolder;
        private Context mContext;
        private OnItemClickListener mOnItemClickListener;
        private OnLongItemClickListener mOnLongItemClickListener;

        private ConfigData mConfigData;

        public PullAdapter() {

        }

        /**
         * 设置数据
         *
         * @param configData
         */
        private void setConfigData(ConfigData configData) {
            this.mConfigData = configData;
            this.mContext = this.mConfigData.getContext();

            //头View
            View headView = LayoutInflater.from(mContext).inflate(R.layout.pull_recycler_view_head, null);
            mHeadViewHolder = new HeadViewHolder(mContext, headView, mConfigData);

            //页脚View(包含加载更多)
            View footView = LayoutInflater.from(mContext).inflate(R.layout.pull_recycler_view_footer, null);
            mFootViewHolder = new FootViewHolder(footView, mConfigData);
            mFootViewHolder.changeUIBasedOnOrientation(mOrientation);//根据方向改变UI
        }


        private void setPullRecyclerView(PullRecyclerView pullRecyclerView) {
            this.mPullRecyclerView = pullRecyclerView;
        }

        private void setOrientation(int orientation) {
            this.mOrientation = orientation;
            if (mFootViewHolder != null) {
                mFootViewHolder.changeUIBasedOnOrientation(mOrientation);//根据方向改变UI
            }
        }


        private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        private void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
            this.mOnLongItemClickListener = onLongItemClickListener;
        }

        private int getHeadViewCount() {
            return 1;
        }

        private int getFootViewCount() {
            return 1;
        }

        private HeadViewHolder getHeadViewHolder() {
            return mHeadViewHolder;
        }

        private FootViewHolder getFootViewHolder() {
            return mFootViewHolder;
        }


        /**
         * 创建新View，被LayoutManager所调用(子类不能重写，请使用onPullCreateViewHolder(ViewGroup viewGroup, int viewType)抽象方法)
         */
        @Override
        public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //只有垂直布局才有头(下拉刷新)布局
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                if (viewType == HEAD) {
                    return mHeadViewHolder;
                }
            }
            if (viewType == FOOT) {
                return mFootViewHolder;
            }
            //中View（列表内容）
            return onPullCreateViewHolder(viewGroup, viewType);
        }

        /**
         * 将数据与界面进行绑定的操作(子类不能重写，请使用onPullBindViewHolder(VH vh, int position)抽象方法)
         */
        @Override
        public final void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
            int tempPosition = 0;
            //垂直布局
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                if (getItemViewType(position) == HEAD) return;
                tempPosition = position - 1;
            } else {
                tempPosition = position;
            }
            if (getItemViewType(position) == FOOT) return;

            final int tempPosition2 = tempPosition;
            onPullBindViewHolder((VH) viewHolder, tempPosition);
            if (mOnItemClickListener != null) {
                ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(viewHolder.itemView, tempPosition2);//tempPosition 不包括头尾View
                    }
                });
            }
            if (mOnLongItemClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnLongItemClickListener.onLongClick(viewHolder.itemView, tempPosition2);//tempPosition 不包括头尾View
                    }
                });
            }

            viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    System.out.println(position + "焦点" + hasFocus);
                }
            });
        }

        /**
         * 获取数据的数量(子类不能重写，请使用getPullItemCount()抽象方法)
         */
        @Override
        public final int getItemCount() {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                int count = getPullItemCount() + getHeadViewCount() + getFootViewCount();
                return count;
            }
            return getPullItemCount() + getFootViewCount();
        }

        /**
         * 获取Item视图类型(子类不能重写，请使用重写getPullItemViewType()方法)
         *
         * @param position
         * @return
         */
        @Override
        public final int getItemViewType(int position) {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                if (position < getHeadViewCount()) return HEAD;
                if (position >= getPullItemCount() + getHeadViewCount()) return FOOT;
                return getPullItemViewType(position - 1);
            } else {
                if (position >= getPullItemCount()) return FOOT;
                return getPullItemViewType(position);
            }
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            //判断若为StaggeredGridLayoutManager类型并且是下拉或者加载更多布局，占满一行
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                int position = holder.getLayoutPosition();
                if (getItemViewType(position) == HEAD || getItemViewType(position) == FOOT) {
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                    p.setFullSpan(true);
                }
            }
        }

        /**
         * 获取item视图的类型（该方法将由getItemViewType()方法回调）
         */
        public int getPullItemViewType(int position) {
            return NORMAL;
        }


        /**
         * 得到记录数（该方法将由getItemCount()方法回调）
         */
        public abstract int getPullItemCount();

        /**
         * 创建View（该方法将由onCreateViewHolder()方法回调）
         */
        public abstract VH onPullCreateViewHolder(ViewGroup viewGroup, int viewType);

        /**
         * 绑定View（该方法将由onBindViewHolder()方法回调）
         */
        public abstract void onPullBindViewHolder(VH vh, int position);

    }
}
