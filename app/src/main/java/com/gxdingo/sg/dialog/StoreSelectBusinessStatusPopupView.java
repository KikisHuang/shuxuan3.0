package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.lxj.xpopup.core.BottomPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商家选择营业状态弹出窗口
 *
 * @author JM
 */
public class StoreSelectBusinessStatusPopupView extends BottomPopupView {

    Context mContext;
    OnBusinessStatusListener mOnBusinessStatusListener;
    @BindView(R.id.tv_normal_business)
    TextView tvNormalBusiness;
    @BindView(R.id.tv_suspend_business)
    TextView tvSuspendBusiness;


    public interface OnBusinessStatusListener {
        void onStatus(int code, String name);
    }

    @Override
    protected void onCreate() {

    }

    public StoreSelectBusinessStatusPopupView(@NonNull Context context, OnBusinessStatusListener listener) {
        super(context);
        mContext = context;
        mOnBusinessStatusListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_store_select_business_status;
    }


    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this);


    }

    @OnClick({R.id.tv_normal_business, R.id.tv_suspend_business})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_normal_business:
                if (mOnBusinessStatusListener != null) {
                    mOnBusinessStatusListener.onStatus(1, "正常营业");//正常营业
                    dismiss();
                }
                break;
            case R.id.tv_suspend_business:
                if (mOnBusinessStatusListener != null) {
                    mOnBusinessStatusListener.onStatus(0, "暂停营业");//暂停营业
                    dismiss();
                }
                break;
        }
    }
}
