package com.gxdingo.sg.fragment.client;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.adapter.StoreHomeIMMessageAdapter;
import com.gxdingo.sg.bean.ExitChatEvent;
import com.gxdingo.sg.bean.SubscribesBean;
import com.gxdingo.sg.biz.ClientMessageContract;
import com.gxdingo.sg.biz.ClientMineContract;
import com.gxdingo.sg.presenter.ClientMessagePresenter;
import com.gxdingo.sg.presenter.ClientMinePresenter;
import com.gxdingo.sg.service.IMMessageReceivingService;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.gxdingo.sg.utils.LocalConstant.CLIENT_LOGIN_SUCCEED;
import static com.kikis.commnlibrary.utils.CommonUtils.getd;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMessageFragment extends BaseMvpFragment<ClientMessageContract.ClientMessagePresenter> implements ClientMessageContract.ClientMessageListener, OnItemClickListener {

    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;

    private StoreHomeIMMessageAdapter imMessageAdapter;
    @Override
    protected ClientMessageContract.ClientMessagePresenter createPresenter() {
        return new ClientMessagePresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return false;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_custom_title;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_include_refresh;
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
        title_layout.setTitleText(gets(R.string.message));
        title_layout.setBackVisible(false);
        imMessageAdapter = new StoreHomeIMMessageAdapter();
        recyclerView.setAdapter(imMessageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        imMessageAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        if (UserInfoUtils.getInstance().isLogin())
            getP().getSubscribesMessage(true);
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == CLIENT_LOGIN_SUCCEED){
            getP().refreshList();
            getP().getSubscribesMessage(true);
        }

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
                EventBus.getDefault().post(100999);//发送重置码
            }
            //保存web socket接入url
            SPUtils.getInstance().put(WEB_SOCKET_URL, subscribesListBean.getWebsocketUrl());
            //启动IM消息接收服务
            getContext().startService(new Intent(getContext(), IMMessageReceivingService.class));

            if (refresh) {
                imMessageAdapter.setList(subscribesListBean.getList());
            } else {
                imMessageAdapter.addData(subscribesListBean.getList());
            }
        }

    }

    @Override
    public void clearMessageUnreadItem(String id) {
        //清除item未读消息
        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < imMessageAdapter.getData().size(); i++) {
                SubscribesListBean.SubscribesMessage data = imMessageAdapter.getData().get(i);

                if (data.getShareUuid().equals(id)) {
                    data.setUnreadNum(0);
                    e.onNext(i);
                }
            }
            e.onComplete();
        }), (BaseActivity) reference.get()).subscribe(o -> {
            int pos = (int) o;
            imMessageAdapter.notifyItemChanged(pos);
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (UserInfoUtils.getInstance().isLogin())
            getP().refreshList();
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        SubscribesListBean.SubscribesMessage item = (SubscribesListBean.SubscribesMessage) adapter.getItem(position);
        goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{item.getShareUuid()}));
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean receiveIMMessageBean = (ReceiveIMMessageBean) object;
            if (receiveIMMessageBean != null && !TextUtils.isEmpty(receiveIMMessageBean.getSendIdentifier())) {
                //不用做处理，直接刷新IM订阅列表即可（消息发送者的那条订阅消息会显示在第一条）
                getP().refreshList();
            }
        }
        //聊天页面退出事件，因为如果在当前聊天页面，未读消息数需要客户端手动清除？
        if (object instanceof ExitChatEvent) {
            ExitChatEvent exitChatEvent = (ExitChatEvent) object;
            getP().clearUnreadMsg(exitChatEvent.id);

        }
    }
}
