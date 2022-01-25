
package com.gxdingo.sg.adapter;

import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BankcardBean;
import com.kikis.commnlibrary.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Kikis
 * @date: 2022/1/24
 * @page:
 */
public class DialogBankcardAdapter extends BaseQuickAdapter<BankcardBean, BaseViewHolder> {


    //回调选择的银行卡id
    private int bankCardId;

    public DialogBankcardAdapter(int id) {
        super(R.layout.module_item_cash_select);
        addChildClickViewIds(R.id.bankcard_stv);
        this.bankCardId = id;
    }

    public void setBankCardId(int id) {
        this.bankCardId = id;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BankcardBean bankcardBean) {

        SuperTextView bankcard_stv = baseViewHolder.getView(R.id.bankcard_stv);

        if (!isEmpty(bankcardBean.getName()))
            bankcard_stv.getLeftTextView().setText(bankcardBean.getName());

        if (!isEmpty(bankcardBean.getIcon()))
            Glide.with(getContext()).load(bankcardBean.getIcon()).apply(GlideUtils.getInstance().getDefaultOptions()).into(bankcard_stv.getLeftIconIV());


        bankcard_stv.setRightIcon(bankCardId == bankcardBean.getId() ? R.drawable.module_svg_cash_selecte : 0);

    }
}
