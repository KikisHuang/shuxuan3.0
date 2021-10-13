package com.kikis.commnlibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.view.CustomRefreshFooter;
import com.kikis.commnlibrary.view.CustomRefreshHeader;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;

/**
 * Dialog通用样式
 */
public abstract class BaseFragmentDialog extends DialogFragment implements OnRefreshLoadMoreListener {

    public WeakReference<Context> reference;

    protected View view;

    private Unbinder unbinder;

    protected SmartRefreshLayout refreshLayout;

    protected Bundle args;

    //最后点击的时间
    private long lastClickTime = 0;
    //上次点击的viewId
    private int lastClickId = 0;
    //点击时间间隔
    private int clickInterval = 300;


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
     * EventBus register abstract method
     * 注： 开启后必须要有一个方法前添加 注解@Subscribe （回调方法，方法名自定），否则会报错；
     */
    protected abstract boolean eventBusRegister();

    /**
     * 设置dialog布局
     *
     * @return
     */
    public abstract int initContentView();

    /**
     * 高
     *
     * @return
     */
    public abstract int DialogHeight();

    /**
     * 宽
     *
     * @return
     */
    public abstract int DialogWidth();

    /**
     * 动画
     *
     * @return
     */
    public abstract int AnimStyle();

    /**
     * 点击外部取消
     *
     * @return
     */
    public abstract boolean OutCancel();

    /**
     * 显示位置
     * Gravity.BOTTOM Gravity.Center
     *
     * @return
     */
    public abstract int ShowPosition();

    /**
     * 背景昏暗
     *
     * @return
     */
    public abstract float DimAmount();

    /**
     * y偏移量
     *
     * @return
     */
    public abstract int ParamsY();


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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //如果Activity不存在，不弹窗
        if (getActivity().isFinishing()) {
            dismissAllowingStateLoss();
        }
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.ActionSheetDialogAnimation);
//        reference = ((InitActivity) getActivity()).reference;
        reference = new WeakReference<Context>(getActivity());
        ViewInit(inflater, container);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerPresenter();
        init();
        initData();
        eventBusInit();
    }


    /**
     * 在控件初始化之前先初始化presenter组件
     */
    protected void registerPresenter() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        args = getArguments();
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    private void ViewInit(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(initContentView(), container);

    }

    /**
     * 刷新控件初始化
     */
    protected void refreshViewinit(View refreshView) {
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


        MaterialHeader materialHeader = new MaterialHeader(reference.get());
        materialHeader.setColorSchemeColors(getc(R.color.pink_dominant_tone));
        refreshLayout.setRefreshHeader(materialHeader);
        refreshLayout.setRefreshFooter(new ClassicsFooter(reference.get()));
    }

    private void eventBusInit() {
        //Register
        EventBus.getDefault().register(this);
    }

    private void initParams() {
        Window window = getDialog().getWindow();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();

            if (DimAmount() > 0) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                //改变背景阴影
                params.dimAmount = DimAmount();
            }

            if (ParamsY()>0)
                params.y=ParamsY();

            params.alpha = 1.0f;

            params.gravity = ShowPosition();

            if (DialogWidth() == 0)
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
            else {
                params.width = DialogWidth();
            }

            //设置dialog高度
            if (DialogHeight() == 0) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = DialogHeight();
            }

            //设置dialog动画
            if (AnimStyle() != 0) {
                window.setWindowAnimations(AnimStyle());
            }

            window.setAttributes(params);
        }
        setCancelable(OutCancel());
        getDialog().setCanceledOnTouchOutside(OutCancel());

    }

    public BaseFragmentDialog show(FragmentManager manager) {
        super.show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    /**
     * 视图销毁的时候进行的操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//清除标记

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {

    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseFragmentEvent(Object object) {
        if (eventBusRegister() && object != null)
            if (object instanceof String)
                onStringEvent((String) object);
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

    protected void onStringEvent(String str) {

    }
}