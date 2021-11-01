package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.lxj.xpopup.core.BottomPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/19
 * @page:
 */
public class ClientCallPhoneDialog extends BottomPopupView {

    @BindView(R.id.phone_number_tv)
    public TextView phone_number_tv;


    private String mPhoneNumber;

    public ClientCallPhoneDialog(@NonNull Context context,String phone) {
        super(context);
        this.mPhoneNumber = phone;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_client_call_phone;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this, bottomPopupContainer);
        if (!isEmpty(mPhoneNumber))
            phone_number_tv.setText(mPhoneNumber);
    }

    @OnClick({R.id.btn_cancel,R.id.phone_number_tv})
    public void onClickViews(View v){
        switch (v.getId()){
            case R.id.btn_cancel:
                this.dismiss();
                break;
            case R.id.phone_number_tv:

                break;
        }
    }
}
