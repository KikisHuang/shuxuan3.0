
package com.gxdingo.sg.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.gxdingo.sg.dialog.ChatFunctionDialog;
import com.gxdingo.sg.dialog.PostionFunctionDialog;
import com.gxdingo.sg.utils.ImMessageUtils;
import com.gxdingo.sg.utils.ShareUtils;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.FunctionsItem;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.gxdingo.sg.bean.NormalBean;
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
import com.kikis.commnlibrary.utils.AnimationUtil;
import com.kikis.commnlibrary.utils.Constant;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.bean.SHARE_MEDIA;
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
import static com.blankj.utilcode.util.ClipboardUtils.copyText;
import static com.blankj.utilcode.util.FileUtils.createOrExistsDir;
import static com.blankj.utilcode.util.KeyboardUtils.registerSoftInputChangedListener;
import static com.blankj.utilcode.util.KeyboardUtils.unregisterSoftInputChangedListener;
import static com.blankj.utilcode.util.PermissionUtils.isGranted;
import static com.blankj.utilcode.util.StringUtils.isEmpty;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.gxdingo.sg.http.Api.getUpLoadImage;
import static com.gxdingo.sg.utils.ImServiceUtils.startImService;
import static com.gxdingo.sg.utils.LocalConstant.EMOTION_LAYOUT_IS_SHOWING;
import static com.gxdingo.sg.utils.emotion.EmotionMainFragment.CHAT_ID;
import static com.gxdingo.sg.utils.emotion.EmotionMainFragment.ROLE;
import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.Constant.KEY;
import static com.kikis.commnlibrary.utils.GsonUtil.getObjMap;
import static com.kikis.commnlibrary.utils.IntentUtils.getImagePreviewInstance;
import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.RecycleViewUtils.MoveToPositionTop;


