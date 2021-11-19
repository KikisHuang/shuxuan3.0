package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;
import com.gxdingo.sg.R;
import com.gxdingo.sg.biz.OnContentListener;
import com.gxdingo.sg.view.RegexEditText;
import com.lxj.xpopup.core.CenterPopupView;

import static com.gxdingo.sg.utils.LocalConstant.FIRST_INTER_KEY;


/**
 * @author: Weaving
 * @date: 2021/11/19
 * @page:
 */
public class FillInvitationCodePopupView extends CenterPopupView implements View.OnClickListener {

    private ImageView close_iv;

    private RegexEditText invitation_code_et;

    private TextView btn_confirm;

    private OnContentListener listener;

    public FillInvitationCodePopupView(@NonNull Context context,OnContentListener listener) {
        super(context);
        this.listener = listener;
        addInnerContent();

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_invitation_code;
    }



    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        close_iv = findViewById(R.id.close_iv);
        invitation_code_et = findViewById(R.id.invitation_code_et);
        btn_confirm = findViewById(R.id.btn_confirm);
        close_iv.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_iv:
                SPUtils.getInstance().put(FIRST_INTER_KEY, false);
                dismiss();
                break;
            case R.id.btn_confirm:
                if (listener!=null)
                    listener.onConfirm(this,invitation_code_et.getText().toString());
                break;
        }
    }
}
