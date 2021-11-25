package com.gxdingo.sg.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gxdingo.sg.R;
import com.gxdingo.sg.bean.BusinessDistrictMessageCommentListBean;
import com.gxdingo.sg.utils.EmotionsUtils;
import com.kikis.commnlibrary.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商圈消息-回复内容适配器
 *
 * @author JM
 */
public class BusinessDistrictMessageReplyContentAdapter extends BaseQuickAdapter<BusinessDistrictMessageCommentListBean.Reply, BaseViewHolder> {

    public BusinessDistrictMessageReplyContentAdapter() {
        super(R.layout.module_item_business_district_message_reply_content);
        addChildClickViewIds(R.id.ll_message_reply_content_layout);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, BusinessDistrictMessageCommentListBean.Reply reply) {
        ((TextView) baseViewHolder.findView(R.id.tv_reply_content)).setText(contentConversion(reply.getContent()));
    }

    /**
     * 内容转换，让其能显示表情图片
     *
     * @param content
     * @return
     */
    private SpannableStringBuilder contentConversion(String content) {
        //通过正则表达式将消息内容里面的表情标签转换成图片
        CharSequence text = content;
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        String rexgString = "(\\[(.*?)])";//表示[]中括号里面任意内容的都视为表情，含中括号
        Pattern pattern = Pattern.compile(rexgString);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int emotionResId = EmotionsUtils.getValue(matcher.group());
            if (emotionResId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), emotionResId);
                int bitmapWH = (int) getContext().getResources().getDimension(R.dimen.emoji_size);
                Bitmap newBitmap = BitmapUtils.updateBitmapWidthAndHeight(bitmap, bitmapWH, bitmapWH);
                builder.setSpan(new ImageSpan(getContext(), newBitmap), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }
}
