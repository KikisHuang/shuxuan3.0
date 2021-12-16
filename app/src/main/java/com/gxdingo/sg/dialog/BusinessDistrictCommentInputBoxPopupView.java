package com.gxdingo.sg.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gxdingo.sg.R;
import com.gxdingo.sg.fragment.IMEmotionFragment;
import com.gxdingo.sg.fragment.IMEmotionItemFragment;
import com.kikis.commnlibrary.utils.BitmapUtils;
import com.kikis.commnlibrary.utils.SystemUtils;
import com.lxj.xpopup.core.BottomPopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blankj.utilcode.util.KeyboardUtils.hideSoftInput;

/**
 * 商圈评论输入框弹出窗口
 *
 * @author JM (弃用)
 */
public class BusinessDistrictCommentInputBoxPopupView extends BottomPopupView {

    Context mContext;
    //    IMSelectSendAddressAdapter mAdapter;
    OnCommentContentListener mOnCommentContentListener;
    @BindView(R.id.et_content_input_box)
    EditText etContentInputBox;
    @BindView(R.id.iv_expression)
    ImageView ivExpression;
    @BindView(R.id.btn_send_info)
    Button btnSendInfo;
    @BindView(R.id.ll_main_function_menu)
    LinearLayout llMainFunctionMenu;
    @BindView(R.id.rl_child_function_menu_layout)
    RelativeLayout rlChildFunctionMenuLayout;
    @BindView(R.id.cl_input_content)
    ConstraintLayout clInputContent;


    String mHint = "";
    IMEmotionFragment mIMEmotionFragment;//菜单-表情

    FragmentManager mFragmentManager;
    ArrayList<Fragment> mFragments = new ArrayList<>();
    int mCurrClickFunctionMenuId;//当前点击的主功能菜单ID


    public interface OnCommentContentListener {
        void commentContent(Object object);
    }


    public BusinessDistrictCommentInputBoxPopupView(@NonNull Context context, String hint, FragmentManager fragmentManager, OnCommentContentListener listener) {
        super(context);
        mContext = context;
        mHint = hint;
        mFragmentManager = fragmentManager;
        mOnCommentContentListener = listener;

        //监听全局布局（用来监听软键盘显示和关闭）
        ((Activity) mContext).getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_business_district_comment_input_box_fm_dialog;
    }


    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this);
//        SystemUtils.showKeyboard(mContext);
        etContentInputBox.setFocusable(true);
        etContentInputBox.setFocusableInTouchMode(true);
        etContentInputBox.requestFocus();
        etContentInputBox.findFocus();

        EventBus.getDefault().register(this);
        etContentInputBox.setHint(mHint);
        //表情功能
        mIMEmotionFragment = new IMEmotionFragment();
        mFragments.add(mIMEmotionFragment);
    }

    @OnClick({R.id.et_content_input_box, R.id.iv_expression, R.id.btn_send_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_content_input_box:
                mCurrClickFunctionMenuId = 0;
                break;
            case R.id.iv_expression:
                onExpressionClick(view);
                break;
            case R.id.btn_send_info:
                if (mOnCommentContentListener != null)
                    mOnCommentContentListener.commentContent(etContentInputBox.getText().toString());
                break;
        }
    }

    boolean mIsDirectlyClosed = false;//是否直接关闭窗口（不考虑表情正在显示）

    /**
     * 直接关闭窗口
     */
    public void directlyDismiss() {
        mIsDirectlyClosed = true;
        mFragmentManager = null;
        mIMEmotionFragment = null;
        super.dismiss();
    }

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        //删除全局布局侦听器
        ((Activity) mContext).getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
//        SystemUtils.hideKeyboard(etContentInputBox);
        super.dismiss();
      /*  if (menuFunctionFragmentIsVisible(mIMEmotionFragment)) {
            hiddenMenuFunctionFragment();
            //传0表示只恢复原来的UI
            restoreUIAndChangeCurBtnIcon(0);
            return;
        } else if (SystemUtils.getSoftInputHeight(mContext) > 0) {
            SystemUtils.hideKeyboard(etContentInputBox);
            return;
        } else {
            //删除全局布局侦听器
            ((Activity) mContext).getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
            EventBus.getDefault().unregister(this);
            SystemUtils.hideKeyboard(etContentInputBox);
            super.dismiss();
        }*/
    }

    /**
     * 全局布局侦听器,通过布局改变来监听软键盘高度改变(软键盘打开和关闭时回调)
     */
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            //当点击表情按钮的时候触发了软键盘弹出或关闭操作时不执行下面代码
            if (mCurrClickFunctionMenuId == R.id.iv_expression) {
                return;
            }

            //传0表示只恢复原来的UI
            restoreUIAndChangeCurBtnIcon(0);
            //表情Fragment显示的就隐藏掉所有的菜单Fragment
            if (menuFunctionFragmentIsVisible(mIMEmotionFragment)) {
                hiddenMenuFunctionFragment();
            }
        }
    };

    /**
     * 接收event事件
     *
     * @param object
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object object) {
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
    }

    /**
     * 恢复原来的UI，并且改变当前按钮图标
     *
     * @param id 当前点中的按钮，传0表示只恢复原来的UI
     */
    private void restoreUIAndChangeCurBtnIcon(int id) {
        //先恢复所有按钮的原来图标
        ivExpression.setImageResource(R.drawable.module_svg_im_expression);//表情按钮

        //只有下面的按钮才可以改变图标（改成软键盘图标）
        if (id == R.id.iv_expression) {
            //再将被点中的按钮图标改成软键盘图标
            ImageView imageView = findViewById(id);
            imageView.setImageResource(R.drawable.module_svg_im_soft_keyboard);
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
     * 主要功能菜单-点击表情按钮
     */
    private void onExpressionClick(View view) {
        if (menuFunctionFragmentIsHidden(mIMEmotionFragment)) {
            mCurrClickFunctionMenuId = R.id.iv_expression;
            SystemUtils.hideKeyboard(view);
            etContentInputBox.postDelayed(new Runnable() {
                @Override
                public void run() {
                    etContentInputBox.requestFocus();
                    //恢复原来的UI，并改变表情按钮图标
                    restoreUIAndChangeCurBtnIcon(R.id.iv_expression);
                    showMenuFunctionFragment(mIMEmotionFragment);
                    //恢复输入发送内容布局在原来的位置（高度）
                    restoreInputContentLayoutPositionHeight();
                }
            }, 100);

        } else if (menuFunctionFragmentIsVisible(mIMEmotionFragment)) {
            hiddenMenuFunctionFragment();
            //传0表示只恢复原来的UI
            restoreUIAndChangeCurBtnIcon(0);
            SystemUtils.showKeyboard(mContext);
        }
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

    public View getView() {
        return bottomPopupContainer;
    }

    /**
     * 显示菜单功能Fragment
     *
     * @param fragment 要显示的Fragment
     */
    private void showMenuFunctionFragment(Fragment fragment) {
        hiddenMenuFunctionFragment();
        if (fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.add(R.id.rl_child_function_menu_layout, fragment, "fragmentTagStr");
                mFragmentManager.executePendingTransactions();
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
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                if (fragment.isAdded()) {
                    ft.hide(fragment);
                }
                ft.commitAllowingStateLoss();
            }
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
        ImageSpan imageSpan = new ImageSpan(mContext, newBitmap);
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
}
