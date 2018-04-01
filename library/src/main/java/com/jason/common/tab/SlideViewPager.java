package com.jason.common.tab;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SlideViewPager extends ViewPager {

    public SlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideViewPager(Context context) {
        super(context);
        init();
    }

    private void init() {
    }

    private boolean isEnableSlide = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isEnableSlide && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isEnableSlide && super.onInterceptTouchEvent(event);
    }

    public void setEnableSlide(boolean enableSilide) {
        this.isEnableSlide = enableSilide;
    }
}
