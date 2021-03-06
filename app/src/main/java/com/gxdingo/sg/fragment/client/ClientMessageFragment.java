package com.gxdingo.sg.fragment.client;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.activity.ClientActivity;
import com.gxdingo.sg.activity.StoreHomeSearchActivity;
import com.gxdingo.sg.adapter.IMMessageAdapter;
import com.gxdingo.sg.bean.ExitChatEvent;
import com.gxdingo.sg.biz.ClientMessageContract;
import com.gxdingo.sg.dialog.ChatListFunctionDialog;
import com.gxdingo.sg.presenter.ClientMessagePresenter;
import com.kikis.commnlibrary.utils.MessageCountManager;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.kikis.commnlibrary.bean.SubscribesListBean;
import com.kikis.commnlibrary.fragment.BaseMvpFragment;
import com.kikis.commnlibrary.utils.RecycleViewUtils;
import com.kikis.commnlibrary.utils.RxUtil;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.gxdingo.sg.utils.ImServiceUtils.resetImService;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.BACK_TOP_MESSAGE_LIST;
import static com.gxdingo.sg.utils.LocalConstant.LOGIN_SUCCEED;
import static com.gxdingo.sg.utils.LocalConstant.NOTIFY_MSG_LIST_ADAPTER;
import static com.kikis.commnlibrary.utils.Constant.WEB_SOCKET_URL;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.RecycleViewUtils.forceStopRecyclerViewScroll;

/**
 * @author: Weaving
 * @date: 2021/10/13
 * @page:
 */
public class ClientMessageFragment extends BaseMvpFragment<ClientMessageContract.ClientMessagePresenter> implements OnItemLongClickListener, ClientMessageContract.ClientMessageListener, OnItemClickListener {


    @BindView(R.id.smartrefreshlayout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.unread_msg_num)
    public TextView unread_msg_num;

    @BindView(R.id.nodata_layout)
    public View nodata_layout;


    private IMMessageAdapter imMessageAdapter;

    @Override
    protected ClientMessageContract.ClientMessagePresenter createPresenter() {
        return new ClientMessagePresenter();
    }


    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return 0;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_fragment_cilent_msg;
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
        imMessageAdapter = new IMMessageAdapter();
        recyclerView.setAdapter(imMessageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(reference.get()));
        imMessageAdapter.setOnItemClickListener(this);
        imMessageAdapter.setOnItemLongClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void lazyInit() {
        super.lazyInit();
        if (UserInfoUtils.getInstance().isLogin()) {
            getP().getUnreadMessageNum();
            if (isFirstLoad) {
                isFirstLoad = !isFirstLoad;
                getP().getSubscribesMessage(true);
            } else {

                getP().refreshList();
            }
        }
    }


    @OnClick({R.id.search_img})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;

        switch (v.getId()) {
            case R.id.search_img:
                startActivity(new Intent(reference.get(), StoreHomeSearchActivity.class));
                break;
        }

    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LOGIN_SUCCEED) {
            forceStopRecyclerViewScroll(recyclerView);
            //????????????
            RecycleViewUtils.MoveToPosition((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView, 0);

            getP().getSubscribesMessage(true);

        } else if (type == NOTIFY_MSG_LIST_ADAPTER)
            imMessageAdapter.notifyDataSetChanged();
        else if (type == BACK_TOP_MESSAGE_LIST) {
            forceStopRecyclerViewScroll(recyclerView);
            //????????????
            RecycleViewUtils.MoveToPosition((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView, 0);
        }
    }

    @Override
    public void onSubscribes(boolean refresh, SubscribesListBean subscribesListBean) {
        if (subscribesListBean != null && subscribesListBean.getList() != null) {

            //?????????????????????websocket url
            String webSocketUrl = SPUtils.getInstance().getString(WEB_SOCKET_URL);
            /*
             * ???????????????webSocketUrl??????????????????????????????????????????url???????????????
             * ?????????BaseWebSocket??????null,????????????????????????IMMessageReceivingService????????????
             */
            if (!webSocketUrl.equals(subscribesListBean.getWebsocketUrl())) {
//                EventBus.getDefault().post(100999);//???????????????
                resetImService();
            }
            //??????web socket??????url
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
        //??????item????????????
        RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {
            for (int i = 0; i < imMessageAdapter.getData().size(); i++) {
                SubscribesListBean.SubscribesMessage data = imMessageAdapter.getData().get(i);

                if (data.getShareUuid().equals(id)) {
                    MessageCountManager.getInstance().reduceUnreadMessageNum(data.getUnreadNum());
                    data.setUnreadNum(0);
                    e.onNext(i);
                }
            }
            e.onComplete();
        }), (BaseActivity) reference.get()).subscribe(o -> {
            int pos = (int) o;

            if (ClientActivity.getInstance() != null)
                ClientActivity.getInstance().setUnreadMsgNum(MessageCountManager.getInstance().getUnreadMessageNum());

            imMessageAdapter.notifyItemChanged(pos);
        });
    }

    @Override
    public void setUnreadMsgNum(Integer data) {
        if (data != null && data > 0)
            unread_msg_num.setText("(" + data + ")");
        else
            unread_msg_num.setVisibility(View.GONE);
    }

    @Override
    public void onSetTopResult(int pos, int sort) {
        if (sort <= imMessageAdapter.getData().size() - 1) {

            if (sort > 0) {
                //??????????????????
                imMessageAdapter.getData().get(pos).sort = 1;
                imMessageAdapter.getData().add(0, imMessageAdapter.getData().remove(pos));
                imMessageAdapter.notifyDataSetChanged();
            } else
                //????????????????????????
                getP().refreshList();
        }
    }

    @Override
    public void onSubDel(int position) {
        if (imMessageAdapter != null) {
            imMessageAdapter.remove(imMessageAdapter.getData().get(position));
            imMessageAdapter.notifyItemChanged(position);
        }
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        SubscribesListBean.SubscribesMessage item = (SubscribesListBean.SubscribesMessage) adapter.getItem(position);
        goToPagePutSerializable(reference.get(), ChatActivity.class, getIntentEntityMap(new Object[]{item.getShareUuid(), item.getSendUserRole()}));
        getP().clearUnreadMsg(item.getShareUuid());
        ClientActivity.getInstance().setUnreadMsgNum(MessageCountManager.getInstance().reduceUnreadMessageNum(item.getUnreadNum()));
    }

    @Override
    protected void onBaseEvent(Object object) {
        super.onBaseEvent(object);
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean receiveIMMessageBean = (ReceiveIMMessageBean) object;
            if (receiveIMMessageBean != null && !TextUtils.isEmpty(receiveIMMessageBean.getSendIdentifier())) {
                //??????????????????????????????IM?????????????????????????????????????????????????????????????????????????????????
                getP().refreshList();
            }
        }
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (object instanceof ExitChatEvent) {
            ExitChatEvent exitChatEvent = (ExitChatEvent) object;
            getP().clearUnreadMsg(exitChatEvent.id);
        }
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                .autoDismiss(true)
                .hasShadowBg(true)
                .asCustom(new ChatListFunctionDialog(reference.get(), v -> {

                    if (v.getId() == R.id.del_ll)
                        getP().listChatDel(imMessageAdapter.getData().get(position).id, position);
                    else if (v.getId() == R.id.settop_ll)
                        //??????
                        getP().setTop(imMessageAdapter.getData().get(position).id, imMessageAdapter.getData().get(position).sort > 0 ? 0 : 1, position);
                }, imMessageAdapter.getData().get(position).sort > 0 ? true : false).show());
        return false;
    }
}
