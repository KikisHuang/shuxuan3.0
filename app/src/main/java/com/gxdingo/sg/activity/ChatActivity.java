
package com.gxdingo.sg.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.ChatAdapter;
import com.gxdingo.sg.utils.ImMessageUtils;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.FunctionsItem;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.NormalBean;
import com.gxdingo.sg.bean.PayBean;
import com.gxdingo.sg.biz.ProgressListener;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.bean.SendMessageBean;
import com.gxdingo.sg.biz.ChatClickListener;
import com.gxdingo.sg.biz.IMChatContract;
import com.gxdingo.sg.dialog.IMSelectSendAddressPopupView;
import com.gxdingo.sg.dialog.IMSelectTransferAccountsWayPopupView;
import com.gxdingo.sg.http.HttpClient;
import com.gxdingo.sg.presenter.IMChatPresenter;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.emotion.EmotiomComplateFragment;
import com.gxdingo.sg.utils.emotion.EmotionMainFragment;
import com.gxdingo.sg.view.MyBaseSubscriber;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.utils.AnimationUtil;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.ApiResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.view.MotionEvent.ACTION_DOWN;
import static cc.shinichi.library.ImagePreview.LoadStrategy.NetworkAuto;
import static com.blankj.utilcode.util.FileUtils.createOrExistsDir;
import static com.blankj.utilcode.util.KeyboardUtils.registerSoftInputChangedListener;
import static com.blankj.utilcode.util.KeyboardUtils.unregisterSoftInputChangedListener;
import static com.blankj.utilcode.util.PermissionUtils.isGranted;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.gxdingo.sg.adapter.IMOtherFunctionsAdapter.TYPE_ROLE;
import static com.gxdingo.sg.adapter.IMOtherFunctionsAdapter.TYPE_STORE;
import static com.gxdingo.sg.adapter.IMOtherFunctionsAdapter.TYPE_USER;
import static com.gxdingo.sg.http.Api.getUpLoadImage;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.EMOTION_LAYOUT_IS_SHOWING;
import static com.gxdingo.sg.utils.emotion.EmotionMainFragment.CHAT_ID;
import static com.gxdingo.sg.utils.emotion.EmotionMainFragment.ROLE;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;
import static com.kikis.commnlibrary.utils.Constant.KEY;
import static com.kikis.commnlibrary.utils.GsonUtil.getObjMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getImagePreviewInstance;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.RecycleViewUtils.MoveToPositionTop;


/**
 * @author: Kikis
 * @date: 2021/10/10
 * @page:聊天页面
 */

public class ChatActivity extends BaseMvpActivity<IMChatContract.IMChatPresenter> implements IMChatContract.IMChatListener, KeyboardUtils.OnSoftInputChangedListener, View.OnTouchListener, ChatClickListener {


    @BindView(R.id.title_layout)
    public TemplateTitle title_layout;

    @BindView(R.id.refresh_layout)
    public SmartRefreshLayout smartrefreshlayout;

    @BindView(R.id.unread_count_tv)
    public TextView unread_count_tv;

    @BindView(R.id.recyclerView)
    public RecyclerView recycleView;

    @BindView(R.id.tv_other_side_address)
    public TextView tvOtherSideAddress;

    @BindView(R.id.tv_other_side_phone)
    public TextView tv_other_side_phone;

    @BindView(R.id.tv_other_side_nick_name)
    public TextView tv_other_side_nick_name;

    @BindView(R.id.cl_no_address_layout)
    public ConstraintLayout clNoAddressLayout;

    @BindView(R.id.tv_no_address)
    public TextView tv_no_address;

    @BindView(R.id.cl_other_side_address_layout)
    public ConstraintLayout cl_other_side_address_layout;

    @BindView(R.id.rl_cancel_recording_area)
    public RelativeLayout rlCancelRecordingArea;

    @BindView(R.id.cl_voice_recording_layout)
    public ConstraintLayout clVoiceRecordingLayout;

    @BindView(R.id.ll_navigation)
    public LinearLayout ll_navigation;

    @BindView(R.id.store_ll)
    public FrameLayout store_ll;

    @BindView(R.id.ll_location)
    public ImageView ll_location;

    @BindView(R.id.iv_voice_recording_status)
    public ImageView ivVoiceRecordingStatus;

    @BindView(R.id.recorded_voice_scrolling)
    public ImageView recordedVoiceScrolling;

    @BindView(R.id.btn_long_press_to_speak)
    public TextView btn_long_press_to_speak;

    @BindView(R.id.right_arrow)
    public TextView right_arrow;

    @BindView(R.id.cv_other_side_address)
    public CardView cv_other_side_address;

