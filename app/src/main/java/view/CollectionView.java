package view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 *
 */

public class CollectionView {
    private Context mContext;
    private Path mPath;
    private PathMeasure mPathMeasure;

    private int[] mStartPos = new int[2];
    private int[] mEndPos = new int[2];
    private float[] mCurrentPosition = new float[2];

    private View mEndView;

    public AnimateUpdateListener listener;


    public CollectionView(Context context) {
        mContext = context;
        mPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    public View getAnimateLayout(View startView, View endView, @DrawableRes int resid) {
        mEndView = endView;

        //获取起点和终点坐标
        startView.getLocationInWindow(mStartPos);
        endView.getLocationInWindow(mEndPos);

        //通过指定的resid 来获取drawable对象
        Drawable d = mContext.getResources().getDrawable(resid);
        View view = new View(mContext);
        view.setBackgroundDrawable(d);
        view.setLayoutParams(startView.getLayoutParams());

        //将该view添加到当前页面
        ViewGroup decorView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
        decorView.addView(view);
        return view;
    }

    public void startAnimation(final View animateLayout) {

        int startX = mStartPos[0];
        int startY = mStartPos[1];
        int endX = mEndPos[0];
        int endY = mEndPos[1];

        mPath.moveTo(startX, startY);
        int quadX;
        if (startX>endX){
            quadX=endX-150;
        }else {
            quadX=startX+150;
        }

        Log.d("pos","endX=="+endX+"||startX=="+startX);

        mPath.quadTo(quadX, startY, endX, endY);
        mPathMeasure.setPath(mPath, false);


        ValueAnimator animator = new ValueAnimator();
        animator.setFloatValues(0, mPathMeasure.getLength());
        animator.setDuration(2500);
        animator.setInterpolator(new LinearInterpolator());

        final float tan[] = new float[2];//旋转夹角
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, tan);
                animateLayout.setTranslationX(mCurrentPosition[0]);
                animateLayout.setTranslationY(mCurrentPosition[1]);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ViewGroup decorView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
                decorView.removeView(animateLayout);
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
                shakeAnimation(5);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (listener != null) {
                    listener.onAnimationCancel(animation);
                }

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (listener != null) {
                    listener.onAnimationRepeat(animation);
                }

            }
        });
        animator.start();
    }

    private void shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 2, 0, 1);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        mEndView.setAnimation(translateAnimation);
        mEndView.startAnimation(translateAnimation);
    }

    public void setOnAnimateUpdateListener(AnimateUpdateListener l) {
        this.listener = l;
    }

    public interface AnimateUpdateListener {
        void onAnimationStart(Animator animation);

        void onAnimationEnd(Animator animation);

        void onAnimationCancel(Animator animation);

        void onAnimationRepeat(Animator animation);
    }

}
