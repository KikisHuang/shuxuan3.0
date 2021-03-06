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
import com.blankj.utilcode.util.ToastUtils;
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
import com.kikis.commnlibrary.utils.BaseLogUtils;
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
import com.tencent.bugly.crashreport.CrashReport;
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
import static com.kikis.commnlibrary.utils.Constant.CHAT_IDENTIFIER;
import static com.kikis.commnlibrary.utils.Constant.CLOSE_ALL_ACTIVITY;
import static com.kikis.commnlibrary.utils.Constant.UserInfo;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;
import static com.kikis.commnlibrary.utils.SoftKeyboardUtils.isShouldHideInput;


/**
 * Created by lian on 2018/3/1.
 * ?????????????????? Activity;
 * ?????????RxAppCompatActivity;
 * RxAppCompatActivity ????????????RxJava????????????????????????OOM???
 */
public abstract class BaseActivity extends RxAppCompatActivity implements OnRefreshLoadMoreListener, BasicsListener {

    private static final String TAG = getTAG(BaseActivity.class);

    private CompositeDisposable mCompositeDisposable;

    private View view;

    //????????????
    protected View TitleLaouyt = null;
    //????????????
    protected View BottomLaouyt = null;

    //???????????????
    protected View NoDataWallpaper;

    //?????????????????????;
    public ImmersionBar mImmersionBar;

    //?????????
    public WeakReference<BaseActivity> reference;

    //??????rx??????
    private RxPermissions rxPermissions;

    //?????????????????????
    private long lastClickTime = 0;
    //???????????????viewId
    private int lastClickId = 0;
    //??????????????????
    protected int clickInterval = 200;
    protected Bundle savedInstanceState;
    /**
     * ??????????????????????????????????????????Header???Footer?????????????????????????????????xml??????????????????????????????
     * refreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
     * refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
     */
    protected SmartRefreshLayout refreshLayout;

    private SkeletonScreen mSkeletonScreen;


    //??????loading??????
    protected LoadingPopupView loadingPopup;