    private AnimationDrawable mRecordedVoiceAnimation;//录制语音动画

    //最早的一条消息时间
    private String lastMsgTime = "";

    //发布者与订阅者的共享唯一id，使用该值查出聊天记录
    private String mShareUuid = "";

    //对方角色。10=联系用户 11=联系商家 12=联系客服
    private int otherRole = 0;

    //对方的id。otherRole = 11传店铺id；otherRole = 12 默认传0
    private int otherId = 0;

    //聊天消息数据集
    private LinkedList<ReceiveIMMessageBean> mChatDatas;

    //subscribebean 类
    private IMChatHistoryListBean mMessageDetails;

    private ChatAdapter mAdapter;

    private EmotionMainFragment emotionMainFragment;

    //未读消息数
    private int unreadCount = 0;

    private AddressBean mAddress;//收货地址;

    private AddressBean mDefaultAddress;//默认收货地址;

    private long clicktimeDValue = 0; //按下录制的时间差
    //取消发送
    private boolean mCancel = false;

    @Override
    protected boolean eventBusRegister() {
        return true;
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
        return null;
    }

    @Override
    protected View refreshLayout() {
        return smartrefreshlayout;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return true;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_chat;
    }

    @Override
    protected boolean refreshEnable() {
        return true;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    protected void init() {

        title_layout.setBackgroundColor(getc(R.color.white));

        mShareUuid = getIntent().getStringExtra(Constant.SERIALIZABLE + 0);

        //用户首页进入时，没有ShareUuid，需要传入otherRole、otherId
        otherRole = getIntent().getIntExtra(Constant.SERIALIZABLE + 1, 10);
        otherId = getIntent().getIntExtra(Constant.SERIALIZABLE + 2, 0);

        if (otherRole != 12)
            title_layout.setMoreImg(R.drawable.module_svg_more_8935);
        else {
            cv_other_side_address.setVisibility(View.GONE);
            FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) recycleView.getLayoutParams();
            flp.topMargin = 0;
        }
        initEmotionMainFragment();

        //软键盘弹出监听
        registerSoftInputChangedListener(this, this);
        recycleInit();
    }

