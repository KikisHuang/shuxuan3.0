package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.gxdingo.sg.R;
import com.lxj.xpopup.core.CenterPopupView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 一句话提示
 *
 * @author: JM
 * @date: 2021/10/24
 */
public class OneSentenceHintPopupView extends CenterPopupView implements View.OnClickListener {

    @BindView(R.id.close_img)
    ImageView closeImg;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    String mTitle;

    public OneSentenceHintPopupView(@NonNull Context context, String title) {
        super(context);
        mTitle = title;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_one_sentence_hint;
    }

    @Override
    protected int getMaxWidth() {
        return (int) (ScreenUtils.getScreenWidth() * 2.6 / 3);
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        ButterKnife.bind(this);
        tvTitle.setText(mTitle);
        closeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}
