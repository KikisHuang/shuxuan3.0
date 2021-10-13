package com.kikis.commnlibrary.fragment.dialog;

import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.R2;
import com.kikis.commnlibrary.bean.EditDialogBean;
import com.kikis.commnlibrary.dialog.BaseFragmentDialog;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.ConvertUtils.dp2px;
import static com.blankj.utilcode.util.KeyboardUtils.clickBlankArea2HideSoftInput;
import static com.blankj.utilcode.util.KeyboardUtils.hideSoftInput;
import static com.blankj.utilcode.util.KeyboardUtils.isSoftInputVisible;
import static com.blankj.utilcode.util.KeyboardUtils.showSoftInput;
import static com.kikis.commnlibrary.utils.MyToastUtils.customToast;


/**
 * Created by Kikis on 2020/6/4.
 * edit弹窗fragment
 */

public class EditDialogFragment extends BaseFragmentDialog {


    public static final String EDIT_TITLE = "edit_title";
    public static final String EDIT_CONTENT = "edit_content";
    public static final String EDIT_HINT = "edit_hint";
    public static final String EDIT_INDEX = "edit_index";
    public static final String EDIT_TAG = "edit_tag";
    public static final String EDIT_INPUT_TYPE = "edit_input_type";

    public static final int INPUTTYPE_STR = 1;
    public static final int INPUTTYPE_INT = 3;
    public static final int INPUTTYPE_DOUBLE = 4;
    private int inputType;
    private int index = -1;

    public String tag = "";

    @BindView(R2.id.content_edt)
    public EditText content_edt;

    @BindView(R2.id.title_tv)
    public TextView title_tv;


    @Override
    protected boolean refreshEnable() {
        return false;
    }

    @Override
    protected boolean loadmoreEnable() {
        return false;
    }

    @Override
    public int initContentView() {
        return R.layout.module_fragment_dialog;
    }


    @Override
    protected void init() {

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        tag = args.getString(EDIT_TAG, "");
        String content = args.getString(EDIT_CONTENT, "");
        String title = args.getString(EDIT_TITLE, "");
        String hint = args.getString(EDIT_HINT, "");

                index = args.getInt(EDIT_INDEX, -1);


        inputType = args.getInt(EDIT_INPUT_TYPE, -1);

        title_tv.setText(title.isEmpty() ? "提示" : title);

        content_edt.setHint(hint.isEmpty() ? "请输入" : hint);

/*
        if (!content.isEmpty()) {
            content_edt.setText(content);
            content_edt.setSelection(content_edt.getText().length());
        }*/

    }


    @Override
    protected void initData() {
        editType();

    }

    private void editType() {
        if (inputType != -1) {
            switch (inputType) {
                case INPUTTYPE_INT:
                    content_edt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case INPUTTYPE_DOUBLE:
                    content_edt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
            }

        }
    }

    @Override
    protected boolean eventBusRegister() {
        return true;
    }


    @Override
    public int DialogHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int DialogWidth() {
        return dp2px(350);
    }

    @Override
    public int AnimStyle() {
        return R.style.ActionFadeAnimation;
    }

    @OnClick({R2.id.confirm_bt, R2.id.cancel_bt})
    public void onViewClicked(View v) {
        if (!checkClickInterval(v.getId()))
            return;
        int id = v.getId();
        if (id == R.id.confirm_bt) {
            if (!content_edt.getText().toString().isEmpty()) {

                EditDialogBean edb = new EditDialogBean(tag, content_edt.getText().toString());
                if (index != -1)
                    edb.index = index;

                sendEvent(edb);
                dismiss();
            } else
                customToast("请填写内容后再提交");

        } else if (id == R.id.cancel_bt) {
            dismiss();
        }
    }


    @Override
    public boolean OutCancel() {
        return true;
    }

    @Override
    public int ShowPosition() {
        return Gravity.CENTER;
    }

    @Override
    public float DimAmount() {
        return 0.6f;
    }

    @Override
    public int ParamsY() {
        return 0;
    }

}