    private void recycleInit() {

        mChatDatas = new LinkedList<>();

        recycleView.setLayoutManager(new LinearLayoutManager(reference.get()));

        recycleView.setOnTouchListener(this);

        ((SimpleItemAnimator) recycleView.getItemAnimator()).setSupportsChangeAnimations(false);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //如果滑到了底部并有未读消息，则清空
                if (!recycleView.canScrollVertically(1) && unreadCount > 0)
                    showNewMessage();
            }
        });
    }

    @Override
    protected void initData() {
        getP().getCacheAddress();
        loadMore();
    }


    @Override
    protected void onStart() {
        super.onStart();

        //如果页面恢复回来，消息im服务不存在，进行刷新页面操作
        if (!ImMessageUtils.getInstance().isRunning())
            getP().refreshHistoryList(mShareUuid, otherId, otherRole);

        if (UserInfoUtils.getInstance().isLogin())
            //im服务启动检测
            startImService(reference.get());


    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        loadMore();
    }

    /**
     * 加载聊天消息
     */
    private void loadMore() {
        getP().getChatHistoryList(mShareUuid, otherId, otherRole);//获取聊天记录
    }


    private void initEmotionMainFragment() {

        //设置语音录制时动画播放图片素材
        recordedVoiceScrolling.setBackgroundResource(R.drawable.module_im_recorded_voice_scrolling);
        mRecordedVoiceAnimation = (AnimationDrawable) recordedVoiceScrolling.getBackground();

        Bundle bundle = new Bundle();
        bundle.putString(KEY, ChatActivity.class.toString() + System.currentTimeMillis());
        bundle.putString(CHAT_ID, mShareUuid);
        bundle.putInt(ROLE, otherRole);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotiomComplateFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(refreshLayout);

        emotionMainFragment.setonTouchListener((v, event) -> {

            boolean locationPermiss = isGranted(RECORD_AUDIO);
            //没有权限拦截事件
            if (!locationPermiss) {
                getP().checkRecordPermissions(getRxPermissions());
                return true;
            }
            clVoiceRecordingLayout.setVisibility(View.VISIBLE);

            if (event.getAction() == ACTION_DOWN) {
                clicktimeDValue = System.currentTimeMillis();

                getP().startRecorder();

                recordedVoiceScrolling.post(new Runnable() {
                    @Override
                    public void run() {
                        mRecordedVoiceAnimation.start();//启动动画
                    }
                });

                btn_long_press_to_speak.setText("松开发送");
                btn_long_press_to_speak.setTextColor(Color.parseColor("#ffffff"));
                btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#599252"));
            }

            return false;
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();


    }


    @OnClick({R.id.cl_no_address_layout, R.id.cl_other_side_address_layout, R.id.unread_count_tv, R.id.img_back, R.id.img_more})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

            case R.id.cl_no_address_layout:
                //联系商家跳转选择地址列表
                if (otherRole == 11)
                    goToPagePutSerializable(reference.get(), ClientAddressListActivity.class, getIntentEntityMap(new Object[]{2}));
                break;
            case R.id.cl_other_side_address_layout:


                if (otherRole == 11) {
                    if (UserInfoUtils.getInstance().getUserInfo().getRole() == 10)
                        //用户联系商家跳转选择地址列表
                        goToPagePutSerializable(reference.get(), ClientAddressListActivity.class, getIntentEntityMap(new Object[]{2}));
                    else {
                        //商家联系商家显示选择弹窗
                        new XPopup.Builder(reference.get())
                                .isDarkTheme(false)
                                .isDestroyOnDismiss(true)
                                .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.select_receiving_address), gets(R.string.go_navigation)).setItemClickListener((itemv, pos) -> {
                                    if (pos == 0)
                                        //选择地址
                                        goToPagePutSerializable(reference.get(), ClientAddressListActivity.class, getIntentEntityMap(new Object[]{2}));
                                    else if (pos == 1)
                                        showNavigationDialog();
                                })).show();
                    }


                } else if (otherRole == 10)
                    showNavigationDialog();


                break;
            case R.id.unread_count_tv:
                showNewMessage();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.img_more:
                //客服没有右上角的更多功能
                if (otherRole == 12)
                    return;
                //联系商家跳转商商家详情
                if (otherRole == 11) {
                    if (mMessageDetails != null && mMessageDetails.getOtherAvatarInfo() != null)
                        goToPagePutSerializable(reference.get(), ClientStoreDetailsActivity.class, getIntentEntityMap(new Object[]{(int) mMessageDetails.getOtherAvatarInfo().getId()}));
                } else {
                    //跳转举报、投诉用户页面
                    if (mMessageDetails != null && mMessageDetails.getOtherAvatarInfo() != null)
                        goToPagePutSerializable(reference.get(), IMComplaintActivity.class, getIntentEntityMap(new Object[]{mMessageDetails.getOtherAvatarInfo().getSendIdentifier(), mMessageDetails.getOtherAvatarInfo().getSendRole(), mShareUuid}));
                }

                break;
          /*  case R.id.photo_album_img:
                PhotoUtils.Photo(reference.get(), this);
                break;
            case R.id.camera_img:
                PhotoUtils.TakePhoto(reference.get(), this);
                break;*/
        }
    }

    /**
     * 第三方导航选择弹窗
     */
    private void showNavigationDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true)
                .isDarkTheme(false)
                .asCustom(new BaseActionSheetPopupView(reference.get()).addSheetItem(gets(R.string.gaode_map), gets(R.string.baidu_map), gets(R.string.tencent_map)).setItemClickListener((itemv, pos) -> {
                    getP().goOutSideNavigation(pos, mAddress);
                })).show();
    }


    @Override
    protected void onBaseEvent(Object object) {
/*
        if (object instanceof NewMessage) {
            NewMessage unReadMessage = ((NewMessage) object);

            if (unReadMessage != null && unReadMessage.getSubscribeId() == mShareUuid)
                receiveNewMsg(unReadMessage);

        }*/
        if (object instanceof AddressBean) {
            AddressBean addressBean = (AddressBean) object;
            if (addressBean.selectType == 2) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", addressBean.getId());
                mAddress = addressBean;
                getP().sendMessage(mShareUuid, 30, "", 0, map);
            }
        }

        //发送消息事件
        if (object instanceof SendMessageBean) {
            SendMessageBean smb = (SendMessageBean) object;

            getP().sendMessage(mShareUuid, 0, smb.content, 0, null);
        }

        /**
         * 从IMMessageReceivingService接收到的IM消息
         */
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean receiveIMMessageBean = (ReceiveIMMessageBean) object;
            if (receiveIMMessageBean != null && !TextUtils.isEmpty(receiveIMMessageBean.getSendIdentifier())) {
                if (mMessageDetails.getOtherAvatarInfo() != null) {

                    //判断消息发送者是否跟当前聊天
                    if (mMessageDetails.getOtherAvatarInfo().getSendIdentifier().equals(receiveIMMessageBean.getSendIdentifier())) {

                        if (receiveIMMessageBean.getType() != 30)
                            //一样则通过该方法显示消息
                            receiveNewMsg(receiveIMMessageBean);
                        else
                            setAddressInfo(receiveIMMessageBean.getMsgAddress());
                    }

                    //自己发送的转账消息
                    if (UserInfoUtils.getInstance().getIdentifier().equals(receiveIMMessageBean.getSendIdentifier()) && receiveIMMessageBean.getType() == 20) {
                        receiveNewMsg(receiveIMMessageBean);
                    }

                }
            }
        }

        if (object instanceof PayBean.TransferAccountsDTO) {
/*            PayBean.TransferAccountsDTO data = (PayBean.TransferAccountsDTO) object;
            Map<String, Object> map = new HashMap<>();
            map.put("id", data.getId());
            //转账成功
            getP().sendMessage(mShareUuid, 21, "", 0,map );*/

        }

        /**
         * 其他功能
         */
        if (object instanceof FunctionsItem) {
            FunctionsItem functionsItem = (FunctionsItem) object;
            /**
             * 联系商家
             */
            if (functionsItem.type == TYPE_STORE) {
                //相册
                if (functionsItem.position == 0) {
                    getP().photoSourceClick(0);
                }
                //拍照
                else if (functionsItem.position == 1) {
                    getP().photoSourceClick(1);
                }
                //地址
                else if (functionsItem.position == 2) {
                    getP().getAddressList();
                }
                //转账
                else if (functionsItem.position == 3) {
                    showSelectTransferAccountsWayDialog();
                }
                //电话
                else if (functionsItem.position == 4) {
                    String phone = "";

                    //如果地址不为空
                    if (mAddress != null && !isEmpty(mAddress.getMobile())) {
                        //两个商家聊天的情况下如果地址是我的，属于购买方，打电话取对方电话，如果不是我的，我属于商家，取地址的电话
                        if (mAddress.identifier.equals(UserInfoUtils.getInstance().getIdentifier()))
                            phone = mMessageDetails.getOtherAvatarInfo().getMobile();
                        else
                            phone = mAddress.getMobile();
                    } else
                        phone = mMessageDetails.getOtherAvatarInfo().getMobile();

                    callPhone(phone);
                }
                //投诉
                else if (functionsItem.position == 5) {
                    if (mMessageDetails != null)
                        goToPagePutSerializable(reference.get(), IMComplaintActivity.class, getIntentEntityMap(new Object[]{mMessageDetails.getOtherAvatarInfo().getSendIdentifier(), mMessageDetails.getOtherAvatarInfo().getSendRole(), mShareUuid}));
//                    goToPagePutSerializable(reference.get(), ArticleListActivity.class, getIntentEntityMap(new Object[]{0, "shuxuanyonghutousu"}));

                }
            }
            /**
             * 联系用户
             */
            else if (functionsItem.type == TYPE_USER) {

                if (functionsItem.position == 0) {
                    //相册
                    getP().photoSourceClick(0);
                } else if (functionsItem.position == 1) {
                    //拍照
                    getP().photoSourceClick(1);
                } else if (functionsItem.position == 2) {
                    if (mAddress != null && !isEmpty(mAddress.getMobile()))
                        callPhone(mAddress.getMobile());
                    else
                        onMessage("没有获取到电话号码");

                } else if (functionsItem.position == 3) {
                    //转账
                    showSelectTransferAccountsWayDialog();
                }
            } else if (functionsItem.type == TYPE_ROLE) {
                /**
                 * 联系客服
                 */
                if (functionsItem.position == 0) {
                    //相册
                    getP().photoSourceClick(0);
                } else if (functionsItem.position == 1) {
                    //拍照
                    getP().photoSourceClick(1);
                }
            }
        }

    }

    /**
     * 打电话
     *
     * @param mobile
     */
    private void callPhone(String mobile) {
        if (!isEmpty(mobile)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        } else
            onMessage("没有获取到电话号码");
    }

    /**
     * 设置地址信息
     */
    private void setAddressInfo(AddressBean addressInfo) {

        cl_other_side_address_layout.setVisibility(View.VISIBLE);
        clNoAddressLayout.setVisibility(View.GONE);

        if (addressInfo != null) {
            mAddress = addressInfo;

            if (!isEmpty(addressInfo.getStreet()))
                tvOtherSideAddress.setText(addressInfo.getStreet() + " " + addressInfo.getDoorplate());

            if (!isEmpty(addressInfo.getMobile()))
                tv_other_side_phone.setText(addressInfo.getMobile());

            if (!isEmpty(addressInfo.getName()))
                tv_other_side_nick_name.setText(addressInfo.getName());
        }

    }


    /**
     * 显示选择转账弹窗
     */
    private void showSelectTransferAccountsWayDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(new IMSelectTransferAccountsWayPopupView(this, new IMSelectTransferAccountsWayPopupView.OnTransferAccountsWayListener() {
                    @Override
                    public void way(int type) {
                        if (mMessageDetails != null && mMessageDetails.getOtherAvatarInfo() != null)
                            goToPagePutSerializable(reference.get(), IMTransferAccountsPayActivity.class, getIntentEntityMap(new Object[]{mShareUuid, type, mMessageDetails.getOtherAvatarInfo()}));
                    }
                }).show());
    }

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == EMOTION_LAYOUT_IS_SHOWING)
            moveTo(mChatDatas.size() - 1);
    }


    /**
     * 消息数据添加逻辑
     *
     * @param chat_list
     */
    private void addData(List<ReceiveIMMessageBean> chat_list) {

        if (chat_list.size() > 0) {

            RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {


                int pos = -1;
                if (mChatDatas.size() <= 0)
                    mChatDatas.addAll(0, chat_list);
                else {
                    int oldSize = mChatDatas.size();
                    //消息去重
                    for (int j = chat_list.size() - 1; j >= 0; j--) {
                        boolean isNewMsg = true;

                        for (int i = 0; i < mChatDatas.size(); i++) {
                            if (mChatDatas.get(i).getId() == chat_list.get(j).getId())
                                isNewMsg = false;
                        }
                        if (isNewMsg)
                            mChatDatas.add(0, chat_list.get(j));
                    }

                    if (oldSize != mChatDatas.size())
                        pos = chat_list.size() > 0 ? chat_list.size() - 1 : chat_list.size();

                }

                /*  for (int i = chat_list.size(); i <= 0; i--) {
                    mChatDatas.addFirst(chat_list.get(i));
                }*/

                e.onNext(pos);
                e.onComplete();
            }), this).subscribe(o -> {
                int p = (int) o;

                if (p != -1) {
                    recycleView.scrollToPosition(p);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) recycleView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(p, 0);
                }

                mAdapter.notifyDataSetChanged();

            });
        }
    }

    /**
     * 创建新消息
     *
     * @param receiveIMMessageBean
     */

    private void createNewMsg(ReceiveIMMessageBean receiveIMMessageBean) {
        //地址类型
        if (receiveIMMessageBean.getType() == 30)
            setAddressInfo(receiveIMMessageBean.getMsgAddress());

        mChatDatas.add(receiveIMMessageBean);
        mAdapter.notifyDataSetChanged();

        moveTo(mChatDatas.size() - 1);
    }

    /**
     * 接收新消息
     *
     * @param receiveIMMessageBean
     */

    private void receiveNewMsg(ReceiveIMMessageBean receiveIMMessageBean) {

        mChatDatas.add(receiveIMMessageBean);

        mAdapter.notifyDataSetChanged();

        //如果不在底部弹出未读消息提示框，否则向下滑动，显示新消息
        if (recycleView.canScrollVertically(1)) {
            unreadCount++;
            unread_count_tv.setText(unreadCount > 99 ? "99+" : unreadCount + "");
            if (unread_count_tv.getVisibility() == View.GONE) {
                unread_count_tv.startAnimation(AnimationUtil.getInstance().ViewShowAnima(100));
                unread_count_tv.setVisibility(View.VISIBLE);
            }
        } else
            moveTo(mChatDatas.size() - 1);

        //如果是前一条转账的状态事件，刷新前一条发送转账消息的状态
        if (receiveIMMessageBean.getType() == 21 && receiveIMMessageBean.getMsgAccounts() != null) {
            RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {

                for (int i = 0; i < mChatDatas.size(); i++) {
                    if (mChatDatas.get(i).getMsgAccounts() != null && mChatDatas.get(i).getMsgAccounts().getId() != null && mChatDatas.get(i).getMsgAccounts().getId().equals(receiveIMMessageBean.getMsgAccounts().getId())) {
                        mChatDatas.get(i).setMsgAccounts(receiveIMMessageBean.getMsgAccounts());
                        e.onNext(i);
                        break;
                    }
                }
                e.onComplete();
            }), this).subscribe(o -> {
                int p = (int) o;
                mAdapter.notifyItemChanged(p);

            });

        }
    }

    @Override
    public void onSoftInputChanged(int height) {
        if (height > 0) {
            moveTo(mChatDatas.size() - 1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

/*        LogUtils.i("evnet y === " + event.getY());

//        LogUtils.i("ivVoiceRecordingStatus top === " + ivVoiceRecordingStatus.getBottom());

        LogUtils.i("btn_long_press_to_speak bottom === " + btn_long_press_to_speak.getBottom());

        LogUtils.i("ivVoiceRecordingStatus bottom === " + ivVoiceRecordingStatus.getBottom());

        int y = (int) (event.getY() - (btn_long_press_to_speak.getBottom() - ivVoiceRecordingStatus.getBottom()));

        LogUtils.i("height y === " + y);*/

        if (btn_long_press_to_speak.getBottom() > 0 && ivVoiceRecordingStatus.getBottom() > 0) {

            switch (event.getAction()) {
                case ACTION_DOWN:

                /*    clicktimeDValue = System.currentTimeMillis();

                    getP().startRecorder();

                    recordedVoiceScrolling.post(new Runnable() {
                        @Override
                        public void run() {
                            mRecordedVoiceAnimation.start();//启动动画
                        }
                    });

                    btn_long_press_to_speak.setText("松开发送");
                    btn_long_press_to_speak.setTextColor(Color.parseColor("#ffffff"));
                    btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#599252"));*/

                    break;
                case MotionEvent.ACTION_MOVE:

                    int y = (int) (event.getY() - (btn_long_press_to_speak.getBottom() - ivVoiceRecordingStatus.getBottom()));
                    int cancelY = ivVoiceRecordingStatus.getBottom();

/*                    LogUtils.i("y ==== " + y);

                    LogUtils.i("cancelY ==== " + cancelY);*/

                    //当滑动超过close bt getbottom的后，设置为取消状态
                    if (y > cancelY) {
                        mCancel = false;
                        btn_long_press_to_speak.setText("松开发送");
                        btn_long_press_to_speak.setTextColor(Color.parseColor("#ffffff"));
                        btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#599252"));
                        ivVoiceRecordingStatus.setImageResource(R.drawable.module_svg_im_voice_recording_status_open);
                    } else {
                        mCancel = true;
                        btn_long_press_to_speak.setText("取消发送");
                        btn_long_press_to_speak.setTextColor(Color.parseColor("#2E2E2E"));
                        btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#EDEDED"));
                        ivVoiceRecordingStatus.setImageResource(R.drawable.module_svg_im_voice_recording_status_close);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    if (mCancel) {
                        resetVoiceBt();
                        getP().cancelRecorder();
                        break;
                    }

                    if (System.currentTimeMillis() - clicktimeDValue > 1500) {
                        resetVoiceBt();
                        getP().stopRecorder();
                    } else {
                        onMessage("录制时间太短");
                        getP().cancelRecorder();
                        resetVoiceBt();

                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void resetVoiceBt() {

        clVoiceRecordingLayout.setVisibility(View.GONE);

        btn_long_press_to_speak.setText("松开发送");
        btn_long_press_to_speak.setTextColor(Color.parseColor("#ffffff"));
        btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#599252"));
        ivVoiceRecordingStatus.setImageResource(R.drawable.module_svg_im_voice_recording_status_open);

        recordedVoiceScrolling.post(new Runnable() {
            @Override
            public void run() {
                mRecordedVoiceAnimation.stop(); //关闭动画
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        if (v.getId() == R.id.recyclerView && emotionMainFragment != null) {
            emotionMainFragment.hideSoftKeyboard();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (emotionMainFragment != null) {
            if (!emotionMainFragment.isInterceptBackPress())
                finish();
        } else
            super.onBackPressed();
    }

    /**
     * 滑动到底部显示新消息，并清空未读泡泡
     */

    private void showNewMessage() {

        //如果不在底部，则滑动到底部
        if (recycleView.canScrollVertically(1))
            moveTo(mChatDatas.size() - 1);

        //清空未读消息数
        if (unreadCount > 0) {
            unreadCount = 0;
            AlphaAnimation hideAnima = (AlphaAnimation) AnimationUtil.getInstance().ViewHiddenAnima(100);
            unread_count_tv.startAnimation(hideAnima);
            hideAnima.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    unread_count_tv.setText(unreadCount + "");
                    unread_count_tv.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }


    /**
     * 移动到底部
     */
    private void moveTo(int pos) {

        recycleView.post(new Runnable() {
            @Override
            public void run() {
                MoveToPositionTop(recycleView, pos);
//                mLayoutManager.scrollToPositionWithOffset (pos,0);
            }
        });
    }

    @Override
    protected IMChatContract.IMChatPresenter createPresenter() {
        return new IMChatPresenter();
    }

    @Override
    public void onDestroy() {
        unregisterSoftInputChangedListener(getWindow());

        //清除锁定id
        Constant.CHAT_IDENTIFIER = "";
        LocalConstant.CHAT_UUID = "";
        if (mAdapter != null)
            mAdapter.cancel();
        super.onDestroy();
    }


    private void upLoadFile(String url) {

        onStarts();
        ReceiveIMMessageBean cb = new ReceiveIMMessageBean();
        cb.upload_progress = 1;
        cb.setType(10);
        cb.setId(-1);
        cb.setSendIdentifier("");
        cb.setContent(url);
//        cb.setCreateTime(getNowString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())));

        mChatDatas.add(cb);

        moveTo(mChatDatas.size() - 1);

        int pos = mChatDatas.size() - 1;

        Map<String, Object> map = getObjMap();

        map.put(Constant.FILE, new File(url));

        Observable<NormalBean> observable = HttpClient.postUpLoad(getUpLoadImage(), map, new ProgressListener() {
            @Override
            public void onProgress(boolean done, int progress) {
                if (done) {
                    //上传成功
                    mChatDatas.get(pos).upload_progress = 100;
                } else {
                    mChatDatas.get(pos).upload_progress = progress;
                    mAdapter.notifyItemChanged(pos);
                }
            }
        })
                .execute(new CallClazzProxy<ApiResult<NormalBean>, NormalBean>(new TypeToken<NormalBean>() {
                }.getType()) {
                });
        MyBaseSubscriber subscriber = new MyBaseSubscriber<NormalBean>(this) {
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                LogUtils.e(e);
                onMessage("图片上传失败 " + e.getMessage());
                mChatDatas.remove(pos);
                mAdapter.notifyDataSetChanged();
                onAfters();
            }

            @Override
            public void onNext(NormalBean normalBean) {
                onAfters();
                LogUtils.i("onNext onNext onNext onNext");

                mChatDatas.get(pos).upload_progress = 100;
                mChatDatas.get(pos).setContent(normalBean.url);

                mAdapter.notifyItemChanged(pos);

                getP().sendPictureMessage(mShareUuid, normalBean.url, pos);
//                getP().sendMessage(mShareUuid, 10, normalBean.url, 0, null);


            }
        };

        observable.subscribe(subscriber);
        addDisposable(subscriber);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        setIntent(intent);
//        init();
//        initData();
    }

    @Override
    public void onImageClick(String string) {
        if (!isEmpty(string))
            getImagePreviewInstance(this, NetworkAuto, 0, true).setImage(string).start();
    }

    @Override
    public void onAudioClick(String content, boolean isPlay, int pos) {

        recycleView.post(() -> {
            if (isPlay)
                getP().playVoice(content);
            else
                getP().stopVoice();
        });
    }


    /**
     * 清除语音未读
     *
     * @param position
     * @param id
     */
    @Override
    public void clearUnread(int position, long id) {
        getP().clearMessageUnread(position, id);

    }

    /**
     * 转账点击事件
     *
     * @param position
     * @param id
     */
    @Override
    public void onTransferClick(int position, long id) {
        getP().getTransfer(position, id);
    }

    /**
     * 头像点击事件
     *
     * @param position
     * @param id
     */
    @Override
    public void onAvatarClickListener(int position, long id) {
        if (otherRole == 10)
            return;

        if (!mChatDatas.get(position).getSendIdentifier().equals(UserInfoUtils.getInstance().getIdentifier()))
            goToPagePutSerializable(reference.get(), ClientBusinessCircleActivity.class, getIntentEntityMap(new Object[]{(int) id}));
    }

    @Override
    public void onChatHistoryList(IMChatHistoryListBean imChatHistoryListBean) {

        if (imChatHistoryListBean != null) {

            if (isEmpty(mShareUuid) && !isEmpty(imChatHistoryListBean.getShareUuid()))
                mShareUuid = imChatHistoryListBean.getShareUuid();

            //只在第一页的时候返回，为空就赋值
            if (mAdapter == null) {
                mChatDatas.clear();
                mMessageDetails = imChatHistoryListBean;
                //锁定自己的聊天id
                Constant.CHAT_IDENTIFIER = mMessageDetails.getMyAvatarInfo().getSendIdentifier();
                //记录聊天订阅id
                LocalConstant.CHAT_UUID = mShareUuid;

                mAdapter = new ChatAdapter(reference.get(), mChatDatas, imChatHistoryListBean, ChatActivity.this);
                recycleView.setAdapter(mAdapter);
                addData(imChatHistoryListBean.getList());


                if (imChatHistoryListBean.getOtherAvatarInfo() != null) {
                    title_layout.setTitleTextSize(16);
                    title_layout.setTitleText(imChatHistoryListBean.getOtherAvatarInfo().getSendNickname());//标题设置为对方昵称
                }
                moveTo(mChatDatas.size() - 1);

                cl_other_side_address_layout.setVisibility(imChatHistoryListBean.getAddress() != null ? View.VISIBLE : View.GONE);
                clNoAddressLayout.setVisibility(imChatHistoryListBean.getAddress() == null ? View.VISIBLE : View.GONE);

                //联系商家才显示
                store_ll.setVisibility(otherRole == 11 ? View.VISIBLE : View.GONE);

                //如果自己是商家、并且联系人也是商家
                ll_location.setVisibility(UserInfoUtils.getInstance().getUserInfo().getRole() == 11 && otherRole == 11 ? View.VISIBLE : View.GONE);

                //如果自己是商家、并且联系人是用户显示导航
                ll_navigation.setVisibility(UserInfoUtils.getInstance().getUserInfo().getRole() == 11 && otherRole == 10 ? View.VISIBLE : View.GONE);

                //用户联系商家
                right_arrow.setVisibility(UserInfoUtils.getInstance().getUserInfo().getRole() == 10 && otherRole == 11 ? View.VISIBLE : View.GONE);

                mAddress = imChatHistoryListBean.getAddress();

                if (imChatHistoryListBean.getAddress() != null) {

                    if (!isEmpty(imChatHistoryListBean.getAddress().getStreet()))
                        tvOtherSideAddress.setText(imChatHistoryListBean.getAddress().getStreet() + " " + imChatHistoryListBean.getAddress().getDoorplate());

                    if (!isEmpty(imChatHistoryListBean.getAddress().getMobile()))
                        tv_other_side_phone.setText(imChatHistoryListBean.getAddress().getMobile());

                    if (!isEmpty(imChatHistoryListBean.getAddress().getName()))
                        tv_other_side_nick_name.setText(imChatHistoryListBean.getAddress().getName());

                } else {
                    //对方角色。10=联系用户 11=联系商家 12=联系客服
                    //联系用户
                    if (otherRole == 10)
                        tv_no_address.setText("对方还没有添加收货地址");
                    else if (otherRole == 11) {
                        //联系商家如果有默认地址信息就发送
                        if (mDefaultAddress == null)
                            tv_no_address.setText("没有收货地址去添加");
                        else {
                            mAddress = mDefaultAddress;
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", mAddress.getId());
                            getP().sendMessage(mShareUuid, 30, "", 0, map);
                        }
                    }
                }

            } else {
                if (imChatHistoryListBean.getList() != null) {
                    addData(imChatHistoryListBean.getList());
                    mAdapter.notifyDataSetChanged();
                }
            }

        }

    }

    @Override
    public void onAddNewChatHistoryList(ArrayList<ReceiveIMMessageBean> list) {

        mChatDatas.clear();
        mChatDatas.addAll(list);
        moveTo(mChatDatas.size() - 1);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public LinkedList<ReceiveIMMessageBean> getNowChatHistoryList() {
        return mChatDatas;
    }

    @Override
    public void onSendMessageSuccess(ReceiveIMMessageBean receiveIMMessageBean) {
        createNewMsg(receiveIMMessageBean);
    }

    @Override
    public void onSendMessageSuccessResultPos(ReceiveIMMessageBean receiveIMMessageBean, int pos) {

        mChatDatas.set(pos, receiveIMMessageBean);

        mAdapter.notifyItemChanged(pos);

    }

    @Override
    public void onUploadImageUrl(String url) {
        upLoadFile(url);
        emotionMainFragment.hideSoftKeyboard();
    }

    @Override
    protected synchronized void showNewMessageDialog(ReceiveIMMessageBean unReadMessage) {
        //聊天页面不弹消息弹窗
//        super.showNewMessageDialog(unReadMessage);
    }

    @Override
    public String getShareUUID() {
        return mShareUuid;
    }

    @Override
    public void getTransFerSucceed(int position) {
        mChatDatas.get(position).getMsgAccounts().setStatus(2);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * 显示选择地址弹窗
     *
     * @param list
     */
    @Override
    public void showSelectAddressDialog(List<AddressBean> list) {

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(new IMSelectSendAddressPopupView(this, list, new IMSelectSendAddressPopupView.OnSendAddressListener() {
                    @Override
                    public void address(Object object) {

                    }
                }).show());
    }

    @Override
    public void onAddressResult(AddressBean cacheDefaultAddress) {
        mDefaultAddress = cacheDefaultAddress;
    }

    /**
     * 已读语音消息回调
     *
     * @param position
     * @param id
     */
    @Override
    public void readAudioMsg(int position, long id) {

        if (mChatDatas != null && mChatDatas.size() >= position && mChatDatas.get(position).getId() == id && mChatDatas.get(position).recipientRead == 0) {
            mChatDatas.get(position).recipientRead = 1;
            mAdapter.notifyItemChanged(position);
        }
    }
}

