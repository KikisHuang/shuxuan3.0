package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMSelectSendAddressAdapter;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IM-选择发送地址弹出窗口
 *
 * @author JM
 */
public class IMSelectSendAddressPopupView extends BottomPopupView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_use_another_address)
    TextView tvUseAnotherAddress;//"使用其他地址"和"没有收货地址 去添加"共用，改变字体演示即可
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    Context mContext;
    IMSelectSendAddressAdapter mAdapter;
    OnSendAddressListener mOnSendAddressListener;


    public interface OnSendAddressListener {
        void address(Object object);
    }

    @Override
    protected void onCreate() {

    }

    public IMSelectSendAddressPopupView(@NonNull Context context, OnSendAddressListener listener) {
        super(context);
        mContext = context;
        mOnSendAddressListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_im_select_send_address_popup;
    }


    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this);
        mAdapter = new IMSelectSendAddressAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                mOnSendAddressListener.address(null);
                dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
        List<Object> tempData = new ArrayList<>();
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        tempData.add(new Object());
        mAdapter.setList(tempData);

        tvUseAnotherAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
