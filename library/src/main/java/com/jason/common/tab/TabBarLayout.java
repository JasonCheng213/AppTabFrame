package com.jason.common.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * 横向平均分块点击监听
 * <p>
 * Created by Jason on 2018/3/28.
 */

public class TabBarLayout extends AppCompatImageView {

    private OnTabBarClickListener mOnTabBarClickListener;
    private int mTouchSlop;
    private ViewGroup mTabItemContainer;

    private boolean isEnableDividing;
    private int mDividingLineHeight = -1;
    private int mDividingLineColor = -1;

    private Paint mPaint;

    public TabBarLayout(Context context) {
        this(context, null);
    }

    public TabBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TabBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void bindTabItemContainer(ViewGroup viewGroup) {
        mTabItemContainer = viewGroup;
    }

    public void setDividing(int height, int color) {
        mDividingLineHeight = TabUtil.dp2Px(getContext(), height);
        mDividingLineColor = color;
        if (mPaint == null)
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mDividingLineColor);
        mPaint.setStrokeWidth(mDividingLineHeight);
    }

    public void enableDividing(boolean enable) {
        isEnableDividing = enable;
    }

    public void setOnTabBarClickListener(OnTabBarClickListener listener) {
        mOnTabBarClickListener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isEnableDividing && mDividingLineHeight > 0 && mDividingLineColor != -1) {
            canvas.drawLine(0, mDividingLineHeight / 2, getWidth(), mDividingLineHeight / 2, mPaint);
        }
    }

    private float mLastX, mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = event.getX();
            mLastY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (Math.abs(event.getX() - mLastX) <= mTouchSlop && Math.abs(event.getY() - mLastY) <= mTouchSlop) {
                tap(mLastX, mLastY);
            }
        }
        return true;
    }

    private void tap(float x, float y) {
        if (mTabItemContainer == null)
            throw new RuntimeException("did you call bindTabItemContainer()?");
        int position = (int) (x / (getWidth() / mTabItemContainer.getChildCount()));
        if (mOnTabBarClickListener != null)
            mOnTabBarClickListener.onClick(this, position);
    }

    public interface OnTabBarClickListener {
        void onClick(View view, int tab);
    }
}
