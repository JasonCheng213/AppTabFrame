package com.jason.common.tab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Tab Bar Layout
 * <p>
 * Created by Jason on 2018/3/28.
 */

public class TabBarLayout extends FrameLayout implements View.OnClickListener {

    private OnTabBarClickListener mOnTabBarClickListener;
    private ImageView mTabBackground;
    private LinearLayout mTabClickContainer;

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
        mTabBackground = new ImageView(getContext());
        mTabBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mTabClickContainer = new LinearLayout(getContext());
        addView(mTabBackground, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mTabClickContainer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public LinearLayout getTabClickContainer() {
        return mTabClickContainer;
    }

    public void bindTabItemContainer(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View clickView = new View(getContext());
            clickView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            clickView.setOnClickListener(this);
            clickView.setTag(i);
            clickView.setClickable(true);
            Drawable background = viewGroup.getChildAt(i).getBackground();
            if (background != null) {
                viewGroup.getChildAt(i).setBackgroundDrawable(null);
                clickView.setBackgroundDrawable(background);
            }
            mTabClickContainer.addView(clickView);
        }
    }

    public void setTabBackground(Drawable drawable) {
        mTabBackground.setImageDrawable(drawable);
    }

    public void setScrollEffectBackground(Drawable drawable) {
        if (drawable != null)
            mTabClickContainer.setBackgroundDrawable(drawable);
    }

    public void setDividing(float height, int color) {
        int tmpHeight = TabUtil.dp2Px(getContext(), height);
        mDividingLineHeight = tmpHeight < 1 ? 1 : tmpHeight;
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

    @Override
    public void onClick(View view) {
        if (mOnTabBarClickListener != null)
            mOnTabBarClickListener.onClick(this, (Integer) view.getTag());
    }

    public interface OnTabBarClickListener {
        void onClick(View view, int tab);
    }
}
