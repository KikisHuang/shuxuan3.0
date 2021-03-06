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
import com.gxdingo.sg.db.CommonDaoUtils;
import com.gxdingo.sg.db.DaoUtilsStore;
import com.gxdingo.sg.db.bean.DraftBean;
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

import static com.gxdingo.sg.db.SqlUtils.EQUAL;
import static com.gxdingo.sg.db.SqlUtils.WHERE;
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

    private IMOtherFunctionsAdapter mIMOtherFunctionsAdapter;//????????????????????????????????????????????????

    @BindView(R.id.send_button)
    public Button send_button;

    @BindView(R.id.recyclerview_horizontal)
    public RecyclerView recyclerview_horizontal;

    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;

    //????????????
    public EmotionKeyboard mEmotionKeyboard;

    //?????????????????????view
    private View contentView;

    //?????????????????????ViewPager
    @BindView(R.id.vp_emotionview_layout)
    public NoHorizontalScrollerViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    //?????????????????????flag?????????eventBus??????
    private String PageFlag = "";

    //??????id
    private String chatId = "";
    //???????????????10=???????????? 11=???????????? 12=????????????
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
                .setEmotionView(ll_emotion_layout)//??????????????????
                .setFuncationView(prvOtherFunctions)//??????????????????
                .bindToContent(contentView)//????????????view
                .bindToEditText(bar_edit_text, chatId)//??????????????????EditView
                .bindToEmotionButton(emotion_button)//??????????????????
                .bindToVoiceButton(iv_voice, voice_tv)//??????????????????
                .bindToFuncationButton(funcation_button)//??????????????????
                .bindToSendButton(send_tv, chatId)//??????????????????
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
                EventBus.getDefault().post(new FunctionsItem(role, position, mIMOtherFunctionsAdapter.getTitleItems()[position]));
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
                model1.flag = "????????????";
                model1.isSelected = true;
                list.add(model1);
            } else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.mipmap.ic_launcher_round);
                model.flag = "????????????" + i;
                model.isSelected = false;
                list.add(model);
            }
        }
        //?????????????????????????????????
        CurrentPosition = 0;
        SPUtils.getInstance().put(CURRENT_POSITION_FLAG, CurrentPosition);

        //??????tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        recyclerview_horizontal.setHasFixedSize(true);//???RecyclerView?????????????????????,???????????????RecyclerView?????????
        recyclerview_horizontal.setAdapter(horizontalRecyclerviewAdapter);
        recyclerview_horizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //?????????recyclerview_horizontal?????????
       /* horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //?????????????????????tab
                int oldPosition = SPUtils.getInstance().getInt(CURRENT_POSITION_FLAG, 0);
                //???????????????????????????
                datas.get(oldPosition).isSelected = false;
                //?????????????????????tab??????
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SPUtils.getInstance().put(CURRENT_POSITION_FLAG, CurrentPosition);
                //???????????????????????????????????????????????????
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager????????????
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {

            }
        });*/
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param pos
     */
    private void showEmotionLayout(int pos) {

        if (mEmotionKeyboard.mEmotionLayout.isShown()) {
            mEmotionKeyboard.lockContentHeight();//?????????????????????????????????????????????????????????
            mEmotionKeyboard.hideEmotionLayout(true);//????????????????????????????????????
            mEmotionKeyboard.unlockContentHeightDelayed();//???????????????????????????????????????
        } else {
            if (mEmotionKeyboard.isSoftInputShown()) {//??????
                mEmotionKeyboard.lockContentHeight();
                mEmotionKeyboard.showEmotionLayout();
                mEmotionKeyboard.unlockContentHeightDelayed();
            } else {
                mEmotionKeyboard.showEmotionLayout();//?????????????????????????????????????????????
            }
        }
        horizontalRecyclerviewAdapter.setCurrentItem(pos);
    }

    /**
     * ????????????view
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
        //??????fragment????????????
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //??????????????????
        EmotiomComplateFragment f1 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE, PageFlag);
        fragments.add(f1);
        fragments.add(new Fragment());

       /* Bundle b = null;
        b = new Bundle();
        b.putString("Interge", "Fragment-" + "??????");
        Fragment1 fg = Fragment1.newInstance(Fragment1.class, b);
        fragments.add(fg);*/

        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(), fragments);

        viewPager.setAdapter(adapter);
    }


    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @return true?????????????????????????????????????????????
     * false ???????????????????????????
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
                                // ????????????????????????,??????????????????????????????????????????
                                StringBuilder sb = new StringBuilder(bar_edit_text.getText().toString());
                                sb.insert(curPosition, gb.emotionName);

                                // ??????????????????,????????????????????????
                                SpannableString spann = SpanStringUtils.getEmotionContent(gb.emotion_map_type,
                                        KikisUitls.getContext(), sb.toString());
                                emitter.onNext(spann);
                            }
                            emitter.onComplete();
                        }, BackpressureStrategy.ERROR), (BaseActivity) getActivity()).subscribe(o -> {
                            // ??????????????????,????????????????????????
                            SpannableString sp = (SpannableString) o;
                            bar_edit_text.setText(sp);

                            // ??????????????????????????????????????????
                            bar_edit_text.setSelection(curPosition + gb.emotionName.length());
                        });
                        break;
                }

            }
        }
    }


    /**
     * ??????????????????????????????
     */
    public void hideSoftKeyboard() {
        try {

            if (mEmotionKeyboard.mEmotionLayout.isShown())
                mEmotionKeyboard.hideEmotionLayout(false);
            if (mEmotionKeyboard.isSoftInputShown())
                mEmotionKeyboard.hideSoftInput();
            //??????????????????
            mEmotionKeyboard.funcationLayoutStatus(false);

        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    /**
     * edittext???????????????????????????????????????
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
     * edittext??????????????????
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
