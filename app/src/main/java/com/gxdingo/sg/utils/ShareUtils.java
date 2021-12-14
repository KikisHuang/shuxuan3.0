package com.gxdingo.sg.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by Kikis on 2021/5/27.
 */

public class ShareUtils {

    /**
     * 分享Web链接
     *
     * @param context
     * @param umShareListener
     * @param type            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN_CIRCLE
     * @param url
     * @param title
     * @param description
     * @param thumb
     */
    public static void UmShare(Context context, UMShareListener umShareListener, String url, String title, String description, Object thumb, SHARE_MEDIA... type) {

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        if (thumb instanceof String) {
            web.setThumb(new UMImage(context, String.valueOf(thumb)));  //缩略图
        } else if (thumb instanceof Bitmap) {
            web.setThumb(new UMImage(context, (Bitmap) thumb));  //缩略图
        }
        web.setDescription(description);//描述
        new ShareAction((Activity) context)
//                .setDisplayList(type)//传入平台
                .setPlatform(type[0])
                .withMedia(web)
                .setCallback(umShareListener)//回调监听器
                .share();
    }

    /**
     * 分享图片
     *
     * @param context
     * @param umShareListener
     * @param type            (0 all 1 wechat 2 weixin circle 3 sina)
     * @param bitmap
     */
    public static void UmShareImg(Context context, UMShareListener umShareListener, int type, Bitmap bitmap) {

        UMImage img = new UMImage(context, bitmap);
        UMImage thumb = new UMImage(context, bitmap);
        img.setThumb(thumb);

        SHARE_MEDIA sma = SHARE_MEDIA.QQ;

        if (type == 1)
            sma = SHARE_MEDIA.WEIXIN;
        else if (type == 4)
            sma = SHARE_MEDIA.QQ;
        else if (type == 3)
            sma = SHARE_MEDIA.SINA;
        else if (type == 2)
            sma = SHARE_MEDIA.WEIXIN_CIRCLE;

        if (type == 0) {
            new ShareAction((Activity) context)
                    .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN_CIRCLE)
                    .withMedia(img)
                    .setCallback(umShareListener)
                    .open();
        } else {
            new ShareAction((Activity) context)
                    .setPlatform(sma)//传入平台
                    .withMedia(img)
                    .setCallback(umShareListener)//回调监听器
                    .share();
        }
    }


}
