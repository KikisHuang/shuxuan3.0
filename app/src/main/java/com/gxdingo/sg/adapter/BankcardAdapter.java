package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BankcardBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Weaving
 * @date: 2021/10/22
 * @page:
 */
public class BankcardAdapter extends BaseQuickAdapter<BankcardBean, BaseViewHolder> {

    private int mType;

    public BankcardAdapter(int type) {
        super(type==0?R.layout.module_recycle_item_select_bankcard:R.layout.module_recycle_item_bank_card);
        mType = type;

        if (type == 1)
            addChildClickViewIds(R.id.btn_unbind);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BankcardBean bankcardBean) {
        if (mType == 0){
            ImageView bankLogo = baseViewHolder.getView(R.id.iv_bank_logo);
            Glide.with(bankLogo)
                    .applyDefaultRequestOptions(GlideUtils.getInstance().getCircleCrop())
                    .load(bankcardBean.getIcon())
                    .into(bankLogo);
            baseViewHolder.setText(R.id.tv_bank_name,bankcardBean.getName()+"("+bankcardBean.getBankType()+")");
        }else {
            ImageView bankLogo=baseViewHolder.getView(R.id.iv_bank_logo);
            Glide.with(bankLogo)
                    .applyDefaultRequestOptions(GlideUtils.getInstance().getCircleCrop())
                    .load(bankcardBean.getIcon())
                    .into(bankLogo);
            baseViewHolder.setText(R.id.tv_bank_name,bankcardBean.getName());
            baseViewHolder.setText(R.id.tv_bank_card_type,bankcardBean.getCardName());
            baseViewHolder.setText(R.id.tv_bank_card_number,bankcardBean.getNumber());
        }

    }
}
