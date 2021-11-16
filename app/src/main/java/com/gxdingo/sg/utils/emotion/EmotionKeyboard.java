package com.gxdingo.sg.utils.emotion;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.SendMessageBean;
import com.gxdingo.sg.utils.LocalConstant;
import com.taobao.monitor.adapter.network.TBRestSender;

import org.greenrobot.eventbus.EventBus;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.BarUtils.getNavBarHeight;
import static com.blankj.utilcode.util.BarUtils.isNavBarVisible;
import static com.blankj.utilcode.util.ScreenUtils.getScreenHeight;
import static com.blankj.utilcode.util.TimeUtils.getNowString;
import static com.kikis.commnlibrary.utils.Constant.SOFT_INPUT_HEIGHT;

/**
 * @author: Kikis
 * @date: 2020/10/26
 * @page: 自定义表情底部指示器
 */
public class EmotionKeyboard {

    private Activity mActivity;
    //    private InputMethodManager mInputManager;//软键盘管理类
    public View mEmotionLayout;//表情布局
    public View mFuncationLayout;//功能布局
    private EditText mEditText;//
    private View mContentView;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪
    private ImageView expressionimg;
    private ImageView voiceimg;
    private TextView voicetv;
    private boolean showVoice = false;
    private long onTouchtime = 0;

    private EmotionKeyboard() {

    }

    /**
     * 外部静态调用
     *
     * @param activity
     * @return
     */
    public static EmotionKeyboard with(Activity activity) {
        EmotionKeyboard emotionInputDetector = new EmotionKeyboard();
        emotionInputDetector.mActivity = activity;
//        emotionInputDetector.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return emotionInputDetector;
    }

    /**
     * 绑定内容view，此view用于固定bar的高度，防止跳闪
     *
     * @param contentView
     * @return
     */
    public EmotionKeyboard bindToContent(View contentView) {
        mContentView = contentView;
        return this;
    }


