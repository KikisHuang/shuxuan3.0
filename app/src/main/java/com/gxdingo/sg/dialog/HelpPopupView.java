package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.HelpBean;
import com.lxj.xpopup.core.CenterPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/12/2
 * @page:
 */
public class HelpPopupView extends CenterPopupView {

    @BindView(R.id.title_tv)
    public TextView title_tv;

    @BindView(R.id.avatar_img)
    public ImageView avatar_img;

    @BindView(R.id.subTitle_tv)
    public TextView subTitle_tv;

    @BindView(R.id.btn_help)
    public TextView btn_help;

    private HelpBean helpBean;

    public HelpPopupView(@NonNull Context context,HelpBean helpBean) {
        super(context);
        this.helpBean = helpBean;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_help;
    }

    @OnClick(R.id.btn_help)
    public void OnViewsClick(View v){
        switch (v.getId()){
            case R.id.btn_help:
                break;
        }
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this, this);
        if (helpBean!=null){
            title_tv.setText(helpBean.getTitle());
            Glide.with(this).load(helpBean.getImage()).into(avatar_img);
            if (!isEmpty(helpBean.getSubtitle())) {
                subTitle_tv.setText(helpBean.getSubtitle());
                btn_help.setText("帮忙助力");
            }else {
                subTitle_tv.setVisibility(GONE);
                btn_help.setText("一键免费抽奖");
            }
        }
    }
}
