package com.gxdingo.sg.utils.emotion;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ChatActivity;
import com.gxdingo.sg.adapter.IMOtherFunctionsAdapter;
import com.gxdingo.sg.bean.FunctionsItem;
import com.gxdingo.sg.bean.GlobalBean;
import com.gxdingo.sg.bean.ImageModel;
import com.gxdingo.sg.utils.LocalConstant;
import com.gxdingo.sg.utils.SpanStringUtils;
import com.gxdingo.sg.utils.UserInfoUtils;
import com.kikis.commnlibrary.activitiy.BaseActivity;
import com.kikis.commnlibrary.fragment.BaseFragment;
import com.kikis.commnlibrary.utils.KikisUitls;
import com.kikis.commnlibrary.utils.RxUtil;
import com.kikis.commnlibrary.view.recycler_view.PullDividerItemDecoration;
import com.kikis.commnlibrary.view.recycler_view.PullGridLayoutManager;
import com.kikis.commnlibrary.view.recycler_view.PullRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import static com.kikis.commnlibrary.utils.Constant.CURRENT_POSITION_FLAG;
import static com.kikis.commnlibrary.utils.Constant.KEY;
import static com.kikis.commnlibrary.utils.SoftKeyboardUtils.touchEventInView;

/**
 * @author: Kikis
 * @date: 2020/12/29
 * @page:
 */
public class EmotionMainFragment extends BaseFragment {

    private int CurrentPosition = 0;

    public static final String CHAT_ID = "chat_id";
    public static final String ROLE = "role";
    public static final String TYPE = "type";

    protected Bundle args;


    @BindView(R.id.ll_emotion_layout)
    public LinearLayout ll_emotion_layout;

    @BindView(R.id.emotion_button)
    public ImageView emotion_button;

    @BindView(R.id.funcation_button)
    public ImageView funcation_button;

    @BindView(R.id.iv_voice)
    public ImageView iv_voice;

    @BindView(R.id.voice_tv)
    public TextView voice_tv;

    @BindView(R.id.bar_edit_text)
    public EditText bar_edit_text;

    @BindView(R.id.layout)
    public LinearLayout layout;


    @BindView(R.id.send_tv)
    public TextView send_tv;
/*
    @BindView(R.id.function_layout)
    public LinearLayout function_layout;*/

    @BindView(R.id.funcation_layout)
    public PullRecyclerView prvOtherFunctions;

    private IMOtherFunctionsAdapter mIMOtherFunctionsAdapter;//其他功能适配器，例如相册、拍照等

    @BindView(R.id.send_button)
    public Button send_button;

    @BindView(R.id.recyclerview_horizontal)
    public RecyclerView recyclerview_horizontal;

    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;

    //表情面板
    public EmotionKeyboard mEmotionKeyboard;

    //需要绑定的内容view
    private View contentView;

    //不可横向滚动的ViewPager
    @BindView(R.id.vp_emotionview_layout)
    public NoHorizontalScrollerViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    //当前标签页面的flag，用于eventBus响应
    private String PageFlag = "";

    //聊天id
    private String chatId = "";
    //对方角色。10=联系用户 11=联系商家 12=联系客服
    private int role = 10;


