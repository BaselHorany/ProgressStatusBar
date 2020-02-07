package com.basel.ProgressStatusBar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;


public class ProgressStatusBar extends View {

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private Paint progressPaint;
    private int progress;
    private boolean isViewAdded;
    private OnProgressListener pListener;

    public ProgressStatusBar(Context context) {
        super(context);
        init();
    }

    public ProgressStatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressStatusBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        int barColor = Color.parseColor("#60ffffff");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            barColor = Color.parseColor("#40212121");
        }

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(barColor);

        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, getStatusBarHeight(getContext()),
                WindowManager.LayoutParams.FIRST_SUB_WINDOW,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        
        setBackgroundColor(Color.TRANSPARENT);
        
    }


    private void remove() {
        this.progress = 0;
        if(isViewAdded) {
            isViewAdded = false;
            windowManager.removeViewImmediate(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        remove();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (progress!= 100) {
            int progressEndX = (int) (getWidth() * progress / 100f);
            canvas.drawRect(0, getTop(), progressEndX, getBottom(), progressPaint);
        }
    }


    private void prepare() {
        if(!isViewAdded) {
            this.progress = 0;
            windowManager.addView(this, params);
            isViewAdded = true;
        }
    }

    public void startFakeProgress(int duration) {
        prepare();
        ValueAnimator barProgress = ValueAnimator.ofFloat(0, 1);
        barProgress.setDuration(duration);
        barProgress.setInterpolator(new DecelerateInterpolator());
        barProgress.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float interpolation = (float) animation.getAnimatedValue();
                progress = (int) (interpolation * 100);
                if(isViewAdded){
                    invalidate();
                    if(pListener!=null) pListener.onUpdate(progress);
                }
            }
        });
        barProgress.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(pListener!=null) pListener.onStart();
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                if(pListener!=null) pListener.onEnd();
                remove();
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        barProgress.start();
    }

    public void setProgress(int progress) {
        prepare();
        if(this.progress==0 && pListener!=null) pListener.onStart();
        this.progress = progress;
        if (progress<100) {
            invalidate();
            if(pListener!=null) pListener.onUpdate(progress);
        }else{
            remove();
            if(pListener!=null) pListener.onEnd();
        }
    }

    public void setProgressColor(int color) {
        progressPaint.setColor(color);
    }
    
    public void setProgressBackgroundColor(int color) {
        this.setBackgroundColor(color);
    }

    public interface OnProgressListener {
        void onStart();
        void onUpdate(int progress);
        void onEnd();
    }

    public void setProgressListener(OnProgressListener progressListener) {
        pListener = progressListener;
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }else{
            result =  (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * context.getResources().getDisplayMetrics().density);
        }
        return result;
    }

}
