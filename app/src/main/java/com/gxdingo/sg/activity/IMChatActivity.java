package com.gxdingo.sg.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMChatContentAdapter;
import com.gxdingo.sg.adapter.IMOtherFunctionsAdapter;
import com.kikis.commnlibrary.bean.AddressBean;
import com.gxdingo.sg.bean.FunctionsItem;
import com.gxdingo.sg.bean.IMChatHistoryListBean;
import com.kikis.commnlibrary.bean.ReceiveIMMessageBean;
import com.gxdingo.sg.biz.IMChatContract;
import com.gxdingo.sg.dialog.IMSelectSendAddressPopupView;
import com.gxdingo.sg.dialog.IMSelectTransferAccountsWayPopupView;
import com.gxdingo.sg.fragment.IMEmotionFragment;
import com.gxdingo.sg.fragment.IMEmotionItemFragment;
import com.gxdingo.sg.fragment.IMOtherFunctionsFragment;
import com.gxdingo.sg.presenter.IMChatPresenter;
import com.kikis.commnlibrary.utils.BitmapUtils;
import com.kikis.commnlibrary.utils.ScreenUtils;
import com.kikis.commnlibrary.utils.SystemUtils;
import com.kikis.commnlibrary.activitiy.BaseMvpActivity;
import com.kikis.commnlibrary.utils.MyToastUtils;
import com.kikis.commnlibrary.view.TemplateTitle;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullLinearLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * IM聊天
 */
@SuppressLint("NewApi")
public class IMChatActivity extends BaseMvpActivity<IMChatContract.IMChatPresenter> implements IMChatContract.IMChatListener {
    public static final String EXTRA_SHARE_UUID = "EXTRA_SHARE_UUID";//发布者与订阅者的共享唯一id常量

