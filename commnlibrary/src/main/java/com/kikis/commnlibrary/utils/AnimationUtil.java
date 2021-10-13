package com.kikis.commnlibrary.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import static com.kikis.commnlibrary.utils.CommonUtils.getTAG;

/**
 * Created by lian on 2019/10/31.
 * 动画工具类;
 */
public class AnimationUtil {

    private static final String TAG = getTAG(AnimationUtil.class);


    //帧动画anim
    private AnimationDrawable refreshingAnim;
    //帧动画map
    public static Map<String, AnimationDrawable> animMap;

    public static AnimationUtil instance;

    public static AnimationUtil getInstance() {

        synchronized (AnimationUtil.class) {
            if (instance == null) {
                instance = new AnimationUtil();
                animMap = new HashMap<>();
            }
        }
        return instance;
    }

    public AnimationUtil() {

    }

    /**
     * 5.0揭露动画
     *
     * @param animateView
     */
    public void createCircularRevealAnimation(final View animateView) {
        // 显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /**
             * createCircularReveal 方法参数
             * view 执行动画的view
             * centerX 圆心横坐标
             * centerY 圆心纵坐标
             * startRadius 动画开始时圆的半径
             * endRadius 动画结束时圆的半径
             */
            final Animator animator = ViewAnimationUtils.createCircularReveal(animateView,
                    animateView.getWidth() / 2,
                    animateView.getHeight() / 2,
                    0,
                    (float) Math.hypot(animateView.getWidth(), animateView.getHeight()));
            // Math.hypot确定圆的半径（算长宽的斜边长，这样半径不会太短也不会很长效果比较舒服）

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animateView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(2000);
            animator.start();
        } else {
            animateView.setVisibility(View.VISIBLE);
        }
        animateView.setEnabled(true);
    }

    /**
     * 开启帧动画
     */
    public void startFrameAnimation(String tag, ImageView img, int resId) {
        img.setImageResource(resId);
        refreshingAnim = (AnimationDrawable) img.getDrawable();
        animMap.put(tag, refreshingAnim);
        refreshingAnim.start();
    }

    /**
     * 关闭帧动画
     */
    public void stopFrameAnimation(String flag) {
        if (refreshingAnim != null && refreshingAnim.isRunning() && animMap != null && animMap.containsKey(flag)) {
            for (Map.Entry<String, AnimationDrawable> entry : animMap.entrySet()) {
                String mapKey = entry.getKey();
                if (mapKey.equals(flag)) {
                    AnimationDrawable mapValue = entry.getValue();
                    mapValue.stop();
                    break;
                }
            }
            animMap.remove(flag);
        }
    }


    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }


    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到控件的顶部
     *
     * @return
     */
    public TranslateAnimation moveToViewTop() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 从控件的顶部移动到控件所在位置
     *
     * @return
     */
    public TranslateAnimation moveToViewLocation1() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(300);
        return mHiddenAction;
    }

    /**
     * 欢迎界面平移动画;
     *
     * @return
     */
    public TranslateAnimation WelcomeAnima() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -2.15f);
        mHiddenAction.setDuration(1000);
        mHiddenAction.setFillAfter(true);
        return mHiddenAction;
    }

    /**
     * 欢迎界面旋转动画;
     *
     * @return
     */

    public RotateAnimation WelcomeAnimatwo() {
        final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(1300);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * 欢迎界面缩放动画;
     * 从IMG中心放大;
     *
     * @return
     */

    public ScaleAnimation WelcomeAnimathree() {

        ScaleAnimation animation = new ScaleAnimation(
                1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(15000);
        return animation;
    }

    /**
     * 标题缩放动画;
     * 从view中心缩小;
     *
     * @return
     */
    public ScaleAnimation TitleZoomAnima(boolean tag) {

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 1, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //3秒完成动画
        scaleAnimation.setDuration(200);
        return scaleAnimation;
    }

    /**
     * 闪烁动画
     *
     * @param view
     */
    public void TwinkleAnima(View view) {
        //闪烁
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(200);
        alphaAnimation1.setRepeatCount(1);
        view.setAnimation(alphaAnimation1);
        view.startAnimation(alphaAnimation1);
//        alphaAnimation1.setRepeatMode(Animation.REVERSE);
    }

    /**
     * view显示动画
     */
    public Animation ViewShowAnima(int interval) {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(interval);
        return animation;
    }

    /**
     * view隐藏动画
     */
    public Animation ViewHiddenAnima(int interval) {

        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(interval);

        return animation;
    }


    /**
     * 无限旋转动画
     */
    public RotateAnimation RotateAnima() {

        RotateAnimation animation;
        int magnify = 10000;
        int toDegrees = 360;
        int duration = 2000;
        toDegrees *= magnify;
        duration *= magnify;
        animation = new RotateAnimation(0, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        return animation;
    }


    /**
     * 抖动动画
     *
     * @param view
     */
    public ObjectAnimator ShakeAnima(View view) {
        return tada(view, 1f);
    }

    public ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(350);
    }

    /**
     * 动态改变高度动画
     *
     * @param view
     * @param hiding
     * @param maxHeight 最大高度
     * @param minHeight 最小高度
     */
    public static void changeHeightAnima(final View view, final boolean hiding, final int maxHeight, final int minHeight) {

        ValueAnimator va = null;
        Log.i(TAG, "flag ===" + view.getLayoutParams().height);
        if (hiding) {
            //隐藏view，高度从maxHeight变为minHeight
            va = ValueAnimator.ofInt(maxHeight,minHeight);

            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //获取当前的height值
                    int h = (Integer) valueAnimator.getAnimatedValue();
                    //动态更新view的高度
                    view.getLayoutParams().height = h;
                    view.requestLayout();
                }

            });
            va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
            va.setDuration(200);
            //开始动画
            va.start();
        }
        if (!hiding) {
            //显示view，高度从maxHeight变为minHeight
            va = ValueAnimator.ofInt(minHeight, maxHeight);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //获取当前的height值
                    int h = (Integer) valueAnimator.getAnimatedValue();
                    //动态更新view的高度
                    view.getLayoutParams().height = h;
                    view.requestLayout();
                 /*   if (maxHeight==px2dp(h))
                        view.setVisibility(View.VISIBLE);*/
                }
            });
            va.setDuration(200);
            //开始动画
            va.start();
            view.setVisibility(View.VISIBLE);
        }
    }
}
