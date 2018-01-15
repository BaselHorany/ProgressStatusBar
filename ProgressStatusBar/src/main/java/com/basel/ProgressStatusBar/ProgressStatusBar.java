package com.basel.ProgressStatusBar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ProgressStatusBar extends View {

    private TextView mTextView;
    private RelativeLayout mRelativeLayout;
    private WindowManager windowManager;
    private WindowManager.LayoutParams parameters;
    private Paint progressPaint;
    private int ballsColor,barColor,barThickness,progressEndX,progress,colorPrimary,interprogress;
    private ValueAnimator barProgress;
    boolean isWait,isShowPercentage,isViewAdded;
    OnProgressListener pListener;
    public static final float ballScale = 1.0f;
    private float[] ballScaleFloats;
    ArrayList<ValueAnimator> ballsProgress;
    Handler ballsHandler;
    Runnable ballsRunnable;
    private HashMap<ValueAnimator,ValueAnimator.AnimatorUpdateListener> mBallsUpdateListeners;
    Context context;


    public ProgressStatusBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ProgressStatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ProgressStatusBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimaryDark });
        colorPrimary = a.getColor(0, 0);
        a.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            barColor = Color.parseColor("#40212121");
        }else{
            barColor = Color.parseColor("#60ffffff");
        }
        ballsColor = Color.parseColor("#ffffff");
        barThickness = getHeight();

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                statusBarHeight,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,    // Keeps the button presses from going to the background window and Draws over status bar
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.CENTER;

        mRelativeLayout = new RelativeLayout(context);
        mRelativeLayout.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams layoutParameteres = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        mRelativeLayout.setLayoutParams(layoutParameteres);


        ViewGroup.LayoutParams pvParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        this.setLayoutParams(pvParameters);
        mRelativeLayout.addView(this);

        mTextView = new TextView(context);
        ViewGroup.LayoutParams tvParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTextView.setLayoutParams(tvParameters);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setGravity(Gravity.CENTER);
        mRelativeLayout.addView(mTextView);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isWait) {
            progressEndX = (int) (getWidth() * progress / 100f);
            progressPaint.setStrokeWidth(barThickness);
            progressPaint.setColor(barColor);
            canvas.drawRect(0, getTop(), progressEndX, getBottom(), progressPaint);
            if (progress == 100) {
                remove();
            }
        }else{
            progressPaint.setColor(ballsColor);
            progressPaint.setStyle(Paint.Style.FILL);
            progressPaint.setAntiAlias(true);
            float circleSpacing=3;
            float radius=(Math.min(getWidth(),getHeight())-circleSpacing*4)/6;
            float x = getWidth()/ 2-(radius*5+circleSpacing);
            float y=getHeight() / 2;
            for (int i = 0; i < 7; i++) {
                canvas.save();
                float translateX=x+(radius*2)*i+circleSpacing*i;
                canvas.translate(translateX, y);
                canvas.scale(ballScaleFloats[i], ballScaleFloats[i]);
                canvas.drawCircle(0, 0, radius, progressPaint);
                canvas.restore();
            }
        }
    }

    public void setWaiting(int duration) {
        prepare(true,true);
        ballsProgress = new ArrayList<>();
        ballScaleFloats = new float[]{ballScale, ballScale, ballScale, ballScale, ballScale, ballScale, ballScale};
        mBallsUpdateListeners = new HashMap<>();
        int[] delays = new int[]{150,260,375,450,575,650,775};
        for (int i = 0; i < 7; i++) {
            final int index = i;
            ValueAnimator scaleAnimate = ValueAnimator.ofFloat(1, 0.4f, 1);
            scaleAnimate.setDuration(750);
            scaleAnimate.setRepeatCount(ValueAnimator.INFINITE);
            scaleAnimate.setStartDelay(delays[i]);
            addBallsUpdateListener(scaleAnimate,new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ballScaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            ballsProgress.add(scaleAnimate);
        }
        for (int i = 0; i < ballsProgress.size(); i++) {
            ValueAnimator animator = ballsProgress.get(i);
            ValueAnimator.AnimatorUpdateListener ballupdateListener = mBallsUpdateListeners.get(animator);
            if (ballupdateListener!=null){
                animator.addUpdateListener(ballupdateListener);
            }
            animator.start();
        }
        ballsHandler = new Handler();
        ballsRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ballsProgress.size(); i++) {
                    ValueAnimator animator = ballsProgress.get(i);
                    ValueAnimator.AnimatorUpdateListener ballupdateListener = mBallsUpdateListeners.get(animator);
                    if (ballupdateListener!=null){
                        animator.removeUpdateListener(ballupdateListener);
                    }
                    animator.cancel();
                }
                remove();
                ballsHandler.removeCallbacks(ballsRunnable);
            }
        };
        ballsHandler.postDelayed(ballsRunnable, duration);
    }

    public void prepare(boolean isShowPercentage,boolean isWait) {
        this.isShowPercentage = isShowPercentage;
        this.isWait = isWait;
        if(!isViewAdded) {
            windowManager.addView(mRelativeLayout, parameters);
            isViewAdded = true;
            pListener.onStart();
        }
        mRelativeLayout.setVisibility(VISIBLE);
        if(isShowPercentage) {
            mRelativeLayout.setBackgroundColor(colorPrimary);
            if(!isWait){
                mTextView.setVisibility(VISIBLE);
            }
        }else{
            mRelativeLayout.setBackgroundColor(Color.TRANSPARENT);
            mTextView.setVisibility(GONE);
        }

    }

    public void setFakeProgress(int duration,boolean isShowPercentage) {
        prepare(isShowPercentage,false);
        setProgress(duration, true, true);
    }

    public void setProgress(int progress,boolean isShowPercentage) {
        prepare(isShowPercentage,false);
        if(progress<=100) {
            setProgress(progress, true, false);
        }else{
            setProgress(0, false, false);
        }
    }

    public void setProgress(final int progress, boolean animate, final boolean isfake) {
        if (animate) {
            barProgress = ValueAnimator.ofFloat(0, 1);
            barProgress.setDuration(isfake ? progress : 0);
            if(isfake) {
                setProgress(0, false, isfake);
            }
            barProgress.setInterpolator(new DecelerateInterpolator());
            barProgress.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float interpolation = (float) animation.getAnimatedValue();
                    interprogress = (int) (interpolation * (isfake ? 100 : progress));
                    setProgress(interprogress, false, isfake);
                    mTextView.setText("%"+interprogress);
                    pListener.onUpdate(interprogress);
                }
            });
            if (!barProgress.isStarted()) {
                barProgress.start();
            }
        } else {
            this.progress = progress;
            postInvalidate();
        }
    }

    public void setProgressColor(int color) {
        barColor = color;
    }

    public void setProgressBackgroundColor(int color) {
        mRelativeLayout.setBackgroundColor(color);
    }

    public void setBallsColor(int color) {
        ballsColor = color;
    }

    public void remove() {
        if(isViewAdded) {
            windowManager.removeViewImmediate(mRelativeLayout);
            isViewAdded = false;
            mRelativeLayout.setVisibility(GONE);
            mTextView.setText("");
            pListener.onEnd();
        }
    }

    public interface OnProgressListener {
        void onStart();
        void onUpdate(int progress);
        void onEnd();
    }

    public void setProgressListener(OnProgressListener progressListener) {
        pListener = progressListener;
    }

    public void addBallsUpdateListener(ValueAnimator animator, ValueAnimator.AnimatorUpdateListener updateListener){
        mBallsUpdateListeners.put(animator,updateListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if(!isWait) {
            bundle.putInt("progress", progress);
            bundle.putBoolean("isShowPercentage", isShowPercentage);
            bundle.putParcelable("superState", super.onSaveInstanceState());
        }
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle&&!isWait) {
            Bundle bundle = (Bundle) state;
            setProgress(bundle.getInt("progress"),bundle.getBoolean("isShowPercentage"));
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
}
