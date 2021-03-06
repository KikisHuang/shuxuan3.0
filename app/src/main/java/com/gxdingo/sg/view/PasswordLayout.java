package com.gxdingo.sg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.gxdingo.sg.R;
import com.kikis.commnlibrary.activitiy.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.KeyboardUtils.isSoftInputVisible;
import static com.blankj.utilcode.util.RegexUtils.isMatch;


public class PasswordLayout extends LinearLayout {
    private int maxLength = 6; //密码长度

    private int inputIndex = 0; //设置子View状态index

    private List<String> mPassList;//储存密码

    private pwdChangeListener pwdChangeListener;//密码状态改变监听


    private Context mContext;

    private boolean mIsShowInputLine;
    private boolean mShowSoftInput;
    private int mInputColor;
    private int mNoinputColor;
    private int mLineColor;
    private int mTxtInputColor;
    private int mDrawType;
    private int mInterval;
    private int mItemWidth;
    private int mItemHeight;
    private int mShowPassType;
    private int mTxtSize;
    private int mBoxLineSize;


    public void setPwdChangeListener(pwdChangeListener pwdChangeListener) {
        this.pwdChangeListener = pwdChangeListener;
    }

    public PasswordLayout(Context context) {
        this(context, null);
    }

    public PasswordLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化View
     */
    private void initView(Context context, AttributeSet attrs) {

        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PassWordLayoutStyle);

        mInputColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_box_input_color, R.color.pass_view_rect_input);
        mNoinputColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_box_no_input_color, R.color.regi_line_color);
        mLineColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_input_line_color, R.color.pass_view_rect_input);
        mTxtInputColor = ta.getResourceId(R.styleable.PassWordLayoutStyle_text_input_color, R.color.pass_view_rect_input);
        mDrawType = ta.getInt(R.styleable.PassWordLayoutStyle_box_draw_type, 0);
        mInterval = ta.getDimensionPixelOffset(R.styleable.PassWordLayoutStyle_interval_width, 4);
        maxLength = ta.getInt(R.styleable.PassWordLayoutStyle_pass_leng, 6);
        mItemWidth = ta.getDimensionPixelOffset(R.styleable.PassWordLayoutStyle_item_width, 40);
        mItemHeight = ta.getDimensionPixelOffset(R.styleable.PassWordLayoutStyle_item_height, 40);
        mShowPassType = ta.getInt(R.styleable.PassWordLayoutStyle_pass_inputed_type, 0);
        mTxtSize = ta.getDimensionPixelOffset(R.styleable.PassWordLayoutStyle_draw_txt_size, 18);
        mBoxLineSize = ta.getDimensionPixelOffset(R.styleable.PassWordLayoutStyle_draw_box_line_size, 4);
        mIsShowInputLine = ta.getBoolean(R.styleable.PassWordLayoutStyle_is_show_input_line, true);
        mShowSoftInput = ta.getBoolean(R.styleable.PassWordLayoutStyle_show_soft_input, true);
        ta.recycle();

        mPassList = new ArrayList<>();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        if (mShowSoftInput) {
            //设置点击时弹出输入法
            setOnClickListener(view -> {
                setFocusable(true);
                setFocusableInTouchMode(true);
                requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(PasswordLayout.this, InputMethodManager.SHOW_IMPLICIT);
            });
            this.setOnKeyListener(new MyKeyListener());//按键监听
        } else {
            setFocusable(false);
            setFocusableInTouchMode(false);
        }


        setOnFocusChangeListener((view, b) -> {
            if (b) {
                PasswordView passwordView = (PasswordView) getChildAt(inputIndex);
                if (passwordView != null) {
                    passwordView.setmIsShowRemindLine(mIsShowInputLine);
                    passwordView.startInputState();
                }
            } else {
                PasswordView passwordView = (PasswordView) getChildAt(inputIndex);
                if (passwordView != null) {
                    passwordView.setmIsShowRemindLine(false);
                    passwordView.updateInputState(false);
                }
            }
        });
    }

    /**
     * 添加子View
     *
     * @param context
     */
    private void addChildVIews(Context context) {
        for (int i = 0; i < maxLength; i++) {
            PasswordView passwordView = new PasswordView(context);
            LayoutParams params = new LayoutParams(mItemWidth, mItemHeight);
            if (i > 0) {                                       //第一个和最后一个子View不添加边距
                params.leftMargin = mInterval;
            }

            passwordView.setInputStateColor(mInputColor);
            passwordView.setNoinputColor(mNoinputColor);
            passwordView.setInputStateTextColor(mTxtInputColor);
            passwordView.setRemindLineColor(mLineColor);
            passwordView.setmBoxDrawType(mDrawType);
            passwordView.setmShowPassType(mShowPassType);
            passwordView.setmDrawTxtSize(mTxtSize);
            passwordView.setmDrawBoxLineSize(mBoxLineSize);
            passwordView.setmIsShowRemindLine(mIsShowInputLine);

            addView(passwordView, params);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 0) {     //判断 子View宽+边距是否超过了父布局 超过了则重置宽高
            if ((maxLength * mItemWidth + (maxLength - 1) * mInterval) > getMeasuredWidth()) {
                mItemWidth = (getMeasuredWidth() - (maxLength - 1) * mInterval) / maxLength;
                mItemHeight = mItemWidth;
            }

            addChildVIews(getContext());
        }

    }

    /**
     * 添加密码
     *
     * @param pwd
     */
    public void addPwd(String pwd) {
        if (mPassList != null && mPassList.size() < maxLength) {
            mPassList.add(pwd + "");
            setNextInput(pwd);
        }

        if (pwdChangeListener != null) {
            if (mPassList.size() < maxLength) {
                pwdChangeListener.onChange(getPassString());
            } else {
                pwdChangeListener.onFinished(getPassString());
            }
        }
    }

    /**
     * 添加全部密码
     *
     * @param pwd 密码
     */
    public void addAllPwd(String pwd) {
        if (!isEmpty(pwd) && pwd.length() == maxLength && mPassList.size() == 0) {
            String strings[] = pwd.split("");
            for (int i = 0; i < strings.length; i++) {
                if (!isEmpty(strings[i]))
                    addPwd(strings[i]);
            }
        }
    }

    /**
     * 删除密码
     */
    public void removePwd() {
        if (mPassList != null && mPassList.size() > 0) {
            mPassList.remove(mPassList.size() - 1);
            setPreviosInput();
        }

        if (pwdChangeListener != null) {
            if (mPassList.size() > 0) {
                pwdChangeListener.onChange(getPassString());
            } else {
                pwdChangeListener.onNull();
            }
        }
    }

    /**
     * 清空所有密码
     */
    public void removeAllPwd() {
        if (mPassList != null) {
            for (int i = mPassList.size(); i >= 0; i--) {
                if (i > 0) {
                    setNoInput(i, false, "");
                } else if (i == 0) {
                    PasswordView passWordView = (PasswordView) getChildAt(i);
                    if (passWordView != null) {
                        passWordView.setmPassText("");
                        passWordView.startInputState();
                    }
                }

            }

            mPassList.clear();
            inputIndex = 0;
        }


        if (pwdChangeListener != null) {
            pwdChangeListener.onNull();
        }
    }

    /**
     * 获取密码
     *
     * @return pwd
     */
    public String getPassString() {

        StringBuffer passString = new StringBuffer();

        for (String i : mPassList) {
            passString.append(i);
        }

        return passString.toString();
    }

    /**
     * 设置下一个View为输入状态
     */
    private void setNextInput(String pwdTxt) {
        if (inputIndex < maxLength) {
            setNoInput(inputIndex, true, pwdTxt);
            inputIndex++;
            PasswordView passwordView = (PasswordView) getChildAt(inputIndex);
            if (passwordView != null) {
                passwordView.setmPassText(pwdTxt + "");
                passwordView.startInputState();
            }
        }

    }

    /**
     * 设置上一个View为输入状态
     */
    private void setPreviosInput() {
        if (inputIndex > 0) {
            setNoInput(inputIndex, false, "");
            inputIndex--;
            PasswordView passWordView = (PasswordView) getChildAt(inputIndex);
            if (passWordView != null) {
                passWordView.setmPassText("");
                passWordView.startInputState();
            }
        } else if (inputIndex == 0) {
            PasswordView passWordView = (PasswordView) getChildAt(inputIndex);
            if (passWordView != null) {
                passWordView.setmPassText("");
                passWordView.startInputState();
            }
        }
    }

    /**
     * 设置指定View为不输入状态
     *
     * @param index   view下标
     * @param isinput 是否输入过密码
     */
    public void setNoInput(int index, boolean isinput, String txt) {
        if (index < 0) {
            return;
        }
        PasswordView passwordView = (PasswordView) getChildAt(index);
        if (passwordView != null) {
            passwordView.setmPassText(txt);
            passwordView.updateInputState(isinput);
        }
    }


    public interface pwdChangeListener {
        void onChange(String pwd);//密码改变

        void onNull();  //密码删除为空

        void onFinished(String pwd);//密码长度已经达到最大值
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;          //显示数字键盘
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        return new ZanyInputConnection(this, false);
    }


    private class ZanyInputConnection extends BaseInputConnection {

        @Override
        public boolean commitText(CharSequence txt, int newCursorPosition) {
            return super.commitText(txt, newCursorPosition);
        }

        public ZanyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }


    /**
     * 按键监听器
     */
    class MyKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.isShiftPressed()) {//处理*#等键
                    return false;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {//处理数字
                    addPwd(keyCode - 7 + "");              //点击添加密码
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_DEL) {       //点击删除
                    removePwd();
                    return true;
                }

                if (isSoftInputVisible((BaseActivity) mContext)) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
            }
            return false;
        }//onKey
    }


    //恢复状态
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mPassList = savedState.saveString;
        inputIndex = mPassList.size();

        if (mPassList.isEmpty()) {
            return;
        }

        for (int i = 0; i < getChildCount(); i++) {
            PasswordView passwordView = (PasswordView) getChildAt(i);
            if (i > mPassList.size() - 1) {
                if (passwordView != null) {
                    passwordView.setmIsShowRemindLine(false);
                    passwordView.updateInputState(false);
                }
                break;
            }

            if (passwordView != null) {
                passwordView.setmPassText(mPassList.get(i));
                passwordView.updateInputState(true);
            }
        }

    }

    //保存状态
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.saveString = this.mPassList;
        return savedState;
    }


    public static class SavedState extends BaseSavedState {
        public List<String> saveString;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            if (saveString != null)
                dest.writeList(saveString);
        }

        private SavedState(Parcel in) {
            super(in);
            if (saveString != null)
                in.readStringList(saveString);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