    @BindView(R.id.title_layout)
    TemplateTitle titleLayout;
    @BindView(R.id.tv_right_button)
    TextView tvRightButton;
    @BindView(R.id.notice_fl)
    RelativeLayout noticeFl;
    @BindView(R.id.tv_right_image_button)
    ImageView tvRightImageButton;
    @BindView(R.id.tv_other_side_nick_name)
    TextView tvOtherSideNickName;
    @BindView(R.id.tv_other_side_phone)
    TextView tvOtherSidePhone;
    @BindView(R.id.right_arrow)
    TextView rightArrow;
    @BindView(R.id.cl_other_side_address_layout)
    ConstraintLayout clOtherSideAddressLayout;
    @BindView(R.id.tv_no_address)
    TextView tvNoAddress;
    @BindView(R.id.cl_no_address_layout)
    ConstraintLayout clNoAddressLayout;
    @BindView(R.id.cv_other_side_address)
    CardView cvOtherSideAddress;
    @BindView(R.id.chat_content_list)
    PullRecyclerView chatContentList;
    @BindView(R.id.iv_voice)
    ImageView ivVoice;
    @BindView(R.id.et_content_input_box)
    EditText etContentInputBox;
    @BindView(R.id.btn_long_press_to_speak)
    TextView btnLongPressToSpeak;
    @BindView(R.id.iv_expression)
    ImageView ivExpression;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.btn_send_info)
    Button btnSendInfo;
    @BindView(R.id.ll_main_function_menu)
    LinearLayout llMainFunctionMenu;
    @BindView(R.id.rl_child_function_menu_layout)
    RelativeLayout rlChildFunctionMenuLayout;
    @BindView(R.id.cl_input_content)
    ConstraintLayout clInputContent;
    @BindView(R.id.layout)
    ConstraintLayout layout;
    @BindView(R.id.rl_cancel_recording_area)
    RelativeLayout rlCancelRecordingArea;
    @BindView(R.id.rl_recording_area)
    RelativeLayout rlRecordingArea;
    @BindView(R.id.cl_voice_recording_layout)
    ConstraintLayout clVoiceRecordingLayout;
    @BindView(R.id.view_occlusion_layer)
    View viewOcclusionLayer;
    @BindView(R.id.iv_voice_recording_status)
    ImageView ivVoiceRecordingStatus;
    @BindView(R.id.recorded_voice_scrolling)
    ImageView recordedVoiceScrolling;
    @BindView(R.id.tv_other_side_address)
    TextView tvOtherSideAddress;
    @BindView(R.id.view_input_content_layout_touch_shrink)
    View viewInputContentLayoutTouchShrink;

    RecyclerView.SmoothScroller mSmallClassSmoothScroller;
    FragmentManager fragmentManager;

    ArrayList<Fragment> mFragments = new ArrayList<>();
    IMOtherFunctionsFragment mIMOtherFunctionsFragment;//菜单-其他功能
    IMEmotionFragment mIMEmotionFragment;//菜单-表情
    AnimationDrawable mRecordedVoiceAnimation;//录制语音动画

    IMChatContentAdapter mIMChatContentAdapter;
    ArrayList<ReceiveIMMessageBean> mMessageDatas = new ArrayList<>();//聊天数据
    PullLinearLayoutManager mPullLinearLayoutManager;
    boolean isRestoreStackFromEnd;
    boolean isSetStackFromEnd;
    int mCurrClickFunctionMenuId;//当前点击的主功能菜单ID
    int mSoftInputHeight = 0;//软键盘高度

    IMChatHistoryListBean.MyAvatarInfo mMyAvatarInfo;//自己头像信息
    IMChatHistoryListBean.OtherAvatarInfo mOtherAvatarInfo;//对方头像信息
    AddressBean mAddress;//收货地址
    boolean isInitFirstLoad;//是否是初始化获取聊天记录列表时

    String mShareUuid;//发布者与订阅者的共享唯一id

    /**
     * IM聊天UI回调监听接口
     */
    public interface OnIMChatUICallbackListener {
        /**
         * 回调函数
         *
         * @param view
         * @param position item位置
         * @param type     类型（如0 文字、10 图片）
         * @param objData  数据（如文字、图片URL、语音URL）
         */
        void onCallback(View view, int position, int type, Object objData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected IMChatContract.IMChatPresenter createPresenter() {
        return new IMChatPresenter();
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }

    @Override
    protected int activityTitleLayout() {
        return R.layout.module_include_new_custom_title;
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
        return null;
    }

    @Override
    protected boolean closeDispatchTouchEvent() {
        return false;
    }

    @Override
    protected int initContentView() {
        return R.layout.module_activity_im_chat;
    }

    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }


    @Override
    protected void init() {
        //显示标题栏右边图片按钮
        tvRightImageButton.setVisibility(View.VISIBLE);
        //监听全局布局（用来监听软键盘显示和关闭）
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

        //聊天消息列表
        mSmallClassSmoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        mIMChatContentAdapter = new IMChatContentAdapter(this, mMessageDatas, mOnIMChatUICallbackListener);
        mPullLinearLayoutManager = new PullLinearLayoutManager(this);
        chatContentList.setLayoutManager(mPullLinearLayoutManager);
        chatContentList.addItemDecoration(new PullDividerItemDecoration(this, (int) getResources().getDimension(R.dimen.dp10)));
        chatContentList.setPullAdapter(mIMChatContentAdapter);
        chatContentList.setPullRefreshEnable(true);
        chatContentList.setOnRecyclerViewListener(new PullRecyclerView.OnRecyclerViewListener() {
            @Override
            public void onRefresh() {
                if (!TextUtils.isEmpty(mShareUuid)) {
                    getP().getChatHistoryList(mShareUuid,0,0);//获取聊天记录
                }
            }

            @Override
            public void onLoadMore() {
                //用不到，默认上拉加载更多是禁用的
            }
        });
        chatContentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int first = mPullLinearLayoutManager.findFirstVisibleItemPosition();
                /**
                 * 如果显示第一个item(即下拉刷新布局)并且RecyclerView是处在置顶状态下，
                 * 并且设置过setStackFromEnd(),并且没有恢复过setStackFromEnd()的，
                 * 则设置setStackFromEnd(false),让其恢复下拉刷新功能
                 */
                if (first == 0 && !chatContentList.canScrollVertically(-1) && isSetStackFromEnd && !isRestoreStackFromEnd) {
                    isRestoreStackFromEnd = true;
                    try {
                        mPullLinearLayoutManager.setStackFromEnd(false);
                    } catch (Exception e) {
                    }
                }
            }
        });

        //将光标设置在内容最后
        String content = etContentInputBox.getText().toString();
        etContentInputBox.setSelection(content.length());

        /**
         * 子菜单Fragment
         */
        fragmentManager = getSupportFragmentManager();
        //其他功能
        mIMOtherFunctionsFragment = new IMOtherFunctionsFragment();
        //表情功能
        mIMEmotionFragment = new IMEmotionFragment();
        mFragments.add(mIMOtherFunctionsFragment);
        mFragments.add(mIMEmotionFragment);

        //设置语音录制时动画播放图片素材
        recordedVoiceScrolling.setBackgroundResource(R.drawable.module_im_recorded_voice_scrolling);
        mRecordedVoiceAnimation = (AnimationDrawable) recordedVoiceScrolling.getBackground();

        etContentInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    ivAdd.setVisibility(View.GONE);
                    btnSendInfo.setVisibility(View.VISIBLE);
                } else {
                    ivAdd.setVisibility(View.VISIBLE);
                    btnSendInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //长按语音说话触摸监听
        setOnLongPressToRecordVoice();

    }



    @Override
    protected void initData() {
        mShareUuid = getIntent().getStringExtra(EXTRA_SHARE_UUID);

        if (!TextUtils.isEmpty(mShareUuid)) {
            getP().getChatHistoryList(mShareUuid,0,0);//获取聊天记录
        }
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
        //大于0说明软键盘已打开（弹出）
        if (mSoftInputHeight > 0) {
            //恢复输入内容布局原来的高度位置（当界面不可见的情况下系统会关闭软键盘的，所以需要恢复原来的位置）
            restoreInputContentLayoutPositionHeight();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除全局布局侦听器
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
        //刷新订阅列表显示最后聊天内容
//        sendEvent(100998);
    }

    /**
     * IM聊天UI回调匿名监听接口
     */
    private OnIMChatUICallbackListener mOnIMChatUICallbackListener = new OnIMChatUICallbackListener() {
        /**
         * 回调函数
         *
         * @param view
         * @param position item位置
         * @param type     类型（如0 文字、10 图片）
         * @param objData  数据（如文字、图片URL、语音URL）
         */
        @Override
        public void onCallback(View view, int position, int type, Object objData) {

        }
    };

    @Override
    protected void onBaseEvent(Object object) {
        /**
         * 其他功能
         */
        if (object instanceof FunctionsItem) {
            FunctionsItem functionsItem = (FunctionsItem) object;
            /**
             * 用户
             */
            if (functionsItem.type == IMOtherFunctionsAdapter.TYPE_USER) {
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
                    showSelectSendAddressDialog();
                }
                //转账
                else if (functionsItem.position == 3) {
                    showSelectTransferAccountsWayDialog();
                }
                //电话
                else if (functionsItem.position == 4) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + 10000));
                    startActivity(intent);
                }
                //投诉
                else if (functionsItem.position == 5) {
                    startActivity(new Intent(this, IMComplaintActivity.class));
                }
            }
            /**
             * 商家
             */
            else if (functionsItem.type == IMOtherFunctionsAdapter.TYPE_STORE) {

            }
        }

        /**
         * 表情
         */
        if (object instanceof IMEmotionItemFragment.EmotionData) {
            IMEmotionItemFragment.EmotionData emotionData = (IMEmotionItemFragment.EmotionData) object;
            //是否是退格键图标，是则执行删除光标处内容操作
            if (!TextUtils.isEmpty(emotionData.key) && emotionData.key.equals("[Backspace]")) {
                onDeleteContentAtCursorInInputBox();
            } else {
                onAddEmotionInTheInputBox(emotionData);
            }
        }

        /**
         * 转账
         */
        if (object instanceof IMSelectTransferAccountsWayPopupView) {

        }
        /**
         * 从IMMessageReceivingService接收到的IM消息
         */
        if (object instanceof ReceiveIMMessageBean) {
            ReceiveIMMessageBean receiveIMMessageBean = (ReceiveIMMessageBean) object;
            if (receiveIMMessageBean != null && !TextUtils.isEmpty(receiveIMMessageBean.getSendIdentifier())) {
                if (mOtherAvatarInfo != null) {
                    //判断消息发送者是否跟当前发送者一样
                    if (mOtherAvatarInfo.getSendIdentifier().equals(receiveIMMessageBean.getSendIdentifier())) {
                        //一样则通过该方法显示消息
                        onSendMessageSuccess(receiveIMMessageBean);
                    }
                }
            }
        }
    }

    /**
     * 返回聊天记录列表
     */
    @Override
    public void onChatHistoryList(IMChatHistoryListBean imChatHistoryListBean) {
        if (imChatHistoryListBean != null) {
            //只在第一页的时候返回，为空就赋值
            if (mMyAvatarInfo == null && mOtherAvatarInfo == null) {
                mMyAvatarInfo = imChatHistoryListBean.getMyAvatarInfo();
                mOtherAvatarInfo = imChatHistoryListBean.getOtherAvatarInfo();
                mAddress = imChatHistoryListBean.getAddress();
                mIMChatContentAdapter.setAvatar(mMyAvatarInfo, mOtherAvatarInfo);

                if (mOtherAvatarInfo != null) {
                    titleLayout.setTitleTextSize(16);
                    titleLayout.setTitleText(mOtherAvatarInfo.getSendNickname());//标题设置为对方昵称
                }
            }

            if (imChatHistoryListBean.getList() != null) {
                mMessageDatas.addAll(0, imChatHistoryListBean.getList());
                mIMChatContentAdapter.notifyDataSetChanged();
            }

            //是初始第一次加载聊天记录的，直接滚动到最底部。上拉加载第二页即之后的页数则滚动到上一页顶部的位置
            if (!isInitFirstLoad) {
                isInitFirstLoad = true;
                messageListScrollsToBottom(true);//聊天消息列表滚到底部
            } else {
                if (imChatHistoryListBean.getList() != null) {
                    if (mMessageDatas.size() >= imChatHistoryListBean.getList().size()) {
                        int scrollsPosition = mMessageDatas.size() - imChatHistoryListBean.getList().size();
                        chatContentList.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSmallClassSmoothScroller.setTargetPosition(scrollsPosition);
                                mPullLinearLayoutManager.startSmoothScroll(mSmallClassSmoothScroller);
                            }
                        },5000);
                    }
                }
            }
        }
        //停止下拉刷新
        chatContentList.stopRefresh();

    }

    /**
     * 没有更多历史聊天记录
     */
    @Override
    public void finishLoadmoreWithNoMoreData() {
        chatContentList.setPullRefreshEnable(false);//没有记录则禁用下拉刷新
    }

    /**
     * 发送消息成功
     *
     * @param receiveIMMessageBean 接收的IM消息
     */
    @Override
    public void onSendMessageSuccess(ReceiveIMMessageBean receiveIMMessageBean) {
        mMessageDatas.add(receiveIMMessageBean);
        mIMChatContentAdapter.notifyDataSetChanged();

        //判断当前是否在滚动列表，滚动则不滚动底部
        if (!chatContentList.isTouchScroll()) {
            messageListScrollsToBottom(false);//聊天消息列表滚到底部
        }
        etContentInputBox.setText("");
    }

    @Override
    public void onSendMessageSuccessResultPos(ReceiveIMMessageBean receiveIMMessageBean, int pos) {

    }


    /**
     * 回调上传图片URL
     *
     * @param url 服务器返回上传后的URL
     */
    @Override
    public void onUploadImageUrl(String url) {
        //发送图片消息
        getP().sendPictureMessage(mShareUuid, url, 0);
    }

    @Override
    public String getShareUUID() {
        return null;
    }

    @Override
    public void getTransFerSucceed(int position) {

    }

    @Override
    public void showSelectAddressDialog(List<AddressBean> list) {

    }

    /**
     * 转账
     */

    /**
     * 设置长按录音
     */
    private void setOnLongPressToRecordVoice() {
        btnLongPressToSpeak.setOnTouchListener(new View.OnTouchListener() {
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ivVoice.setVisibility(View.GONE);
                        ivExpression.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.GONE);

                        viewOcclusionLayer.setVisibility(View.VISIBLE);
                        rlCancelRecordingArea.setVisibility(View.VISIBLE);
                        llMainFunctionMenu.setBackgroundResource(0);

                        recordedVoiceScrolling.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecordedVoiceAnimation.start();//启动动画
                            }
                        });
                        btnLongPressToSpeak.setText("松开发送");
                        btnLongPressToSpeak.setTextColor(Color.parseColor("#ffffff"));
                        btnLongPressToSpeak.setBackgroundColor(Color.parseColor("#599252"));
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnLongPressToSpeak.getLayoutParams();
                        params.topMargin = 0;
                        params.leftMargin = 0;
                        params.rightMargin = 0;
                        params.bottomMargin = 0;
                        params.height = (int) getResources().getDimension(R.dimen.recording_area_button_height);

                        right = ScreenUtils.getScreenWidth(IMChatActivity.this);
                        bottom = params.height;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        System.out.println("left:" + left + ",top:" + top + ",right:" + right + ",bottom:" + bottom + "   x:" + event.getX() + "  y:" + event.getY());
                        if (x >= left && x <= right && y >= top && y <= bottom) {
                            btnLongPressToSpeak.setText("松开发送");
                            btnLongPressToSpeak.setTextColor(Color.parseColor("#ffffff"));
                            btnLongPressToSpeak.setBackgroundColor(Color.parseColor("#599252"));
                            ivVoiceRecordingStatus.setImageResource(R.drawable.module_svg_im_voice_recording_status_open);
                        } else {
                            btnLongPressToSpeak.setText("取消发送");
                            btnLongPressToSpeak.setTextColor(Color.parseColor("#2E2E2E"));
                            btnLongPressToSpeak.setBackgroundColor(Color.parseColor("#EDEDED"));
                            ivVoiceRecordingStatus.setImageResource(R.drawable.module_svg_im_voice_recording_status_close);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ivVoice.setVisibility(View.VISIBLE);
                        ivExpression.setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.VISIBLE);

                        viewOcclusionLayer.setVisibility(View.GONE);
                        rlCancelRecordingArea.setVisibility(View.GONE);
                        llMainFunctionMenu.setBackgroundColor(Color.parseColor("#EDEDED"));

                        recordedVoiceScrolling.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecordedVoiceAnimation.stop(); //关闭动画
                            }
                        });
                        btnLongPressToSpeak.setText("长按说话");
                        btnLongPressToSpeak.setTextColor(Color.parseColor("#2E2E2E"));
                        btnLongPressToSpeak.setBackgroundColor(Color.parseColor("#ffffff"));
                        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) btnLongPressToSpeak.getLayoutParams();
                        params2.topMargin = (int) getResources().getDimension(R.dimen.dp6);
                        params2.leftMargin = (int) getResources().getDimension(R.dimen.dp6);
                        params2.rightMargin = (int) getResources().getDimension(R.dimen.dp6);
                        params2.bottomMargin = (int) getResources().getDimension(R.dimen.dp6);
                        params2.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        MyToastUtils.customToast("松开");
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 发送文字消息
     * 两处调用sendTextMessage(),一处是onViewClicked发送按钮点击事件（软键盘隐藏时），一处是dispatchTouchEvent方法（软键盘弹出时）
     */
    private void sendTextMessage() {
        //发送文字消息
        getP().sendTextMessage(mShareUuid, etContentInputBox.getText().toString());
    }

    @OnClick({R.id.tv_right_image_button, R.id.tv_no_address, R.id.view_input_content_layout_touch_shrink, R.id.et_content_input_box, R.id.iv_voice, R.id.btn_long_press_to_speak, R.id.iv_expression, R.id.iv_add, R.id.btn_send_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right_image_button:
                break;
            case R.id.tv_no_address:
                break;
            case R.id.btn_long_press_to_speak:

                break;
            /**内容输入框*/
            case R.id.et_content_input_box:
                onContentInputEditClick(view);
                break;
            /**点击则隐藏输入发送内容布局下面的子菜单功能布局**/
            case R.id.view_input_content_layout_touch_shrink:
                onInputContentLayoutTouchShrink(view);
                break;
            /**语音，打开语音录制按钮**/
            case R.id.iv_voice:
                onVoiceClick(view);
                break;
            /**表情，打开表情网格**/
            case R.id.iv_expression:
                onExpressionClick(view);
                break;
            /**加号按钮，打开其他功能布局**/
            case R.id.iv_add:
                onAddClick(view);
                break;
            /**发送按钮,软键盘隐藏时触发*/
            case R.id.btn_send_info:
                //发送文字消息
                sendTextMessage();
                break;
        }
    }

    private void onInputContentLayoutTouchShrink(View view) {
        if (mCurrClickFunctionMenuId == R.id.iv_expression) {
            onExpressionClick(view);
        } else if (mCurrClickFunctionMenuId == R.id.iv_add) {
            onAddClick(view);
        }
        mCurrClickFunctionMenuId = 0;
        //隐藏输入内容布局触摸收缩View
        viewInputContentLayoutTouchShrink.setVisibility(View.GONE);
    }

    /**
     * 主要功能菜单 - 点击内容输入文本框
     */
    private void onContentInputEditClick(View view) {
        mCurrClickFunctionMenuId = 0;
        //隐藏输入内容布局触摸收缩View
        viewInputContentLayoutTouchShrink.setVisibility(View.GONE);
    }

    /**
     * 主要功能菜单-点击语音按钮
     */
    private void onVoiceClick(View view) {
        if (clVoiceRecordingLayout.getVisibility() == View.GONE) {
            mCurrClickFunctionMenuId = R.id.iv_voice;
            //隐藏输入内容布局触摸收缩View
            viewInputContentLayoutTouchShrink.setVisibility(View.GONE);

            //恢复原来的UI，并改变语音按钮图标
            restoreUIAndChangeCurBtnIcon(R.id.iv_voice);
            etContentInputBox.setVisibility(View.GONE);
            clVoiceRecordingLayout.setVisibility(View.VISIBLE);
            SystemUtils.hideKeyboard(view);
            hiddenMenuFunctionFragment();
            ivAdd.setVisibility(View.VISIBLE);
            btnSendInfo.setVisibility(View.GONE);

            //恢复输入发送内容布局在原来的位置（高度）
            restoreInputContentLayoutPositionHeight();
        } else {
            mCurrClickFunctionMenuId = 0;
            //传0表示只恢复原来的UI
            restoreUIAndChangeCurBtnIcon(0);
            SystemUtils.showKeyboard(this);
            etContentInputBox.requestFocus();
        }
    }

    /**
     * 主要功能菜单-点击表情按钮
     */
    private void onExpressionClick(View view) {
        if (menuFunctionFragmentIsHidden(mIMEmotionFragment)) {
            mCurrClickFunctionMenuId = R.id.iv_expression;

            etContentInputBox.requestFocus();
            SystemUtils.hideKeyboard(view);
            //恢复原来的UI，并改变表情按钮图标
            restoreUIAndChangeCurBtnIcon(R.id.iv_expression);
            showMenuFunctionFragment(mIMEmotionFragment);
            //恢复输入发送内容布局在原来的位置（高度）
            restoreInputContentLayoutPositionHeight();

            //显示输入内容布局触摸收缩View
            viewInputContentLayoutTouchShrink.setVisibility(View.VISIBLE);
        } else if (menuFunctionFragmentIsVisible(mIMEmotionFragment)) {
            mCurrClickFunctionMenuId = 0;
            hiddenMenuFunctionFragment();
            //传0表示只恢复原来的UI
            restoreUIAndChangeCurBtnIcon(0);
            SystemUtils.showKeyboard(this);
        }
    }

    /**
     * 主要功能菜单-点击加号按钮
     */
    private void onAddClick(View view) {
        if (menuFunctionFragmentIsHidden(mIMOtherFunctionsFragment)) {
            mCurrClickFunctionMenuId = R.id.iv_add;
            //显示输入内容布局触摸收缩View
            viewInputContentLayoutTouchShrink.setVisibility(View.VISIBLE);

            SystemUtils.hideKeyboard(view);
            //传0表示只恢复原来的UI
            restoreUIAndChangeCurBtnIcon(0);
            showMenuFunctionFragment(mIMOtherFunctionsFragment);
            //恢复输入发送内容布局在原来的位置（高度）
            restoreInputContentLayoutPositionHeight();

            ivAdd.setVisibility(View.VISIBLE);
            btnSendInfo.setVisibility(View.GONE);
        } else if (menuFunctionFragmentIsVisible(mIMOtherFunctionsFragment)) {
            mCurrClickFunctionMenuId = 0;
            hiddenMenuFunctionFragment();
            SystemUtils.showKeyboard(this);
        }
    }

    /**
     * 恢复输入内容布局位置高度
     */
    private void restoreInputContentLayoutPositionHeight() {
        setInputContentLayoutPositionHeight(0);
    }

    /**
     * 设置输入内容布局位置高度
     *
     * @param positionHeight
     */
    private void setInputContentLayoutPositionHeight(int positionHeight) {
        clInputContent.setPadding(0, 0, 0, positionHeight);
    }

    /**
     * 恢复原来的UI，并且改变当前按钮图标
     *
     * @param id 当前点中的按钮，传0表示只恢复原来的UI
     */
    private void restoreUIAndChangeCurBtnIcon(int id) {
        //先恢复所有按钮的原来图标
        ivVoice.setImageResource(R.drawable.module_svg_im_voice_8938);//语音按钮
        ivExpression.setImageResource(R.drawable.module_svg_im_expression);//表情按钮

        etContentInputBox.setVisibility(View.VISIBLE);
        clVoiceRecordingLayout.setVisibility(View.GONE);

        if (etContentInputBox.getText().toString() != null && etContentInputBox.getText().toString().length() > 0) {
            btnSendInfo.setVisibility(View.VISIBLE);
            ivAdd.setVisibility(View.GONE);
        } else {
            btnSendInfo.setVisibility(View.GONE);
            ivAdd.setVisibility(View.VISIBLE);
        }

        //只有下面的按钮才可以改变图标（改成软键盘图标）
        if (id == R.id.iv_voice || id == R.id.iv_expression) {
            //再将被点中的按钮图标改成软键盘图标
            ImageView imageView = findViewById(id);
            imageView.setImageResource(R.drawable.module_svg_im_soft_keyboard);
        }
    }

    /**
     * 全局布局侦听器,通过布局改变来监听软键盘高度改变(软键盘打开和关闭时回调)
     */
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //当点击语音或者表情或者加号按钮的时候触发了软键盘弹出或关闭操作时不执行下面代码
            if (mCurrClickFunctionMenuId == R.id.iv_voice
                    || mCurrClickFunctionMenuId == R.id.iv_expression
                    || mCurrClickFunctionMenuId == R.id.iv_add) {
                return;
            }

