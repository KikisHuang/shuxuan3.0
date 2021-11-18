package com.kikis.commnlibrary.activitiy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.gyf.immersionbar.ImmersionBar;
import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.adapter.BaseRecyclerAdapter;
import com.kikis.commnlibrary.bean.GoNoticePageEvent;
import com.kikis.commnlibrary.bean.ReLoginBean;
import com.kikis.commnlibrary.bean.NewMessage;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.biz.BasicsListener;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.BaseMessageLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.KeyboardUtils.hideSoftInput;
import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;
import static com.kikis.commnlibrary.utils.Constant.CLOSE_ALL_ACTIVITY;
import static com.kikis.commnlibrary.utils.Constant.UserInfo;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;
import static com.kikis.commnlibrary.utils.SoftKeyboardUtils.isShouldHideInput;


/**
 * Created by lian on 2018/3/1.
 * 自定义抽象类 Activity;
 * 继承自RxAppCompatActivity;
 * RxAppCompatActivity 可以防止RxJava的内存泄漏，避免OOM；
 */
public abstract class BaseActivity extends RxAppCompatActivity implements OnRefreshLoadMoreListener, BasicsListener {

    private static final String TAG = getTAG(BaseActivity.class);

    private CompositeDisposable mCompositeDisposable;

    private View view;
    //标题布局
    protected View TitleLaouyt = null;
    //底部布局
    protected View BottomLaouyt = null;

    //无数据布局
    protected View NoDataWallpaper;

    //状态栏沉侵控件;
    public ImmersionBar mImmersionBar;

    //弱引用
    public WeakReference<BaseActivity> reference;

    //通用rx权限
    private RxPermissions rxPermissions;

    //最后点击的时间
    private long lastClickTime = 0;
    //上次点击的viewId
    private int lastClickId = 0;
    //点击时间间隔
    protected int clickInterval = 200;
    protected Bundle savedInstanceState;
    /**
     * 刷新控件，如需设置特定的刷新Header、Footer直接用代码实现，会覆盖xml中设置的默认刷新布局
     * refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
     * refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
     */
    protected SmartRefreshLayout refreshLayout;

    private SkeletonScreen mSkeletonScreen;


    //全局loading窗口
    protected LoadingPopupView loadingPopup;

    //Activity前后台标识符
    protected boolean isAcBackground = false;