    //Activity??????????????????
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
        statusBarInit();
        ButterKnife.bind(this);
        if (refreshLayout() != null)
            refreshViewinit(refreshLayout());
        eventBusInit();
        registerPresenter();
        init();
        initData();
    }

    /**
     * setcontentview?????????????????????
     */
    protected void onBaseCreate() {

    }


    /**
     * ????????????????????????????????????presenter??????
     */
    protected void registerPresenter() {

    }

    /**
     * EventBus register abstract method
     * ?????? ?????????????????????????????????????????? ??????@Subscribe ?????????????????????????????????????????????????????????
     */
    protected abstract boolean eventBusRegister();


    /**
     * ?????????????????? (??????????????????????????????)
     *
     * @return
     */
    protected abstract int activityTitleLayout();

    /**
     * ???????????????????????????
     *
     * @return
     */
    protected abstract boolean ImmersionBar();

    /**
     * ??????????????? (???????????????????????? ImmersionBar )
     *
     * @return
     */
    protected abstract int StatusBarColors();

    /**
     * ???????????????
     *
     * @return
     */
    protected abstract int NavigationBarColor();

    /**
     * ?????????????????? (????????????????????????)
     * ?????????????????????????????????????????????????????????
     * ??????????????????initContentView ????????? android:layout_weight="1" ???????????????
     *
     * @return
     */
    protected abstract int activityBottomLayout();

    protected abstract View noDataLayout();

    /**
     * ??????????????????
     *
     * @return
     */
    protected abstract View refreshLayout();

    /**
     * ??????DispatchTouchEvent?????????????????????
     *
     * @return
     */
    protected abstract boolean closeDispatchTouchEvent();

    /**
     * ???????????????
     *
     * @return
     */
    protected abstract int initContentView();

    /**
     * ?????? ??????
     *
     * @return
     */
    protected abstract boolean refreshEnable();

    /**
     * ???????????? ??????
     *
     * @return
     */
    protected abstract boolean loadmoreEnable();


    /**
     * ???????????????
     */
    protected abstract void init();


    /**
     * ???????????????
     */
    protected abstract void initData();


    /**
     * ?????????findViewById??????;
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
        //??????ToastUtils ????????????
        ToastUtils.cancel();
    }


    /**
     * ??????????????????RxPermissions
     * ???????????? ???https://github.com/tbruyelle/RxPermissions
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
                // ???????????????????????????View????????????????????????EditText??????????????????????????????????????????????????????????????????
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
     * ??????View?????????;
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


        //????????????????????????
        fmlayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.module_activity_init_fm, new FrameLayout(this), false);
        fmlayout.addView(view);

        setContentView(fmlayout);
    }


    /**
     * ?????????????????????
     */
    private void refreshViewinit(View refreshView) {


        refreshLayout = (SmartRefreshLayout) refreshView;
//        refreshLayout.addView(LayoutInflater.from(getActivity()).inflate(initContentView(), null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
/*        //??????????????????
        refreshLayout.setOnRefreshListener(this);
        //????????????????????????
        refreshLayout.setOnLoadMoreListener(this);*/
        //?????????????????????????????????
        refreshLayout.setOnRefreshLoadMoreListener(this);
        //??????????????????????????????????????? Footer ????????????
//        refreshLayout.setEnableFooterFollowWhenNoMoreData(false);

        //?????????????????????????????????????????????
        refreshLayout.setDisableContentWhenRefresh(false);
        //?????????????????????????????????????????????
        refreshLayout.setDisableContentWhenLoading(false);

        //?????????????????????????????????????????????????????????
//        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);

        //????????????????????????
        refreshLayout.setEnableLoadMore(loadmoreEnable());
        //??????????????????
        refreshLayout.setEnableRefresh(refreshEnable());

        //??????????????????
        refreshLayout.setEnableOverScrollDrag(false);

        MaterialHeader materialHeader = new MaterialHeader(this);
        materialHeader.setColorSchemeResources(R.color.green_dominant_tone);
        refreshLayout.setRefreshHeader(materialHeader);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        //?????????????????????????????????????????????????????????xml??????????????????????????????????????????????????????
//        refreshLayout.setRefreshHeader(new CustomRefreshHeader(reference.get()));
//        refreshLayout.setRefreshFooter(new CustomRefreshFooter(reference.get(), "?????????????????????"));

    }


    /**
     * ???????????????
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
     * ???????????????view
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
     * ???????????????
     */
    protected void hideSkeletonScreen() {
        if (mSkeletonScreen != null) {
            mSkeletonScreen.hide();
            mSkeletonScreen = null;
        }
    }

    /**
     * ??????
     *
     * @param refreshLayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {

        resetNoMoreData();

    }


    /**
     * ????????????
     *
     * @param refreshLayout
     */
    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
    }


    /**
     * ??????contentview??????
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }


    /**
     * ??????????????????
     *
     * @param unReadMessage ??????????????????
     */
    protected synchronized void showNewMessageDialog(final ReceiveIMMessageBean unReadMessage) {
        if (CHAT_IDENTIFIER.equals(unReadMessage.getSendIdentifier()))
            return;

        RxUtil.observe(Schedulers.newThread(), Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> emitter) {

                emitter.onNext(unReadMessage);
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR), BaseActivity.this).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                BaseLogUtils.i("fmlayout count === " + fmlayout.getChildCount());

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
                                    //????????????????????????
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
     * ?????????????????????
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
     * ??????????????????????????????
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
                    htv.setText(isEmpty(hint) ? "????????????" : hint);
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
     * ??????event??????
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
     * ??????event??????
     *
     * @param object
     */
    protected void sendEvent(Object object) {
        EventBus.getDefault().post(object);
    }

    /**
     * ?????????event??????
     *
     * @param object
     */
    protected void onBaseEvent(Object object) {

        Activity activity = this;

        //????????????????????????
        if (object instanceof ReLoginBean)
            finish();

        //?????????????????????
        if (!isAcBackground && object instanceof ReceiveIMMessageBean)
            showNewMessageDialog((ReceiveIMMessageBean) object);
    }

    /**
     * Stringevent??????
     *
     * @param type
     */
    protected void onTypeEvent(Integer type) {
        //????????????activity??????
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
            BaseLogUtils.e(e);
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
        if (loadingPopup != null && loadingPopup.isShow()) {
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
     * ??????????????????
     *
     * @return
     */
    protected boolean checkClickInterval(int id) {
        //?????????view???????????????
        if (lastClickId == id) {
            //????????????????????????
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
     * ??????????????????????????????
     */
    protected void statusBarInit() {

        if (ImmersionBar()) {
            mImmersionBar = ImmersionBar.with(reference.get()).fitsSystemWindows(true).statusBarDarkFont(true, 0.2f).statusBarColor(StatusBarColors() == 0 ? R.color.grayf1 : StatusBarColors());
        } else
            mImmersionBar = ImmersionBar.with(reference.get());

        mImmersionBar.init();
    }


    /**
     * ????????????dialog
     * ??????????????????????????????onAfters????????????????????????????????????????????????dialog
     *
     * @param msg
     */
    public void showLoadingDialog(String msg) {

        try {
            if (loadingPopup == null || !loadingPopup.isShow()) {
                loadingDialogInit();
                if (!isEmpty(msg))
                    loadingPopup.setTitle(msg);

                if (!loadingPopup.isShow())
                    loadingPopup.show();
            }

        } catch (Exception e) {
            //???????????????????????????bugly
            CrashReport.postCatchedException(e);
        }
    }

    /**
     * dialog?????????
     */
    protected void loadingDialogInit() {

        loadingPopup = new XPopup.Builder(reference.get())
                .hasShadowBg(false)
                .isDestroyOnDismiss(false) //???????????????????????????????????????????????????
                .popupAnimation(PopupAnimation.NoAnimation)
                .asLoading();
//                .asLoading("", R.layout.module_dialog_custom);
    }


    /**
     * ?????? http Disposable
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
     * ??????disposable
     */
    protected void clearDisposable() {
        if (this.mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            this.mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }


}
