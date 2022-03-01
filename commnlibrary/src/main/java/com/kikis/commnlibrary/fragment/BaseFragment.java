package com.kikis.commnlibrary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.biz.PreloadListener;
import com.kikis.commnlibrary.utils.BaseLogUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static android.text.TextUtils.isEmpty;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;


/**
 * Created by lian on 2018/3/2
 * 自定抽象父类fragment;
 * 如果需要懒加载、则在lazyLoad中初始化数据、否则init中初始化即可;
 */
public abstract class BaseFragment extends Fragment implements OnRefreshListener, BasicsListener, OnLoadMoreListener, PreloadListener {
    private static final String TAG = getTAG(BaseFragment.class);

    private View view;

    protected View TitleLaouyt = null;

    private Unbinder unbinder;

    private CompositeDisposable mCompositeDisposable;

    //传递过来的参数Bundle，供子类使用
    protected Bundle args;

    public WeakReference<Context> reference;
    /**
     * 刷新控件，如需设置特定的刷新Header、Footer直接用代码实现，会覆盖xml中设置的默认刷新布局
     * refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
     * refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
     */
    protected SmartRefreshLayout refreshLayout;

    //无数据布局 ()
    protected View NoDataWallpaper;

    //权限
    private RxPermissions rxPermissions;

    private SkeletonScreen mSkeletonScreen;

    //最后点击的时间
    private long lastClickTime = 0;
    //上次点击的viewId
    private int lastClickId = 0;
    //点击时间间隔
    private int clickInterval = 300;