    private View.OnTouchListener onTouchListener;

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
        return R.layout.module_fragment_chat_emotion;
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
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
    }


    @Override
    protected void init() {
//        EventBusd
        PageFlag = args.getString(KEY);
        chatId = args.getString(CHAT_ID);
        role = args.getInt(ROLE);

        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(ll_emotion_layout)//绑定表情面板
                .setFuncationView(prvOtherFunctions)//绑定功能面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(bar_edit_text, chatId)//判断绑定那种EditView
                .bindToEmotionButton(emotion_button)//绑定表情按钮
                .bindToVoiceButton(iv_voice, voice_tv)//绑定语音按钮
                .bindToFuncationButton(funcation_button)//绑定功能按钮
                .bindToSendButton(send_tv, chatId)//绑定发送按钮
                .build();


        initFuncationLayout();
        initVoiceView();
    }

    private void initVoiceView() {

        if (onTouchListener != null)
            voice_tv.setOnTouchListener(onTouchListener);
    }

    private void initFuncationLayout() {
        mIMOtherFunctionsAdapter = new IMOtherFunctionsAdapter(reference.get(), role);
        prvOtherFunctions.addItemDecoration(new PullDividerItemDecoration(reference.get(), (int) getResources().getDimension(R.dimen.dp20), 0));
        prvOtherFunctions.setLayoutManager(new PullGridLayoutManager(reference.get(), 4));
        prvOtherFunctions.setPullAdapter(mIMOtherFunctionsAdapter);
        prvOtherFunctions.setOnItemClickListener(new PullRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventBus.getDefault().post(new FunctionsItem(UserInfoUtils.getInstance().getUserInfo().getRole(), position, mIMOtherFunctionsAdapter.getTitleItems()[position]));
            }
        });
    }


    @Override
    protected void initData() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.drawable.module_svg_expression_bt);
                model1.flag = "经典笑脸";
                model1.isSelected = true;
                list.add(model1);
            } else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.mipmap.ic_launcher_round);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }
        //记录底部默认选中第一个
        CurrentPosition = 0;
        SPUtils.getInstance().put(CURRENT_POSITION_FLAG, CurrentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        recyclerview_horizontal.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        recyclerview_horizontal.setAdapter(horizontalRecyclerviewAdapter);
        recyclerview_horizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
       /* horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SPUtils.getInstance().getInt(CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SPUtils.getInstance().put(CURRENT_POSITION_FLAG, CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {

            }
        });*/
    }

    /**
     * 显示软键盘、表情布局、礼物布局
     *
     * @param pos
     */
    private void showEmotionLayout(int pos) {

        if (mEmotionKeyboard.mEmotionLayout.isShown()) {
            mEmotionKeyboard.lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
            mEmotionKeyboard.hideEmotionLayout(true);//隐藏表情布局，显示软键盘
            mEmotionKeyboard.unlockContentHeightDelayed();//软键盘显示后，释放内容高度
        } else {
            if (mEmotionKeyboard.isSoftInputShown()) {//同上
                mEmotionKeyboard.lockContentHeight();
                mEmotionKeyboard.showEmotionLayout();
                mEmotionKeyboard.unlockContentHeightDelayed();
            } else {
                mEmotionKeyboard.showEmotionLayout();//两者都没显示，直接显示表情布局
            }
        }
        horizontalRecyclerviewAdapter.setCurrentItem(pos);
    }

    /**
     * 绑定内容view
     *
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView) {
        this.contentView = contentView;
    }


/*
    @OnClick({R.id.photo_album_img, R.id.camera_img})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.photo_album_img:
                ((ChatActivity) getActivity()).onViewClicked(v);
                break;
            case R.id.camera_img:
                ((ChatActivity) getActivity()).onViewClicked(v);
                break;

        }
    }*/

    @Override
    protected void onTypeEvent(Integer type) {
        super.onTypeEvent(type);
        if (type == LocalConstant.EMOTION_LAYOUT_IS_SHOWING) {
            mEmotionKeyboard.funcationLayoutStatus(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment f1 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE, PageFlag);
        fragments.add(f1);
        fragments.add(new Fragment());

       /* Bundle b = null;
        b = new Bundle();
        b.putString("Interge", "Fragment-" + "礼物");
        Fragment1 fg = Fragment1.newInstance(Fragment1.class, b);
        fragments.add(fg);*/

        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(), fragments);

        viewPager.setAdapter(adapter);
    }


    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        return mEmotionKeyboard.interceptBackPress();
    }

    public boolean isShowEmotion() {
        return mEmotionKeyboard.mEmotionLayout.isShown();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseFragmentEvent(Object object) {
        if (object != null)
            onBaseEvent(object);

    }

    protected void onBaseEvent(Object object) {
        if (object instanceof GlobalBean) {

            final GlobalBean gb = (GlobalBean) object;

            if (gb.flagPage.equals(PageFlag)) {
                switch (gb.event) {
                    case 0:
                        bar_edit_text.dispatchKeyEvent(new KeyEvent(
                                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                        break;
                    case 1:

                        final int curPosition = bar_edit_text.getSelectionStart();

                        RxUtil.observe(Schedulers.newThread(), Flowable.create(emitter -> {
                            if (bar_edit_text.getText().toString().length() + gb.emotionName.length() <= 250) {
                                // 获取当前光标位置,在指定位置上添加表情图片文本
                                StringBuilder sb = new StringBuilder(bar_edit_text.getText().toString());
                                sb.insert(curPosition, gb.emotionName);

                                // 特殊文字处理,将表情等转换一下
                                SpannableString spann = SpanStringUtils.getEmotionContent(gb.emotion_map_type,
                                        KikisUitls.getContext(), sb.toString());
                                emitter.onNext(spann);
                            }
                            emitter.onComplete();
                        }, BackpressureStrategy.ERROR), (BaseActivity) getActivity()).subscribe(o -> {
                            // 特殊文字处理,将表情等转换一下
                            SpannableString sp = (SpannableString) o;
                            bar_edit_text.setText(sp);

                            // 将光标设置到新增完表情的右侧
                            bar_edit_text.setSelection(curPosition + gb.emotionName.length());
                        });
                        break;
                }

            }
        }
    }


    /**
     * 隐藏软键盘或者表情框
     */
    public void hideSoftKeyboard() {
        try {

            if (mEmotionKeyboard.mEmotionLayout.isShown())
                mEmotionKeyboard.hideEmotionLayout(false);
            if (mEmotionKeyboard.isSoftInputShown())
                mEmotionKeyboard.hideSoftInput();
            //隐藏功能布局
            mEmotionKeyboard.funcationLayoutStatus(false);

        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    /**
     * edittext触摸事件判断并且隐藏软键盘
     *
     * @param x
     * @param y
     */
    public void emotionTouchEvent(float x, float y) {
        if (!touchEventInView(layout, x, y)) {
            hideSoftKeyboard();
        }
    }

    /**
     * edittext触摸事件判断
     *
     * @param x
     * @param y
     */
    public boolean emotionTouchEvents(float x, float y) {
        if (!touchEventInView(layout, x, y)) {
            return true;
        }
        return false;
    }

    public void setonTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}
