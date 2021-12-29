package com.gxdingo.sg.dialog;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gxdingo.sg.R;
import com.gxdingo.sg.activity.WebActivity;
import com.gxdingo.sg.bean.HelpBean;
import com.gxdingo.sg.biz.HelpListener;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.lxj.xpopup.core.CenterPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kikis.commnlibrary.utils.IntentUtils.getIntentEntityMap;
import static com.kikis.commnlibrary.utils.IntentUtils.goToPagePutSerializable;
import static com.kikis.commnlibrary.utils.StringUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/12/2
 * @page:
 */
public class HelpPopupView extends CenterPopupView {

    @BindView(R.id.help_type_title_tv)
    public TextView help_type_title_tv;

    @BindView(R.id.title_tv)
    public TextView title_tv;

    @BindView(R.id.avatar_img)
    public ImageView avatar_img;

    @BindView(R.id.subTitle_tv)
    public TextView subTitle_tv;

    @BindView(R.id.btn_help)
    public TextView btn_help;

    private HelpBean helpBean;

    private HelpListener listener;
    //0 为好友助力、1 助力成功
//    private int type;

    public HelpPopupView(@NonNull Context context,HelpBean helpBean,HelpListener listener) {
        super(context);

        this.helpBean = helpBean;
        this.listener = listener;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_help;
    }

    @OnClick({R.id.btn_help,R.id.btn_close})
    public void OnViewsClick(View v){
        switch (v.getId()){
            case R.id.btn_help:
                if (helpBean==null)
                    return;
                if (helpBean.getType() == 0){
                    if (listener!=null)
                        listener.help();
                }else {
                    goToPagePutSerializable(getContext(), WebActivity.class, getIntentEntityMap(new Object[]{false, helpBean.getButtonToUrl()}));
                }
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        ButterKnife.bind(this, this);

        if (helpBean!=null){
            title_tv.setText(helpBean.getTitle().replace("\\n", "\n"));
            if (helpBean.getType() == 0){
                help_type_title_tv.setText("为好友助力");
                subTitle_tv.setText(helpBean.getSubtitle());
                btn_help.setText("帮忙助力");
                Glide.with(this).load(helpBean.getUserAvatar()).apply(GlideUtils.getInstance().getCircleCrop()).into(avatar_img);
            } else{
                help_type_title_tv.setText("助力成功");
                Glide.with(this).load(helpBean.getImage()).apply(GlideUtils.getInstance().getCircleCrop()).into(avatar_img);
                subTitle_tv.setVisibility(GONE);
                btn_help.setText("一键免费抽奖");
            }
        }
    }
}
