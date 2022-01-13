package com.gxdingo.sg.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.gxdingo.sg.R;
import com.lxj.xpopup.core.BottomPopupView;


/**
 * @author: Kikis
 * @date: 2022/1/13
 * @page: 按需求自定义的底部弹窗 BaseActionSheetPopupView
 */
public class InviteFriendsActionSheetPopupView extends BottomPopupView implements View.OnClickListener {

    private View.OnClickListener clickListener;

    public InviteFriendsActionSheetPopupView(@NonNull Context context, View.OnClickListener clickListener) {
        super(context);
        addInnerContent();
        this.clickListener = clickListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.module_dialog_invite_friends;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        findViewById(R.id.copy_invite_friends_ll).setOnClickListener(this);
        findViewById(R.id.share_wechat_ll).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (clickListener != null)
            clickListener.onClick(v);

        dismiss();
    }

}
