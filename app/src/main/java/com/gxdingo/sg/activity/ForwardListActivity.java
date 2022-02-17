package com.gxdingo.sg.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMMessageAdapter;
import com.gxdingo.sg.biz.ClientMessageContract;
import com.gxdingo.sg.dialog.ChatForwardDialog;
import com.gxdingo.sg.dialog.PostionFunctionDialog;
import com.gxdingo.sg.presenter.ClientMessagePresenter;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gxdingo.sg.utils.ImServiceUtils.resetImService;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Kikis
 * @date: 2022/1/22
 * @page:
 */
public class ForwardListActivity extends BaseMvpActivity<ClientMessageContract.ClientMessagePresenter> implements ClientMessageContract.ClientMessageListener, OnItemClickListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    private IMMessageAdapter imMessageAdapter;

    private ReceiveIMMessageBean receiveIMMessageBean;

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_custom_title;
    }

    @Override
    protected boolean ImmersionBar() {
        return true;
    }

    @Override
    protected int StatusBarColors() {
        return R.color.white;
    }

    @Override
    protected int NavigationBarColor() {
        return 0;
    }

    @Override
    protected int activityBottomLayout() {
        return 0;
    }

    @Override
    protected View noDataLayout() {
        return nodata_layout;
    }

    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_include_refresh;
    }

    @Override
    protected boolean refreshEnable() {
        return true;
    }

    @Override
    protected boolean loadmoreEnable() {
        return true;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        if (UserInfoUtils.getInstance().isLogin())
            getP().getSubscribesMessage(true);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        if (UserInfoUtils.getInstance().isLogin())
            getP().getSubscribesMessage(false);
    }

    @Override
    protected void init() {
        receiveIMMessageBean = (ReceiveIMMessageBean) getIntent().getSerializableExtra(Constant.SERIALIZABLE + 0);
        imMessageAdapter = new IMMessageAdapter(true);
        recyclerView.setAdapter(imMessageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        imMessageAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void initData() {
        title_layout.setTitleText("发送给");
        getP().getSubscribesMessage(true);
    }

    @Override
    protected void onBaseCreate() {
        super.onBaseCreate();
    }

    @OnClick({})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

        }
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

        showForwardDialog(imMessageAdapter.getData().get(position));
    }


    private void showForwardDialog(SubscribesListBean.SubscribesMessage subscribesMessage) {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoDismiss(true)
                .hasShadowBg(true)
                .asCustom(new ChatForwardDialog(reference.get(), subscribesMessage, receiveIMMessageBean, v -> {
                    if (v.getId() == R.id.confirm_tv) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("id", receiveIMMessageBean.getDataByType().getId());

                        getP().sendMessage(subscribesMessage.getShareUuid(), 30, "", 0, map);

                    }
                }).show());
    }


    @Override
    public void onSucceed(int type) {
        super.onSucceed(type);
        finish();
    }

    @Override
    public void onSubscribes(boolean refresh, SubscribesListBean subscribesListBean) {
        if (subscribesListBean != null && subscribesListBean.getList() != null) {

            //取出本地存储的websocket url
            String webSocketUrl = SPUtils.getInstance().getString(WEB_SOCKET_URL);
            /*
             * 本地保存的webSocketUrl是否服务器的一样，不一样可能url发生改变，
             * 需要将BaseWebSocket设置null,重新连接（前提是IMMessageReceivingService在运行）
             */
            if (!webSocketUrl.equals(subscribesListBean.getWebsocketUrl())) {
//                EventBus.getDefault().post(100999);//发送重置码
                resetImService();
            }
            //保存web socket接入url
            SPUtils.getInstance().put(WEB_SOCKET_URL, subscribesListBean.getWebsocketUrl());

            startImService();
//            getContext().startService(new Intent(getContext(), IMMessageReceivingService.class));

            if (refresh) {
                imMessageAdapter.setList(subscribesListBean.getList());
            } else {
                imMessageAdapter.addData(subscribesListBean.getList());
            }
        }
    }

    @Override
    public void clearMessageUnreadItem(String id) {

    }

    @Override
    public void setUnreadMsgNum(Integer data) {

    }

    @Override
    public void onSetTopResult(int pos) {

    }

    @Override
    public void onSubDel(int position) {

    }

    @Override
    protected ClientMessageContract.ClientMessagePresenter createPresenter() {
        return new ClientMessagePresenter();
    }
}