//            Rect r = new Rect();
//            /*
//             * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
//             * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
//             */
//            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//            //获取屏幕的高度
//            int screenHeight = getWindow().getDecorView().getRootView().getHeight();
//1
//            //判断是否有虚拟按键
//            if (ScreenUtils.hasNavBar(IMChatActivity.this)) {
//                int h = ScreenUtils.getNavigationBarHeight(IMChatActivity.this);
//                //计算软件盘的高度
//                mSoftInputHeight = screenHeight - (r.bottom + h);
//            } else {
//                //计算软件盘的高度
//                mSoftInputHeight = screenHeight - r.bottom;
//            }

            mSoftInputHeight = SystemUtils.getSoftInputHeight(IMChatActivity.this);

            //传0表示只恢复原来的UI
            restoreUIAndChangeCurBtnIcon(0);
            //表情Fragment或者其他功能Fragment显示的就隐藏掉所有的菜单Fragment
            if (menuFunctionFragmentIsVisible(mIMEmotionFragment) || menuFunctionFragmentIsVisible(mIMOtherFunctionsFragment)) {
                hiddenMenuFunctionFragment();
            }
            //设置输入发送内容布局高度在软键盘之上
            setInputContentLayoutPositionHeight(mSoftInputHeight);

            if (mSoftInputHeight > 0) {
                //聊天消息滚动到底部
                messageListScrollsToBottom(false);
            }
        }
    };


    /**
     * 聊天消息列表滚到底部
     */
    private void messageListScrollsToBottom(boolean isInfo) {
        if (isInfo) {
            //初始化获取聊天记录列表时
            chatContentList.post(new Runnable() {
                @Override
                public void run() {
                    //获取加载的最后一个可见视图在适配器的位置(包含了PullRecyclerView的头(下拉)和尾（上拉）item)
                    int lastVisibleItem = mPullLinearLayoutManager.findLastVisibleItemPosition();
                    int itemCount = chatContentList.getLayoutManager().getItemCount();
                    if (lastVisibleItem != -1) {
                        if ((lastVisibleItem + 1) < itemCount) {
                            //PullRecyclerView的item填满控件高度时跳转到最底部
                            mPullLinearLayoutManager.setStackFromEnd(true);
                            isSetStackFromEnd = true;//标记已经设置过
                        }
                    }
                }
            });
        } else {
            //软键盘弹出和发送消息时
            chatContentList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatContentList.scrollToPosition(mMessageDatas.size());
                }
            }, 50);
        }

    }

    /**
     * 在输入框中添加表情
     *
     * @param emotionData
     */
    private void onAddEmotionInTheInputBox(IMEmotionItemFragment.EmotionData emotionData) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), emotionData.value);
        int bitmapWH = (int) getResources().getDimension(R.dimen.emoji_size);
        Bitmap newBitmap = BitmapUtils.updateBitmapWidthAndHeight(bitmap, bitmapWH, bitmapWH);

        // 得到SpannableString对象,主要用于拆分字符串
        SpannableString spannableString = new SpannableString(emotionData.key);
        // 得到ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(this, newBitmap);
        // 调用spannableString的setSpan()方法
        spannableString.setSpan(imageSpan, 0, emotionData.key.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//将 Spannable.SPAN_INCLUSIVE_EXCLUSIVE改为Spannable.SPAN_EXCLUSIVE_EXCLUSIVE即可解决两张图片中间插入文字

        int index = etContentInputBox.getSelectionStart();
        Editable editable = etContentInputBox.getText();
        // 给EditText追加spannableString
        editable.insert(index, spannableString);
    }

    /**
     * 在输入框中删除光标处的内容
     */
    private void onDeleteContentAtCursorInInputBox() {
//        int index = etContentInputBox.getSelectionStart();
//        Editable editable = etContentInputBox.getText();
//        editable.delete(index - 1, index);

        //通过删除键删除EditText里的内容
        etContentInputBox.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    /**
     * 键盘高度改变(输入法关闭和打开时回调)
     *
     * @param height      高度
     * @param orientation 方向，横屏，竖屏
     */
//    @Override
//    public void onKeyboardHeightChanged(int height, int orientation) {
//        //当点击语音、表情和加号按钮的时候触发了软键盘弹出或关闭的不执行下面代码
//        if (mCurrClickFunctionMenuId == R.id.iv_voice
//                || mCurrClickFunctionMenuId == R.id.iv_expression
//                || mCurrClickFunctionMenuId == R.id.iv_add) {
//            return;
//        }
//
//
//        //判断是否有虚拟按键
//        if (ScreenUtils.hasNavBar(this)) {
//            int h = ScreenUtils.getNavigationBarHeight(this);
//            //计算软件盘的高度
//            mSoftInputHeight = height - h;
//        } else {
//            //计算软件盘的高度
//            mSoftInputHeight = height;
//        }
//
//        //传0表示只恢复原来的UI
//        restoreUIAndChangeCurBtnIcon(0);
//        //表情Fragment或者其他功能Fragment显示的就隐藏掉所有的菜单Fragment
//        if (menuFunctionFragmentIsVisible(mIMEmotionFragment) || menuFunctionFragmentIsVisible(mIMOtherFunctionsFragment)) {
//            hiddenMenuFunctionFragment();
//        }
//        //设置输入发送内容布局高度在软键盘之上
//        setInputContentLayoutPositionHeight(mSoftInputHeight);
//
//        if (mSoftInputHeight > 0) {
//            //滚动到底部
//            chatContentList.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    chatContentList.scrollToPosition(mTempDatas.size());
//                }
//            }, 50);
//        }
//    }

    /**
     * 根据坐标返回触摸到的View
     *
     * @param rootView
     * @param x
     * @param y
     * @return
     */
    private View getTouchTarget(View rootView, int x, int y) {
        View targetView = null;
        // 判断view是否可以聚焦
        ArrayList<View> touchableViews = rootView.getTouchables();
        for (View touchableView : touchableViews) {
            if (isTouchPointInView(touchableView, x, y)) {
                targetView = touchableView;
                break;
            }
        }
        return targetView;
    }

    /**
     * (x,y)是否在view的区域内
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        int left = position[0];
        int top = position[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (x >= left && x <= right && y >= top && y <= bottom) {
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            /**
             * 通过拦截避免点击发送按钮就隐藏了输入法
             */
            //获取触摸的View
            View v = getTouchTarget(layout, (int) ev.getX(), (int) ev.getY());
            if (v != null && v.getId() == R.id.btn_send_info) {//发送内容按钮
                ToastUtils.showLong("发送2");
                //发送消息
                sendTextMessage();
                return false;
//            } else if (v != null && v.getId() == R.id.rl_cancel_recording_area) {
//                MyToastUtils.customToast("取消录音吗？");
//            } else if (v != null && v.getId() == R.id.rl_recording_area) {
//                MyToastUtils.customToast("继续录音");
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 菜单功能Fragment是否隐藏
     *
     * @param fragment
     * @return
     */
    private boolean menuFunctionFragmentIsHidden(Fragment fragment) {
        if (fragment != null) {
            if (fragment.isAdded()) {
                return fragment.isHidden();
            }
            return true;
        }
        return true;
    }

    /**
     * 菜单功能Fragment是否显示
     *
     * @param fragment
     * @return
     */
    private boolean menuFunctionFragmentIsVisible(Fragment fragment) {
        if (fragment != null) {
            if (fragment.isAdded()) {
                return fragment.isVisible();
            }
            return false;
        }
        return false;
    }

    /**
     * 显示菜单功能Fragment
     *
     * @param fragment 要显示的Fragment
     */
    private void showMenuFunctionFragment(Fragment fragment) {
        hiddenMenuFunctionFragment();
        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.add(R.id.rl_child_function_menu_layout, fragment, "fragmentTagStr");
                fragmentManager.executePendingTransactions();
            }
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏菜单功能Fragment
     */
    private void hiddenMenuFunctionFragment() {
        for (Fragment fragment : mFragments) {
            if (fragment != null) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                if (fragment.isAdded()) {
                    ft.hide(fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }


    /**
     * 显示选择发送地址弹窗
     */
    private void showSelectSendAddressDialog() {
        new XPopup.Builder(reference.get())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .isDarkTheme(false)
                .asCustom(new IMSelectSendAddressPopupView(this, null, new IMSelectSendAddressPopupView.OnSendAddressListener() {
                    @Override
                    public void address(Object object) {

                    }
                }).show());
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
                    public void way(int p) {
                        startActivity(new Intent(IMChatActivity.this, IMTransferAccountsPayActivity.class));
                    }
                }).show());
    }


}