    /**
     * 功能窗口
     *
     * @param funcationBt
     * @return
     */
    public EmotionKeyboard bindToFuncationButton(View funcationBt) {

        funcationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFuncationLayout.getVisibility() == View.VISIBLE) {
//                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    funcationLayoutStatus(false);
//                    unlockContentHeightDelayed();//软键盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockContentHeight();
                        hideSoftInput();
                        hideEmotionLayout(false);//隐藏表情布局，显示软键盘
                        funcationLayoutStatus(true);
                        unlockContentHeightDelayed();
                    } else {
                        hideEmotionLayout(false);//隐藏表情布局，显示软键盘
                        funcationLayoutStatus(true);//两者都没显示，直接功能布局
                    }
                }
            }
        });
        return this;
    }

    /**
     * 绑定编辑框
     *
     * @param editText
     * @param chatId
     * @return
     */
    public EmotionKeyboard bindToEditText(EditText editText, String chatId) {

        mEditText = editText;

//        mEditText.requestFocus();

/*        mEditText.setInputType(TYPE_TEXT_FLAG_MULTI_LINE);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEND);
        mEditText.setSingleLine(chatId != -1?false:true);*/
//        mEditText.setMaxLines(3);

        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    onTouchtime = System.currentTimeMillis();

                //防止长按表情、功能布局的缩回
                if (System.currentTimeMillis() - onTouchtime < 300) {
                    if (event.getAction() == MotionEvent.ACTION_UP && mFuncationLayout.getVisibility() == View.VISIBLE) {

                        lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                        funcationLayoutStatus(false);

                        unlockContentHeightDelayed();
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {

                        lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                        hideEmotionLayout(true);//隐藏表情布局，显示软件盘

                        //软件盘显示后，释放内容高度
                        mEditText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                unlockContentHeightDelayed();
                            }
                        }, 200L);
                    }
                }


                return false;
            }
        });

        //软键盘发送按钮事件监听
        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (mEditText != null && mEditText.getText().toString().length() > 0) {

                    SendMessageBean smb = new SendMessageBean(chatId, mEditText.getText().toString());

                    if (isEmpty(chatId))
                        smb.message_type = 1;

                    EventBus.getDefault().post(smb);
                    mEditText.setText("");
                }
            }
            return false;
        });

        return this;
    }


    /**
     * 绑定表情按钮
     *
     * @param emotionButton
     * @return
     */
    public EmotionKeyboard bindToEmotionButton(View emotionButton) {
        expressionimg = (ImageView) emotionButton;
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionLayout.isShown()) {
//                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软键盘
//                    unlockContentHeightDelayed();//软键盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                    }
                }
            }
        });
        return this;
    }

    /**
     * 绑定语音按钮
     *
     * @param iv_voice
     * @param voice_tv
     * @return
     */
    public EmotionKeyboard bindToVoiceButton(ImageView iv_voice, TextView voice_tv) {

        voiceimg = iv_voice;
        voicetv = voice_tv;
        iv_voice.setOnClickListener(v -> voiceLayoutStatus(!showVoice));
        return this;
    }

    /**
     * 绑定发送按钮
     *
     * @param send_button
     * @param chatId
     * @return
     */
    public EmotionKeyboard bindToSendButton(View send_button, final String chatId) {
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText != null && mEditText.getText().toString().length() > 0) {
                    EventBus.getDefault().post(new SendMessageBean(chatId, mEditText.getText().toString()));
                    mEditText.setText("");
                }

            }
        });
        return this;
    }

    /**
     * 设置表情内容布局
     *
     * @param emotionView
     * @return
     */
    public EmotionKeyboard setEmotionView(View emotionView) {
        mEmotionLayout = emotionView;
        return this;
    }

    /**
     * 设置功能面板布局
     *
     * @param funcationView
     * @return
     */
    public EmotionKeyboard setFuncationView(View funcationView) {
        mFuncationLayout = funcationView;
        return this;
    }


    public EmotionKeyboard build() {
        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        //从而方便我们计算软件盘的高度
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //隐藏软件盘
        hideSoftInput();
        return this;
    }

    /**
     * 点击返回键时先隐藏表情布局
     *
     * @return
     */
    public boolean interceptBackPress() {
        if (mEmotionLayout.isShown()) {
            hideEmotionLayout(false);
            return true;
        }
        return false;
    }

    /**
     * 显示表情布局
     */
    public void showEmotionLayout() {

        funcationLayoutStatus(false);
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight <= 0) {
            softInputHeight = getKeyBoardHeight();
        }
        if (showVoice)
            hideVoiceBt();
  /*      if (expressionimg != null)
            expressionimg.setImageResource(R.drawable.module_svg_im_soft_keyboard);*/

        hideSoftInput();
        EventBus.getDefault().post(LocalConstant.EMOTION_LAYOUT_IS_SHOWING);

        mEmotionLayout.getLayoutParams().height = softInputHeight;
        mEmotionLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    public void hideEmotionLayout(boolean showSoftInput) {

        if (mEmotionLayout.isShown()) {
//            expressionimg.setImageResource(R.drawable.module_svg_im_expression);
           /* if (showSoftInput) {
                showSoftInput();
            }*/
            mEmotionLayout.setVisibility(View.GONE);
        }
    }


    /**
     * 隐藏语音按钮
     */
    public void hideVoiceBt() {
        voiceimg.setImageResource(R.drawable.module_svg_im_voice_8938);
        mEditText.setVisibility(View.VISIBLE);

        voicetv.setVisibility(View.GONE);
        showVoice = false;
    }

    /**
     * 显示隐藏表情布局
     *
     * @param show 是否显示
     */
    public void voiceLayoutStatus(boolean show) {
        voicetv.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show) {
            if (mEmotionLayout.isShown()) {
                hideEmotionLayout(false);
                mEmotionLayout.setVisibility(View.GONE);
            }
            if (isSoftInputShown())
                hideSoftInput();

            if (mFuncationLayout.getVisibility() == View.VISIBLE)
                funcationLayoutStatus(false);
            voicetv.setText("按住 说话");
            mEditText.setVisibility(View.INVISIBLE);
            voiceimg.setImageResource(R.drawable.module_svg_im_soft_keyboard);
        } else {
            voiceimg.setImageResource(R.drawable.module_svg_im_voice_8938);

            mEditText.setVisibility(View.VISIBLE);

            if (!isSoftInputShown())
                showSoftInput();

        }

        showVoice = show;
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    public void lockContentHeight() {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    public void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    public void showSoftInput() {
        if (mEditText != null) {
            mEditText.requestFocus();
            mEditText.post(new Runnable() {
                @Override
                public void run() {
                    KeyboardUtils.showSoftInput();
                }
            });
        }

    }


    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        if (mEditText != null && mActivity != null)
            KeyboardUtils.hideSoftInput(mActivity);
    }


    /**
     * 功能布局隐藏显示方法
     *
     * @param show
     */
    public void funcationLayoutStatus(boolean show) {

        if (show) {

            if (showVoice)
                hideVoiceBt();

            if (mFuncationLayout.getVisibility() == View.GONE) {

                int softInputHeight = getSupportSoftInputHeight();
                if (softInputHeight <= 0) {
                    softInputHeight = getKeyBoardHeight();
                }

                mFuncationLayout.getLayoutParams().height = softInputHeight;
                mFuncationLayout.setVisibility(View.VISIBLE);

                EventBus.getDefault().post(LocalConstant.EMOTION_LAYOUT_IS_SHOWING);
            }

        } else {
            if (mFuncationLayout.getVisibility() == View.VISIBLE) {
                mFuncationLayout.setVisibility(View.GONE);
            }

        }
    }


    /**
     * 是否显示软件盘
     *
     * @return
     */
    public boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软键盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            if (isNavBarVisible(mActivity))
                softInputHeight = softInputHeight - getNavBarHeight();

        }

        if (softInputHeight < 0) {
            LogUtils.w("EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
            SPUtils.getInstance().put(SOFT_INPUT_HEIGHT, softInputHeight);
        }

        return softInputHeight;
    }

    /**
     * 获取软键盘高度，如果没有从PreventKeyboardBlockUtil监听获取到软键盘的高度，则根据屏幕比例算出一个平均的软键盘高度。
     *
     * @return
     */
    public int getKeyBoardHeight() {
        int height = getScreenHeight();
        return SPUtils.getInstance().getInt(SOFT_INPUT_HEIGHT) <= 0 ? (int) (height / 2.6) : SPUtils.getInstance().getInt(SOFT_INPUT_HEIGHT);
    }


}
