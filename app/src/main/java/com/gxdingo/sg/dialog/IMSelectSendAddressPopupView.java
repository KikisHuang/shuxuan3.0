package com.gxdingo.sg.dialog;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.ClientAddressListActivity;
import com.gxdingo.sg.adapter.IMSelectSendAddressAdapter;
import com.kikis.commnlibrary.bean.AddressBean;
import com.lxj.xpopup.core.BottomPopupView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;

/**
 * IM-选择发送地址弹出窗口
 *
 * @author JM
 */
public class IMSelectSendAddressPopupView extends BottomPopupView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_use_another_address)
    TextView tvUseAnotherAddress;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.no_address_tv)
    TextView no_address_tv;

    @BindView(R.id.line_view)
    View line_view;

    Context mContext;
    IMSelectSendAddressAdapter mAdapter;
    OnSendAddressListener mOnSendAddressListener;

    private List<AddressBean> list;

    public interface OnSendAddressListener {
        void address(Object object);
    }

    @Override
    protected void onCreate() {

    }

    public IMSelectSendAddressPopupView(@NonNull Context context, List<AddressBean> list, OnSendAddressListener listener) {
        super(context);
        mContext = context;
        this.list = list;
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

        String str2 = "没有收货地址 <font color=\"#009dff\">去添加</font>";

        no_address_tv.setText(Html.fromHtml(str2));
        no_address_tv.setVisibility(list == null || list.size() <= 0 ? VISIBLE : GONE);

        tvUseAnotherAddress.setVisibility(list == null || list.size() <= 0 ? GONE : VISIBLE);
        line_view.setVisibility(list == null || list.size() <= 0 ? GONE : VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setList(list);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            AddressBean item = (AddressBean) adapter.getItem(position);
            item.selectType = 2;
            EventBus.getDefault().post(item);
            dismiss();
        });

        tvUseAnotherAddress.setOnClickListener(v -> {
            goToPagePutSerializable(mContext, ClientAddressListActivity.class, getIntentEntityMap(new Object[]{2}));
            dismiss();
        });
        no_address_tv.setOnClickListener(v -> {
            goToPagePutSerializable(mContext, ClientAddressListActivity.class, getIntentEntityMap(new Object[]{2}));
            dismiss();
        });
        tvCancel.setOnClickListener(v -> dismiss());
    }
}