    private FrameLayout fmlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        onBaseCreate();
        reference = new WeakReference<>(BaseActivity.this);
        mCompositeDisposable = new CompositeDisposable();
        if (initContentView() != 0)
            ViewInit();
        ButterKnife.bind(this);
        if (refreshLayout() != null)
            refreshViewinit(refreshLayout());
        eventBusInit();
        registerPresenter();
        init();
        initData();
        statusBarInit();

    }

    /**
     * setcontentview之前执行的方法
     */
    protected void onBaseCreate() {

    }


    /**
     * 在控件初始化之前先初始化presenter组件
     */
    protected void registerPresenter() {

    }

    /**
     * EventBus register abstract method
     * 注： 开启后必须要有一个方法前添加 注解@Subscribe （回调方法，方法名自定），否则会报错；
     */
    protected abstract boolean eventBusRegister();


    /**
     * 页面标题布局 (如果为空则不添加标题)
     *
     * @return
     */
    protected abstract int activityTitleLayout();

    /**
     * 沉侵状态栏控件开启
     *
     * @return
     */
    protected abstract boolean ImmersionBar();

    /**
     * 状态栏颜色 (改变颜色必须开启 ImmersionBar )
     *
     * @return
     */
    protected abstract int StatusBarColors();

    /**
     * 导航栏颜色
     *
     * @return
     */
    protected abstract int NavigationBarColor();

    /**
     * 页面底部布局 (如果为空则不添加)
     * 针对某些页面需要底部菜单和刷新布局分离
     * 添加该布局时initContentView 请设置 android:layout_weight="1" 否则不显示
     *
     * @return
     */
    protected abstract int activityBottomLayout();

    protected abstract View noDataLayout();

    /**
     * 刷新布局添加
     *
     * @return
     */
    protected abstract View refreshLayout();

    /**
     * 关闭DispatchTouchEvent隐藏软件盘事件
     *
     * @return
     */
    protected abstract boolean closeDispatchTouchEvent();

    /**
     * 布局初始化
     *
     * @return
     */
    protected abstract int initContentView();

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
     * 子类初始化
     */
    protected abstract void init();


    /**
     * 数据初始化
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
        return (T) findViewById(viewID);
    }


    @Override
    protected void onStart() {
        super.onStart();
        isAcBackground = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAcBackground = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (mSkeletonScreen != null)
            mSkeletonScreen = null;

        if (rxPermissions != null)
            rxPermissions = null;

        if (mImmersionBar != null)
            mImmersionBar = null;

        if (loadingPopup != null) {

            if (loadingPopup.isShow())
                loadingPopup.dismiss();

            loadingPopup.onDestroy();
            loadingPopup = null;
        }

        if (reference != null) {
            reference.clear();
            reference = null;
        }
        clearDisposable();
    }


    /**
     * 获取权限申请RxPermissions
     * 使用详解 ：https://github.com/tbruyelle/RxPermissions
     */
    public RxPermissions getRxPermissions() {
        return createRxPermissions();
    }

    protected RxPermissions createRxPermissions() {
        if (rxPermissions == null)
            rxPermissions = new RxPermissions(reference.get());

        return rxPermissions;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DEL)
            return true;
        else
            return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!closeDispatchTouchEvent()) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    hideSoftInput(v);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * EventBus Register;
     */
    private void eventBusInit() {
        //Register
        EventBus.getDefault().register(this);
    }

    /**
     * 页面View初始化;
     */
    protected void ViewInit() {

        if (activityTitleLayout() != 0)
            TitleLaouyt = LayoutInflater.from(this).inflate(activityTitleLayout(), new LinearLayout(this), false);

        if (activityBottomLayout() != 0)
            BottomLaouyt = LayoutInflater.from(this).inflate(activityBottomLayout(), new LinearLayout(this), false);

        if (TitleLaouyt != null) {
            view = LayoutInflater.from(this).inflate(R.layout.module_activity_init, new LinearLayout(this), false);
            LinearLayout layout = view.findViewById(R.id.init_layout);

            layout.addView(TitleLaouyt);
            LayoutInflater inflater = LayoutInflater.from(this);

            inflater.inflate(initContentView(), (ViewGroup) view);
            if (BottomLaouyt != null)
                layout.addView(BottomLaouyt);
//            layout.addView(LayoutInflater.from(this).inflate(initContentView(), null));

        } else if (BottomLaouyt != null) {
            view = LayoutInflater.from(this).inflate(R.layout.module_activity_init, new LinearLayout(this), false);
            LinearLayout layout = view.findViewById(R.id.init_layout);
            LayoutInflater inflater = LayoutInflater.from(this);

            inflater.inflate(initContentView(), (ViewGroup) view);
            if (BottomLaouyt != null)
                layout.addView(BottomLaouyt);

        } else
            view = LayoutInflater.from(this).inflate(initContentView(), new LinearLayout(this), false);


        //添加全局弹窗布局
        fmlayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.module_activity_init_fm, new FrameLayout(this), false);
        fmlayout.addView(view);

        setContentView(fmlayout);
    }


    /**
     * 刷新控件初始化
     */
    private void refreshViewinit(View refreshView) {


        refreshLayout = (SmartRefreshLayout) refreshView;
//        refreshLayout.addView(LayoutInflater.from(getActivity()).inflate(initContentView(), null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
/*        //刷新监听设置
        refreshLayout.setOnRefreshListener(this);
        //加载更多监听设置
        refreshLayout.setOnLoadMoreListener(this);*/
        //刷新、加载更多监听设置
        refreshLayout.setOnRefreshLoadMoreListener(this);
        //设置是否在没有更多数据之后 Footer 跟随内容
//        refreshLayout.setEnableFooterFollowWhenNoMoreData(false);

        //是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenRefresh(false);
        //是否在加载的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(false);

        //是否在列表不满一页时候开启上拉加载功能
//        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        //是否启用加载更多
        refreshLayout.setEnableLoadMore(loadmoreEnable());
        //是否启用刷新
        refreshLayout.setEnableRefresh(refreshEnable());

        //关闭越界拖动
        refreshLayout.setEnableOverScrollDrag(false);


        MaterialHeader materialHeader = new MaterialHeader(this);
        materialHeader.setColorSchemeResources(R.color.green_dominant_tone);
        refreshLayout.setRefreshHeader(materialHeader);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));


        //这里是自定义刷新头部，高优先级，会覆盖xml里面的设置，如果不需要注释代码即可，
