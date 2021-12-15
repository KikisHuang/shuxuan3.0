package com.kikis.commnlibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.kikis.commnlibrary.R;
import com.kikis.commnlibrary.utils.GlideUtils;
import com.kikis.commnlibrary.utils.KikisUitls;

import static android.text.TextUtils.isEmpty;
import static com.blankj.utilcode.util.BarUtils.getStatusBarHeight;
import static com.blankj.utilcode.util.SizeUtils.dp2px;


/**
 * @author: Kikis
 * @date: 2021/2/26
 * @page:自定义消息弹窗view
 */
public class BaseMessageLayout extends FrameLayout {

    private ConstraintLayout message_layout;

    private TextView name_tv;

    private TextView content_tv;

    private ImageView avatar_img;

    private Handler mHandler;

    public BaseMessageLayout(@NonNull Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.module_dialog_base_message, this);

        name_tv = findViewById(R.id.name_tv);

        content_tv = findViewById(R.id.content_tv);

        avatar_img = findViewById(R.id.avatar_img);

        message_layout = findViewById(R.id.message_layout);


        FrameLayout.LayoutParams lp = (LayoutParams) message_layout.getLayoutParams();
        //todo 待优化判断是否有刘海
//        lp.setMargins(0, getStatusBarHeight() + ConvertUtils.dp2px(5), 0, 0);

    }


    public BaseMessageLayout setTitle(String title) {
        if (!isEmpty(title))
            name_tv.setText(title);
        return this;
    }

    public BaseMessageLayout setContent(String content) {
        if (!isEmpty(content))
            content_tv.setText(content);
        return this;
    }

    public BaseMessageLayout setHandler(Handler handler) {
        mHandler = handler;
        return this;
    }


    public BaseMessageLayout setAvatar(String avatar) {
        Glide.with(KikisUitls.getContext()).load(!isEmpty(avatar) ? avatar : R.mipmap.ic_default_avatar).apply(GlideUtils.getInstance().getDefaultOptions()).into(avatar_img);
        return this;
    }

    public BaseMessageLayout setClickListener(OnClickListener onClickListener) {
        if (onClickListener != null)
            message_layout.setOnClickListener(onClickListener);
        return this;
    }

    public void show() {

        Animation animation = AnimationUtils.loadAnimation(KikisUitls.getContext(), R.anim.actionsheet_dialog_top_in);
        message_layout.setAnimation(animation);
        message_layout.startAnimation(animation);
        message_layout.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mHandler != null) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendMessage(new Message());
                        }
                    }, 2000);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
