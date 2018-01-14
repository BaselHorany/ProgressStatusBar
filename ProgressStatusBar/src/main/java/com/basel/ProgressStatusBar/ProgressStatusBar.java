package com.basel.ProgressStatusBar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
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


public class ProgressStatusBar extends View {

    private TextView mTextView;
    private RelativeLayout mRelativeLayout;
    private WindowManager windowManager;
    private WindowManager.LayoutParams parameters;
    private Paint progressPaint;
    private int barColor,barThickness,progressEndX,progress,colorPrimary,interprogress;
    private ValueAnimator barAnimator;
    boolean isShowPercentage,isViewAdded;
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

        barColor = Color.parseColor("#40212121");
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
        progressEndX = (int) (getWidth() * progress / 100f);
        progressPaint.setStrokeWidth(barThickness);
        progressPaint.setColor(barColor);
        canvas.drawRect(0, getTop(), progressEndX, getBottom(), progressPaint);
        if(progress==100){
            mRelativeLayout.setVisibility(GONE);
            mTextView.setText("");
        }
    }

    public void setShowPercentage(boolean isShowPercentage) {
        this.isShowPercentage = isShowPercentage;
        if(!isViewAdded) {
            windowManager.addView(mRelativeLayout, parameters);
            isViewAdded = true;
        }
        mRelativeLayout.setVisibility(VISIBLE);
        if(isShowPercentage) {
            mRelativeLayout.setBackgroundColor(colorPrimary);
            mTextView.setVisibility(VISIBLE);
        }else{
            mRelativeLayout.setBackgroundColor(Color.TRANSPARENT);
            mTextView.setVisibility(GONE);
        }
    }

    public void setFakeProgress(int duration,boolean isShowPercentage) {
        setShowPercentage(isShowPercentage);
        setProgress(duration, true, true);
    }

    public void setProgress(int progress,boolean isShowPercentage) {
        setShowPercentage(isShowPercentage);
        if(progress<=100) {
            setProgress(progress, true, false);
        }else{
            setProgress(0, false, false);
        }
    }

    public void setProgress(final int progress, boolean animate, final boolean isfake) {
        if (animate) {
            barAnimator = ValueAnimator.ofFloat(0, 1);

            barAnimator.setDuration(isfake ? progress : 0);

            if(isfake) {
                setProgress(0, false, isfake);
            }

            barAnimator.setInterpolator(new DecelerateInterpolator());

            barAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float interpolation = (float) animation.getAnimatedValue();
                    interprogress = (int) (interpolation * (isfake ? 100 : progress));
                    setProgress(interprogress, false, isfake);
                    mTextView.setText("%"+interprogress);
                }
            });

            if (!barAnimator.isStarted()) {
                barAnimator.start();
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

    public void remove() {
        if(isViewAdded) {
            windowManager.removeViewImmediate(mRelativeLayout);
            isViewAdded = false;
        }
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
        bundle.putInt("progress", progress);
        bundle.putBoolean("isShowPercentage", isShowPercentage);
        bundle.putParcelable("superState", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setProgress(bundle.getInt("progress"),bundle.getBoolean("isShowPercentage"));
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
}