/**
 * @author: Kikis
 * @date: 2021/10/10
 * @page:????????????
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


    @BindView(R.id.rl_cancel_recording_area)
    public RelativeLayout rlCancelRecordingArea;

    @BindView(R.id.cl_voice_recording_layout)
    public ConstraintLayout clVoiceRecordingLayout;

    @BindView(R.id.iv_voice_recording_status)
    public ImageView ivVoiceRecordingStatus;

    @BindView(R.id.recorded_voice_scrolling)
    public ImageView recordedVoiceScrolling;

    @BindView(R.id.btn_long_press_to_speak)
    public TextView btn_long_press_to_speak;

    private AnimationDrawable mRecordedVoiceAnimation;//??????????????????

    //????????????????????????????????????id?????????????????????????????????
    private String mShareUuid = "";

    //???????????????10=???????????? 11=???????????? 12=????????????
    private int otherRole = 0;

    //?????????id???otherRole = 11?????????id???otherRole = 12 ?????????0 (?????????)
    //3.0?????? ???storeUserIdentifier otherRole?????????
    private String otherId = "";

    //?????????????????????
    private LinkedList<ReceiveIMMessageBean> mChatDatas;

    //subscribebean ???
    private IMChatHistoryListBean mMessageDetails;

    private ChatAdapter mAdapter;

    private EmotionMainFragment emotionMainFragment;

    //???????????????
    private int unreadCount = 0;

    private long clicktimeDValue = 0; //????????????????????????
    //????????????
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

        //??????????????????????????????ShareUuid???????????????otherRole???otherId
        otherRole = getIntent().getIntExtra(Constant.SERIALIZABLE + 1, 10);
        otherId = getIntent().getStringExtra(Constant.SERIALIZABLE + 2);

        if (otherRole != 12)
            title_layout.setMoreImg(R.drawable.module_svg_more_8935);

        initEmotionMainFragment();

        //?????????????????????
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
                //???????????????????????????????????????????????????
                if (!recycleView.canScrollVertically(1) && unreadCount > 0)
                    showNewMessage();
            }
        });
    }

    @Override
    protected void initData() {
        loadMore();
    }


    @Override
    protected void onStart() {
        super.onStart();

        //?????????????????????????????????im??????????????????????????????????????????
        if (!ImMessageUtils.getInstance().isRunning())
            getP().refreshHistoryList(mShareUuid, otherId, otherRole);

        if (UserInfoUtils.getInstance().isLogin())
            //im??????????????????
            startImService();


    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        loadMore();
    }

    /**
     * ??????????????????
     */
    private void loadMore() {
        getP().getChatHistoryList(mShareUuid, otherId, otherRole);//??????????????????
    }


    private void initEmotionMainFragment() {

        //?????????????????????????????????????????????
        recordedVoiceScrolling.setBackgroundResource(R.drawable.module_im_recorded_voice_scrolling);
        mRecordedVoiceAnimation = (AnimationDrawable) recordedVoiceScrolling.getBackground();

        //todo ????????????fragment??? CHAT_ID ???????????????,??????mShareUuid??????????????????CHAT_ID???????????????

        Bundle bundle = new Bundle();
        bundle.putString(KEY, ChatActivity.class.toString() + System.currentTimeMillis());
        bundle.putString(CHAT_ID, mShareUuid);
        bundle.putInt(ROLE, otherRole);
        //??????fragment
        //??????????????????
        emotionMainFragment = EmotiomComplateFragment.newInstance(EmotionMainFragment.class, bundle);
        emotionMainFragment.bindToContentView(refreshLayout);

        emotionMainFragment.setonTouchListener((v, event) -> {

            boolean locationPermiss = isGranted(RECORD_AUDIO);
            //????????????????????????
            if (!locationPermiss) {
                getP().checkRecordPermissions(getRxPermissions());
                return true;
            }
            clVoiceRecordingLayout.setVisibility(View.VISIBLE);

            if (event.getAction() == ACTION_DOWN) {
                clicktimeDValue = System.currentTimeMillis();
                getP().startRecorder();
                recordedVoiceScrolling.post(() -> {
                    mRecordedVoiceAnimation.start();//????????????
                });
                btn_long_press_to_speak.setText("????????????");
                btn_long_press_to_speak.setTextColor(Color.parseColor("#ffffff"));
                btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#599252"));
            }
            return false;
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotionview_main, emotionMainFragment);
        transaction.addToBackStack(null);
        //????????????
        transaction.commit();


    }


    @OnClick({R.id.unread_count_tv, R.id.img_back, R.id.img_more})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        switch (v.getId()) {

            case R.id.unread_count_tv:
                showNewMessage();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.img_more:
//                showBaseDialog();
                if (mMessageDetails != null && mMessageDetails.getOtherAvatarInfo() != null && !isEmpty(mMessageDetails.getOtherAvatarInfo().getSendIdentifier()))
                    goToPagePutSerializable(reference.get(), StoreDetailsActivity.class, getIntentEntityMap(new Object[]{mMessageDetails.getOtherAvatarInfo().getSendIdentifier()}));

                break;
        }
    }

    private void showBaseDialog() {

        int pos[] = {-1, -1}; //???????????????????????????

        title_layout.findViewById(com.kikis.commnlibrary.R.id.title).getLocationOnScreen(pos);//??????????????? Item ????????????????????????????????????????????? (0, 0)

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                .offsetY(pos[1])
                .offsetX(pos[0])
                .autoDismiss(true)
                .hasShadowBg(false)
                .asCustom(new PostionFunctionDialog(reference.get(), v -> {
                    switch (v.getId()) {
                        case R.id.share_ll:

                            if (mMessageDetails != null && !isEmpty(mMessageDetails.shareLinks))
                                ShareUtils.UmShare(reference.get(), null, mMessageDetails.shareLinks, "??????", "??????????????????????????????APP?????????????????????", R.mipmap.ic_app_logo, SHARE_MEDIA.WEIXIN);

                            break;
                        case R.id.report_ll:
                            goToPagePutSerializable(reference.get(), IMComplaintActivity.class, getIntentEntityMap(new Object[]{mMessageDetails.getOtherAvatarInfo().getSendIdentifier()}));

                            break;

                    }

                }, 1).show());
    }

    @Override
    protected void onBaseEvent(Object object) {

        //??????????????????????????????
        if (object instanceof AddressBean) {
            AddressBean addressBean = (AddressBean) object;
            if (addressBean.selectType == 2) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", addressBean.getId());

                getP().sendMessage(mShareUuid, 30, "", 0, map);
            }
        }

        //??????????????????
        if (object instanceof SendMessageBean) {
            SendMessageBean smb = (SendMessageBean) object;
            getP().sendMessage(mShareUuid, 0, smb.content, 0, null);
        }

        /**
         * ???IMMessageReceivingService????????????IM??????
         */
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean receiveIMMessageBean = (ReceiveIMMessageBean) object;
            if (receiveIMMessageBean != null && !TextUtils.isEmpty(receiveIMMessageBean.getSendIdentifier())) {
                if (mMessageDetails.getOtherAvatarInfo() != null) {

                    //??????????????????????????????????????????
                    if (mMessageDetails.getOtherAvatarInfo().getSendIdentifier().equals(receiveIMMessageBean.getSendIdentifier())) {
                        //????????????????????????????????????
                        receiveNewMsg(receiveIMMessageBean);
                    }

                    //???????????????????????????
                    if (UserInfoUtils.getInstance().getIdentifier().equals(receiveIMMessageBean.getSendIdentifier()) && receiveIMMessageBean.getType() == 20)
                        receiveNewMsg(receiveIMMessageBean);
                }
            }
        }

        /**
         * ????????????
         */
        if (object instanceof FunctionsItem) {
            FunctionsItem functionsItem = (FunctionsItem) object;

            //??????
            if (functionsItem.position == 0) {
                getP().photoSourceClick(0);
            }
            //??????
            else if (functionsItem.position == 1) {
                getP().photoSourceClick(1);
            }
            //??????
            else if (functionsItem.position == 2) {
                getP().getAddressList();
            }
            //??????
            else if (functionsItem.position == 3) {
                showSelectTransferAccountsWayDialog();
            }
        }

    }


    /**
     * ????????????????????????
     */
    private void showSelectTransferAccountsWayDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
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
     * ????????????????????????
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
                    //????????????
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
     * ???????????????
     *
     * @param receiveIMMessageBean
     */

    private void createNewMsg(ReceiveIMMessageBean receiveIMMessageBean) {
        try {

            mChatDatas.add(receiveIMMessageBean);
            mAdapter.notifyDataSetChanged();

            moveTo(mChatDatas.size() - 1);
        } catch (Exception e) {
            //???????????????????????????bugly
            CrashReport.postCatchedException(e);
        }
    }

    /**
     * ???????????????
     *
     * @param receiveIMMessageBean
     */

    private void receiveNewMsg(ReceiveIMMessageBean receiveIMMessageBean) {

        mChatDatas.add(receiveIMMessageBean);

        mAdapter.notifyDataSetChanged();

        //????????????????????????????????????????????????????????????????????????????????????
        if (recycleView.canScrollVertically(1)) {
            unreadCount++;
            unread_count_tv.setText(unreadCount > 99 ? "99+" : unreadCount + "");
            if (unread_count_tv.getVisibility() == View.GONE) {
                unread_count_tv.startAnimation(AnimationUtil.getInstance().ViewShowAnima(100));
                unread_count_tv.setVisibility(View.VISIBLE);
            }
        } else
            moveTo(mChatDatas.size() - 1);

        //????????????????????????????????????????????????????????????????????????????????????
        if (receiveIMMessageBean.getType() == 21 && receiveIMMessageBean.getDataByType() != null) {
            RxUtil.observe(Schedulers.newThread(), Observable.create(e -> {

                for (int i = 0; i < mChatDatas.size(); i++) {
                    if (mChatDatas.get(i).getDataByType() != null && mChatDatas.get(i).getDataByType().getId() > 0 && mChatDatas.get(i).getDataByType().getId() == (receiveIMMessageBean.getDataByType().getId())) {
                        mChatDatas.get(i).setDataByType(receiveIMMessageBean.getDataByType());
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

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                int y = (int) (event.getY() - (btn_long_press_to_speak.getBottom() - ivVoiceRecordingStatus.getBottom()));
                int cancelY = ivVoiceRecordingStatus.getBottom();

                //???????????????close bt getbottom??????????????????????????????
                if (y > cancelY) {
                    mCancel = false;
                    btn_long_press_to_speak.setText("????????????");
                    btn_long_press_to_speak.setTextColor(Color.parseColor("#ffffff"));
                    btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#599252"));
                    ivVoiceRecordingStatus.setImageResource(R.drawable.module_svg_im_voice_recording_status_open);
                } else {
                    mCancel = true;
                    btn_long_press_to_speak.setText("????????????");
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
                    onMessage("??????????????????");
                    getP().cancelRecorder();
                    resetVoiceBt();

                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void resetVoiceBt() {

        clVoiceRecordingLayout.setVisibility(View.GONE);

        btn_long_press_to_speak.setText("????????????");
        btn_long_press_to_speak.setTextColor(Color.parseColor("#ffffff"));
        btn_long_press_to_speak.setBackgroundColor(Color.parseColor("#599252"));
        ivVoiceRecordingStatus.setImageResource(R.drawable.module_svg_im_voice_recording_status_open);

        recordedVoiceScrolling.post(new Runnable() {
            @Override
            public void run() {
                mRecordedVoiceAnimation.stop(); //????????????
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
     * ??????????????????????????????????????????????????????
     */

    private void showNewMessage() {

        //???????????????????????????????????????
        if (recycleView.canScrollVertically(1))
            moveTo(mChatDatas.size() - 1);

        //?????????????????????
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
     * ???????????????
     */
    private void moveTo(int pos) {

        recycleView.post(() -> {
            MoveToPositionTop(recycleView, pos);
        });
    }

    @Override
    protected IMChatContract.IMChatPresenter createPresenter() {
        return new IMChatPresenter();
    }

    @Override
    public void onDestroy() {
        getP().saveDraft();

        unregisterSoftInputChangedListener(getWindow());

        //????????????id
        Constant.CHAT_IDENTIFIER = "";
        LocalConstant.CHAT_UUID = "";
        if (mAdapter != null)
            mAdapter.cancel();
        super.onDestroy();
    }

    /**
     * ??????????????????
     *
     * @param url
     */
    private void upLoadFile(String url) {

//        onStarts();
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

        Observable<NormalBean> observable = HttpClient.postUpLoad(getUpLoadImage(), map, (done, progress) -> {
            if (done) {

                if (pos <= mChatDatas.size() - 1 && mAdapter != null)
                    //????????????
                    mChatDatas.get(pos).upload_progress = 100;

            } else {
                if (pos <= mChatDatas.size() - 1 && mAdapter != null) {

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
                onMessage("?????????????????? " + e.getMessage());
                if (pos <= mChatDatas.size() - 1)
                    mChatDatas.remove(pos);
                mAdapter.notifyDataSetChanged();
//                onAfters();
            }

            @Override
            public void onNext(NormalBean normalBean) {
//                onAfters();
                if (pos <= mChatDatas.size() - 1) {
                    mChatDatas.get(pos).upload_progress = 100;
                    mChatDatas.get(pos).setContent(normalBean.url);

                    mAdapter.notifyItemChanged(pos);

                    getP().sendPictureMessage(mShareUuid, normalBean.url, pos);
                }
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
     * ??????????????????????????????
     *
     * @param pos
     */
    @Override
    public void onLocationMapClick(int pos) {
        goToPagePutSerializable(reference.get(), AddressMapInfoActivity.class, getIntentEntityMap(new Object[]{mAdapter.getData().get(pos)}));
    }


    /**
     * ??????????????????
     *
     * @param position
     * @param id
     */
    @Override
    public void clearUnread(int position, long id) {
        getP().clearMessageUnread(position, id);

    }

    /**
     * ??????????????????
     *
     * @param position
     * @param id
     */
    @Override
    public void onTransferClick(int position, long id) {
        getP().getTransfer(position, id);
    }

    /**
     * ??????????????????
     *
     * @param position
     * @param id
     */
    @Override
    public void onAvatarClickListener(int position, String id) {

        if (!mChatDatas.get(position).getSendIdentifier().equals(UserInfoUtils.getInstance().getIdentifier()) && mMessageDetails != null && !isEmpty(mMessageDetails.getOtherAvatarInfo().getSendNickname()))
            goToPagePutSerializable(reference.get(), ClientBusinessCircleActivity.class, getIntentEntityMap(new Object[]{id, mMessageDetails.getOtherAvatarInfo().getSendNickname()}));
    }

    /**
     * ??????item????????????
     *
     * @param position
     * @param isSelf
     */
    @Override
    public void onLongClickChatItem(int position, boolean isSelf) {

        ////???????????? 0=?????? 1=?????? 10=?????? 11=?????? 12=?????? 20=?????? 21=?????? 30=??????????????????
        if (((ReceiveIMMessageBean) mAdapter.getData().get(position)).getType() != 21 || ((ReceiveIMMessageBean) mAdapter.getData().get(position)).getType() != 20) {
            new XPopup.Builder(reference.get())
                    .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                    .autoDismiss(true)
                    .hasShadowBg(false)
                    .asCustom(new ChatFunctionDialog(reference.get(), isSelf, ((ReceiveIMMessageBean) mAdapter.getData().get(position)), args -> {
                        int type = (int) args[0];

                        if (type == 0) {
                            getP().copyText(((ReceiveIMMessageBean) mAdapter.getData().get(position)).getContent(), "?????????????????????");
                        } else if (type == 1) {
                            //??????
                            getP().revocationMessage(((ReceiveIMMessageBean) mAdapter.getData().get(position)).getId(), position);
                        } else if (type == 2) {
                            //???????????????
                            getP().voiceToText(((ReceiveIMMessageBean) mAdapter.getData().get(position)).getContent(), position);
                        } else if (type == 3) {
                            //??????
                            getP().delMessage(((ReceiveIMMessageBean) mAdapter.getData().get(position)).getId(), position);
                        } else if (type == 4) {
                            //??????????????????
                            ReceiveIMMessageBean.DataByType dataBt = ((ReceiveIMMessageBean) mAdapter.getData().get(position)).getDataByType();
                            getP().copyText(dataBt.getStreet() + " " + dataBt.getDoorplate() + " " + dataBt.getName() + " " + dataBt.getMobile(), "?????????????????????");
                        }

                    }).show());
        }


    }

    /**
     * ??????????????????
     *
     * @param position
     */
    @Override
    public void onMessageRevocation(int position) {
        mChatDatas.get(position).setStatus(1);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * ??????????????????
     *
     * @param obj
     * @param pos
     */
    @Override
    public void onIdentifiedContentResult(Object obj, int pos) {

        if (pos <= mChatDatas.size() - 1 && obj != null && mChatDatas.get(pos).getType() == 11) {
            mChatDatas.get(pos).voiceText = (String) obj;
            mAdapter.notifyItemChanged(pos);
        }
    }

    /**
     * ????????????
     *
     * @param position
     */
    @Override
    public void onMessageDelete(int position) {
        if (mAdapter != null) {
            mChatDatas.remove(position);
            mAdapter.notifyDataSetChanged();
            sendEvent(LocalConstant.DELETE_MESSAGE_CONTENT);
        }
    }


    @Override
    public void onChatHistoryList(IMChatHistoryListBean imChatHistoryListBean) {

        if (imChatHistoryListBean != null) {

            if (isEmpty(mShareUuid) && !isEmpty(imChatHistoryListBean.getShareUuid()))
                mShareUuid = imChatHistoryListBean.getShareUuid();

            getP().checkDraft();

            //????????????????????????????????????????????????
            if (mAdapter == null) {
                mChatDatas.clear();
                mMessageDetails = imChatHistoryListBean;
                //?????????????????????id
                Constant.CHAT_IDENTIFIER = mMessageDetails.getMyAvatarInfo().getSendIdentifier();
                //??????????????????id
                LocalConstant.CHAT_UUID = mShareUuid;

                mAdapter = new ChatAdapter(reference.get(), mChatDatas, imChatHistoryListBean, ChatActivity.this);
                recycleView.setAdapter(mAdapter);
                addData(imChatHistoryListBean.getList());

                if (imChatHistoryListBean.getOtherAvatarInfo() != null) {
                    title_layout.setTitleTextSize(16);
                    title_layout.setTitleText(imChatHistoryListBean.getOtherAvatarInfo().getSendNickname());//???????????????????????????
                }
                moveTo(mChatDatas.size() - 1);

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
        try {
            mChatDatas.clear();
            mChatDatas.addAll(list);
            moveTo(mChatDatas.size() - 1);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtils.e("onAddNewChatHistoryList === " + e);
            //???????????????????????????bugly
            CrashReport.postCatchedException(e);
        }
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
        if (pos <= mChatDatas.size() - 1) {
            mChatDatas.set(pos, receiveIMMessageBean);
            mAdapter.notifyItemChanged(pos);
        }
    }

    @Override
    public void onUploadImageUrl(String url) {
        upLoadFile(url);
        if (emotionMainFragment != null)
            emotionMainFragment.hideSoftKeyboard();
    }

    @Override
    protected synchronized void showNewMessageDialog(ReceiveIMMessageBean unReadMessage) {
        //??????????????????????????????
//        super.showNewMessageDialog(unReadMessage);
    }

    @Override
    public String getShareUUID() {
        return mShareUuid;
    }

    @Override
    public EditText getMessageEdttext() {
        return emotionMainFragment != null && emotionMainFragment.bar_edit_text != null ? emotionMainFragment.bar_edit_text : null;
    }

    @Override
    public String getSendIdentifier() {
        return mMessageDetails != null && mMessageDetails.getOtherAvatarInfo() != null && !isEmpty(mMessageDetails.getOtherAvatarInfo().getSendIdentifier()) ? mMessageDetails.getOtherAvatarInfo().getSendIdentifier() : "";
    }

    @Override
    public void getTransFerSucceed(int position) {
        mChatDatas.get(position).getDataByType().setStatus(2);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * ????????????????????????
     *
     * @param list
     */
    @Override
    public void showSelectAddressDialog(List<AddressBean> list) {

        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //???????????????????????????????????????????????????
                .isDarkTheme(false)
                .asCustom(new IMSelectSendAddressPopupView(this, list, object -> {

                }).show());
    }

    /**
     * ????????????????????????
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

