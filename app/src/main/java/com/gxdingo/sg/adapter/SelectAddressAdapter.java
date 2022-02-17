package com.gxdingo.sg.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * @author: Weaving
 * @date: 2021/10/17
 * @page:
 */
public class SelectAddressAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {

    private String keyWord;

    private int mCheckPos = -1;


    public SelectAddressAdapter() {
        super(R.layout.module_item_select_address);
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void checkPost(int pos) {
        mCheckPos = pos;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PoiItem poiItem) {
        TextView tvTitle = baseViewHolder.getView(R.id.tv_district);
        ImageView check_img = baseViewHolder.getView(R.id.check_img);


        if (!isEmpty(keyWord))
            tvTitle.setText(isEmpty(poiItem.getTitle()) ? "" : setTextHighLight(poiItem.getTitle(), new String[]{keyWord}));
        else
            tvTitle.setText(isEmpty(poiItem.getTitle()) ? "" : poiItem.getTitle());

        baseViewHolder.setText(R.id.tv_district, isEmpty(poiItem.getTitle()) ? "" : setTextHighLight(poiItem.getTitle(), new String[]{"银行"}));
        baseViewHolder.setText(R.id.tv_details_address, isEmpty(poiItem.getSnippet()) ? "" : poiItem.getSnippet());

        if (poiItem.getDistance() > 0) {
            if (poiItem.getDistance() < 1000)
                baseViewHolder.setText(R.id.tv_distance, " | " + poiItem.getDistance() + "m");
            else
                baseViewHolder.setText(R.id.tv_distance, " | " + poiItem.getDistance() + "km");
        } else {
            baseViewHolder.setText(R.id.tv_distance, "");
        }

        check_img.setVisibility(getItemPosition(poiItem) == mCheckPos ? View.VISIBLE : View.GONE);

    }

    /**
     * 设置文字高亮
     *
     * @param text
     * @param keyWord
     */
    private SpannableString setTextHighLight(String text, String[] keyWord) {

        SpannableString s = new SpannableString(text);
        for (int i = 0; i < keyWord.length; i++) {
            Pattern p = Pattern.compile(keyWord[i]);
            Matcher m = p.matcher(s);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFC600")), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return s;

    }
}
