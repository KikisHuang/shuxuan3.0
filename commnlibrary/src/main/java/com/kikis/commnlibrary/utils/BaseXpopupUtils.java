package com.kikis.commnlibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.biz.BaseOnConfirmListener;
import com.kikis.commnlibrary.dialog.BaseActionSheetPopupView;
import com.kikis.commnlibrary.view.BaseInputConfirmPopupView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;

import static com.kikis.commnlibrary.utils.CommonUtils.getc;
import static com.kikis.commnlibrary.utils.CommonUtils.gets;

/**
 * @author: Kikis
 * @date: 2020/12/7
 * @page:
 */
public class BaseXpopupUtils {


    /**
     * 一次性通用弹窗
     */
    public static void showBaseEdConfirmXpopupDialog(Context context, CharSequence title, CharSequence edcontent, CharSequence hint, final int edInputType, BaseOnConfirmListener baseOnConfirmListener) {

        new XPopup.Builder(context)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(false)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated(BasePopupView popupView) {
                        super.onCreated(popupView);
                        if (edInputType != 0)
                            ((BaseInputConfirmPopupView) popupView).getEditText().setInputType(edInputType);
                    }
                })
                .autoFocusEditText(true) //是否让弹窗内的EditText自动获取焦点，默认是true
                .asCustom(new BaseInputConfirmPopupView(context, title, edcontent, hint, baseOnConfirmListener)).show();
    }


    /**
     * 默认的确定弹窗方法
     *
     * @param context
     * @param onConfirmListener
     */
    public static void showConfirmPopupView(Context context, String content, OnConfirmListener onConfirmListener, OnCancelListener cancelListener) {
        ConfirmPopupView popupView = new XPopup.Builder(context)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated(BasePopupView popupView) {
                        super.onCreated(popupView);
                        TextView titleTv = ((ConfirmPopupView) popupView).getTitleTextView();
                        titleTv.setTextColor(getc(R.color.black));
                        titleTv.setTextSize(17);
                        titleTv.getPaint().setFakeBoldText(true);


                        TextView contentTv = ((ConfirmPopupView) popupView).getContentTextView();
                        contentTv.setTextColor(getc(R.color.gray_33));
                        contentTv.setTextSize(13);


                        TextView confirmTv = ((ConfirmPopupView) popupView).getConfirmTextView();
                        confirmTv.setTextColor(getc(R.color.blue_dominant_tone));
                        confirmTv.setTextSize(13);

                        confirmTv.getPaint().setFakeBoldText(true);

                        TextView cancelTv = ((ConfirmPopupView) popupView).getCancelTextView();
                        cancelTv.setTextColor(getc(R.color.text_secondary));
                        cancelTv.setTextSize(13);
                    }

                    @Override
                    public void onShow(BasePopupView popupView) {

                        super.onShow(popupView);
                    }
                })
                .asConfirm(gets(R.string.hint), content,
                        gets(R.string.cancel), gets(R.string.confirm), onConfirmListener, cancelListener, false);

        popupView.show();
    }
}