//        refreshLayout.setRefreshHeader(new CustomRefreshHeader(reference.get()));
//        refreshLayout.setRefreshFooter(new CustomRefreshFooter(reference.get(), "已经没有更多了"));

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
                .angle(10)
                .count(10)
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
                .angle(10)
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
     * 获取contentview布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }


    /**
     * 显示消息弹窗
     *
     * @param unReadMessage 消息测试数据
     */
    protected synchronized void showNewMessageDialog(final ReceiveIMMessageBean unReadMessage) {


        RxUtil.observe(Schedulers.newThread(), Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> emitter) {

                emitter.onNext(unReadMessage);
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR), BaseActivity.this).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                LogUtils.i("fmlayout count === " + fmlayout.getChildCount());

                final ReceiveIMMessageBean messageInfoBean = (ReceiveIMMessageBean) o;
                final BaseMessageLayout baseMessageLayout = new BaseMessageLayout(reference.get());
                fmlayout.addView(baseMessageLayout);

                baseMessageLayout
                        .setTitle(messageInfoBean.getSubscribeListVO().getSendNickname())
                        .setContent(messageInfoBean.getSubscribeListVO().getLastMsg())
                        .setAvatar(messageInfoBean.getSubscribeListVO().getSendAvatar())
                        .setHandler(new Handler() {
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                baseMessageLayout.setVisibility(View.GONE);
                                if (fmlayout.getChildCount() > 1) {
                                    //删除所有消息视图
                                    fmlayout.removeViewInLayout(baseMessageLayout);
                                }

                            }
                        })
                        .setClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (messageInfoBean.getSubscribeListVO() != null)
                                    sendEvent(new GoNoticePageEvent(messageInfoBean.getSubscribeListVO().getShareUuid(), messageInfoBean.getSubscribeListVO().getSendUserRole()));

                                fmlayout.removeViewInLayout(baseMessageLayout);
                            }
                        }).show();

            }
        });

    }

    /**
     * 无数据背景开关
     *
     * @param visibi
     */
    protected void NoDataBgSwitch(boolean visibi) {

        if (noDataLayout() != null && NoDataWallpaper == null) {
            if (NoDataWallpaper == null)
                NoDataWallpaper = noDataLayout();
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

    /**
     * 接收event事件
     *
     * @param object
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseActivityEvent(Object object) {
        if (eventBusRegister() && object != null) {
            if (object instanceof Integer)
                onTypeEvent((Integer) object);
            else
                onBaseEvent(object);
        }
    }

    /**
     * 发送event事件
     *
     * @param object
     */
    protected void sendEvent(Object object) {
        EventBus.getDefault().post(object);
    }

    /**
     * 自定义event事件
     *
     * @param object
     */
    protected void onBaseEvent(Object object) {

        Activity activity = this;

        //重新登录全局事件
        if (object instanceof ReLoginBean)
            finish();

        //全局新消息布局
        if (!isAcBackground && object instanceof ReceiveIMMessageBean)
            showNewMessageDialog((ReceiveIMMessageBean) object);
    }

    /**
     * Stringevent事件
     *
     * @param type
     */
    protected void onTypeEvent(Integer type) {
        //关闭所有activity方法
        if (type == CLOSE_ALL_ACTIVITY)
            finish();
    }

    @Override
    public void onRequestComplete() {
        onAfters();
        hideSkeletonScreen();
    }

    @Override
    public void onSucceed(int type) {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onMessage(String msg) {
        try {
            customToast(msg);
        } catch (Exception e) {
            LogUtils.e(e);
        }

    }

    @Override
    public void onStarts() {
        showLoadingDialog("");
    }

    @Override
    public void onStartsBanClick(View view) {
        view.setEnabled(false);
        onStarts();
    }

    @Override
    public void onAftersResumeClick(View view) {
        view.setEnabled(true);
        onAfters();
    }

    @Override
    public void onAfters() {
        if (loadingPopup != null && loadingPopup.isShow()){
            loadingPopup.dismiss();
            loadingPopup.onDestroy();
            loadingPopup = null;
        }

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
     * 点击间隔限制
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
     * 沉浸式导航状态栏设置
     */
    protected void statusBarInit() {

        if (ImmersionBar()) {
            mImmersionBar = ImmersionBar.with(reference.get()).fitsSystemWindows(true);
            mImmersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(StatusBarColors() == 0 ? R.color.grayf1 : StatusBarColors());
        } else
            mImmersionBar = ImmersionBar.with(reference.get());


        if (NavigationBarColor() != 0)
            mImmersionBar.navigationBarColor(NavigationBarColor()).autoNavigationBarDarkModeEnable(true, 0.2f);

        mImmersionBar.init();
    }


    /**
     * 显示加载dialog
     *
     * @param msg
     */
    public void showLoadingDialog(String msg) {

        if (loadingPopup == null)
            loadingDialogInit();

        if (!isEmpty(msg))
            loadingPopup.setTitle(msg);

        if (!loadingPopup.isShow())
            loadingPopup.show();

    }

    /**
     * dialog初始化
     */
    protected void loadingDialogInit() {

        loadingPopup = new XPopup.Builder(reference.get())
                .hasShadowBg(false)
                .isDestroyOnDismiss(false) //对于只使用一次的弹窗，推荐设置这个
                .popupAnimation(PopupAnimation.NoAnimation)
                .asLoading();
//                .asLoading("", R.layout.module_dialog_custom);
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
            mCompositeDisposable = null;
        }
    }


}