    //首次加载
    protected boolean isFirstLoad = true;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        reference = ((InitActivity) getActivity()).reference;
        reference = new WeakReference<Context>(getActivity());
        mCompositeDisposable = new CompositeDisposable();
        ViewInit(inflater, container);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        unbinder = ButterKnife.bind(this, view);
        if (refreshLayout() != null)
            refreshViewinit(refreshLayout());
        registerPresenter();
        init();
        eventBusInit();
        initData();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //增加了Fragment是否可见的判断
        if (!isHidden())
            lazyInit();
    }

    /**
     * Android x版本 懒加载 ，需要自行重写(fragment不销毁)
     */
    protected void lazyInit() {

    }

    /**
     * 在控件初始化之前先初始化presenter组件
     */
    protected void registerPresenter() {

    }

    private void eventBusInit() {
        //Register
        EventBus.getDefault().register(this);
    }

    private void ViewInit(LayoutInflater inflater, ViewGroup container) {

        if (activityTitleLayout() != 0) {
            TitleLaouyt = LayoutInflater.from(getActivity()).inflate(activityTitleLayout(), null);
            view = inflater.inflate(R.layout.module_activity_init, container, false);
            LinearLayout layout = view.findViewById(R.id.init_layout);
            layout.addView(TitleLaouyt);

            View bodyLayout = LayoutInflater.from(getActivity()).inflate(initContentView(), null);

            layout.addView(bodyLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
        } else
            view = inflater.inflate(initContentView(), null);

    }

    /**
     * EventBus register abstract method
     * 注： 开启后必须要有一个方法前添加 注解@Subscribe （回调方法，方法名自定），否则会报错；
     * eventbug 是否需要启用，传false则受到的event子类不会响应
     */
    protected abstract boolean eventBusRegister();


    /**
     * 页面标题布局 (如果为空则不添加标题)
     *
     * @return
     */
    protected abstract int activityTitleLayout();

    /**
     * 初始化
     *
     * @return
     */
    protected abstract int initContentView();

    /**
     * 无数据布局
     *
     * @return
     */
    protected abstract View noDataLayout();

    /**
     * 刷新据布局
     *
     * @return
     */
    protected abstract View refreshLayout();


    /**
     * 刷新 开关
     *
     * @return
     */
    protected abstract boolean refreshEnable();

    /**
     * 加载更多 开关
     *
     * @return
     */
    protected abstract boolean loadmoreEnable();


    /**
     * 所有初始化在此方法完成
     */
    protected abstract void init();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 自定义findViewById方法;
     *
     * @param viewID
     * @param <T>
     * @return
     */
    public <T> T f(int viewID) {
        return (T) view.findViewById(viewID);
    }

    /**
     * 视图销毁的时候进行的操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            view = null;
        }
        if (mSkeletonScreen != null)
            mSkeletonScreen = null;

        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        clearDisposable();

        //防止ToastUtils 内存泄漏
        ToastUtils.cancel();
    }

    public RxPermissions getRxPermissions() {

        return createRxPermissions();
    }

    protected RxPermissions createRxPermissions() {
        if (rxPermissions == null)
            rxPermissions = new RxPermissions(this);

        return rxPermissions;
    }


    /**
     * 添加骨架屏
     *
     * @param recyclerView
     * @param adapter
     * @param res
     */
    protected void newSkeletonScreen(RecyclerView recyclerView, BaseRecyclerAdapter adapter, int res, int color) {
        mSkeletonScreen = Skeleton.bind(recyclerView)
                .adapter(adapter)
                .count(20)
                .angle(20)
                .shimmer(true)
                .duration(1000)
                .color(color == 0 ? R.color.white8 : color)
                .frozen(true)
                .load(res)
                .show();
    }

    /**
     * 添加骨架屏view
     *
     * @param res
     */
    protected void newSkeletonScreen(View rootView, int res) {
        mSkeletonScreen = Skeleton.bind(rootView)
                .load(res)
                .angle(20)
                .shimmer(true)
                .duration(1000)
                .color(R.color.transparent)
                .show();
    }

    /**
     * 隐藏骨架屏
     */
    protected void hideSkeletonScreen() {

        if (mSkeletonScreen != null) {
            mSkeletonScreen.hide();
            mSkeletonScreen = null;
        }
    }

    /**
     * 无数据背景显示开关
     *
     * @param visibi
     */
    protected void NoDataBgSwitch(boolean visibi) {

        if (noDataLayout() != null && NoDataWallpaper == null) {
            if (NoDataWallpaper == null)
                NoDataWallpaper = (LinearLayout) noDataLayout();
        }
        if (NoDataWallpaper != null)
            NoDataWallpaper.setVisibility(visibi ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置无数据背景样式等
     *
     * @param hint
     */
    protected void setNodataStyle(String hint, String bthnt, int des) {

        if (noDataLayout() != null && NoDataWallpaper == null) {

            if (NoDataWallpaper == null)
                NoDataWallpaper = (LinearLayout) noDataLayout();
            if (NoDataWallpaper != null) {

                if (NoDataWallpaper.findViewById(R.id.hint_tv) != null) {
                    TextView htv = NoDataWallpaper.findViewById(R.id.hint_tv);
                    htv.setText(isEmpty(hint) ? "暂无数据" : hint);
                }

                if (NoDataWallpaper.findViewById(R.id.function_bt) != null) {

                    TextView hbt = NoDataWallpaper.findViewById(R.id.function_bt);

                    hbt.setVisibility(isEmpty(bthnt) ? View.GONE : View.VISIBLE);
                    if (!isEmpty(bthnt))
                        hbt.setText(bthnt);
                }

                if (NoDataWallpaper.findViewById(R.id.hint_img) != null) {
                    ImageView himg = NoDataWallpaper.findViewById(R.id.hint_img);
                    if (des != 0)
                        himg.setImageResource(des);
                }

            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }


    /**
     * 刷新
     *
     * @param refreshLayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        resetNoMoreData();
    }

    /**
     * 加载更多
     *
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
    }

    /**
     * 预加载更多
     */
    @Override
    public void onPreloadMore() {

    }

    /**
     * 到达底部
     */
    @Override
    public void onBottomt() {

    }

    /**
     * 刷新控件初始化
     */
    private void refreshViewinit(View refreshView) {

        refreshLayout = (SmartRefreshLayout) refreshView;
//        refreshLayout.addView(LayoutInflater.from(getActivity()).inflate(initContentView(), null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        //刷新监听设置
        refreshLayout.setOnRefreshListener(this);
        //加载更多监听设置
        refreshLayout.setOnLoadMoreListener(this);

        //设置是否在没有更多数据之后 Footer 跟随内容
        refreshLayout.setEnableFooterFollowWhenNoMoreData(false);

        //是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenRefresh(true);
        //是否在加载的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);

        //设置是否在加载更多完成之后滚动内容显示新数据
//        refreshLayout.setEnableScrollContentWhenLoaded(true);
        //是否在列表不满一页时候开启上拉加载功能
//        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);


        //是否启用加载更多
        refreshLayout.setEnableLoadMore(loadmoreEnable());
        //是否启用刷新
        refreshLayout.setEnableRefresh(refreshEnable());

        //关闭越界拖动
        refreshLayout.setEnableOverScrollDrag(false);


        MaterialHeader materialHeader = new MaterialHeader(reference.get());
        materialHeader.setColorSchemeColors(getc(R.color.green_dominant_tone));
        refreshLayout.setRefreshHeader(materialHeader);
        refreshLayout.setRefreshFooter(new ClassicsFooter(reference.get()));

        //这里是自定义刷新头部，高优先级，会覆盖xml里面的设置，如果不需要注释代码即可，
//        refreshLayout.setRefreshHeader(new CustomRefreshHeader(reference.get()));
//        refreshLayout.setRefreshFooter(new CustomRefreshFooter(reference.get(), "已经没有更多了"));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseFragmentEvent(Object object) {
        if (eventBusRegister() && object != null)
            if (object instanceof Integer)
                onTypeEvent((Integer) object);
            else
                onBaseEvent(object);

    }

    /**
     * 发送event事件
     *
     * @param object
     */
    protected void sendEvent(Object object) {
        EventBus.getDefault().post(object);
    }


    protected void onBaseEvent(Object object) {

    }

    /**
     * event事件
     *
     * @param type
     */
    protected void onTypeEvent(Integer type) {

    }

    @Override
    public void onSucceed(int type) {

    }

    @Override
    public void onRequestComplete() {
        onAfters();
        hideSkeletonScreen();

        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.finishRefresh();
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onMessage(String msg) {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.finishRefresh();

        try {
            customToast(msg);
        } catch (Exception e) {
            BaseLogUtils.e(e);
        }
    }


    @Override
    public void onStarts() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).onStarts();
    }

    @Override
    public void onStartsBanClick(View view) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).onStartsBanClick(view);
    }

    @Override
    public void onAftersResumeClick(View view) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).onAftersResumeClick(view);
    }

    @Override
    public void onAfters() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).onAfters();
    }

    @Override
    public void noData() {
        NoDataBgSwitch(true);
    }

    @Override
    public void haveData() {
        NoDataBgSwitch(false);
    }

    @Override
    public void finishLoadmore(boolean success) {
        if (refreshLayout != null)
            refreshLayout.finishLoadMore(success);
    }


    @Override
    public void finishRefreshWithNoMoreData() {
        if (refreshLayout != null)
            refreshLayout.finishRefreshWithNoMoreData();
    }

    @Override
    public void finishLoadmoreWithNoMoreData() {
        if (refreshLayout != null)
            refreshLayout.finishLoadMoreWithNoMoreData();

    }

    @Override
    public void resetNoMoreData() {
        if (refreshLayout != null)
            refreshLayout.resetNoMoreData();
    }

    @Override
    public void finishRefresh(boolean success) {
        if (refreshLayout != null)
            refreshLayout.finishRefresh(success);
    }


    /**
     * 全局点击间隔限制方法
     *
     * @return
     */
    protected boolean checkClickInterval(int id) {
        //同一个view的点击事件
        if (lastClickId == id) {
            //超过了限定的时间
            if (System.currentTimeMillis() - lastClickTime > clickInterval) {
                lastClickTime = System.currentTimeMillis();
                lastClickId = id;
                return true;
            } else {
                lastClickId = id;
                return false;
            }
        }
        lastClickTime = System.currentTimeMillis();
        lastClickId = id;
        return true;
    }

    /**
     * 添加 http Disposable
     *
     * @param disposable
     */
    protected void addDisposable(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);

    }

    /**
     * 清除disposable
     */
    protected void clearDisposable() {
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
        }
    }
}
