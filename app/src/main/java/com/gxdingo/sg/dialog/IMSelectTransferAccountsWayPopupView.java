package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.adapter.IMSelectTransferAccountsWayAdapter;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IM-选择转账方式弹出窗口
 *
 * @author JM
 */
public class IMSelectTransferAccountsWayPopupView extends BottomPopupView {


    Context mContext;
    IMSelectTransferAccountsWayAdapter mAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_more_pay_methods)
    RelativeLayout rlMorePayMethods;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    OnTransferAccountsWayListener mOnTransferAccountsWayListener;

    public interface OnTransferAccountsWayListener {
        void way(int p);
    }

    @Override
    protected void onCreate() {

    }

    public IMSelectTransferAccountsWayPopupView(@NonNull Context context, OnTransferAccountsWayListener listener) {
        super(context);
        mContext = context;
        mOnTransferAccountsWayListener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_im_select_transfer_accounts_way_popup;
    }


    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this);
        mAdapter = new IMSelectTransferAccountsWayAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (mOnTransferAccountsWayListener != null) {
                    if (position == 0)
                        mOnTransferAccountsWayListener.way(20);
                    else if (position == 1)
                        mOnTransferAccountsWayListener.way(10);
                    else
                        mOnTransferAccountsWayListener.way(30);
                }

                dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
        List<TransferAccountsWay> datas = new ArrayList<>();
        datas.add(new TransferAccountsWay(R.drawable.module_svg_transfer_accounts_way_alipay_8703, "支付宝支付"));
        datas.add(new TransferAccountsWay(R.drawable.module_svg_transfer_accounts_way_wechat_8704, "微信支付"));
        mAdapter.setList(datas);

        rlMorePayMethods.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.add(new TransferAccountsWay(R.drawable.module_svg_transfer_accounts_way_shuxuan_8705, "钱包余额支付"));
                mAdapter.setList(datas);
                rlMorePayMethods.setVisibility(GONE);
            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public class TransferAccountsWay {
        public int icon;
        public String name;

        public TransferAccountsWay(int icon, String name) {
            this.icon = icon;
            this.name = name;
        }
    }
}
