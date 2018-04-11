package com.jason.common.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class TabStyle2Activity extends AppCompatActivity implements TabFrameLayout.OnPageScrolledListener {

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, TabStyle2Activity.class);
        return starter;
    }

    TabFrameLayout mTabFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabFrameLayout = new TabFrameLayout.Builder(getApplicationContext(), getSupportFragmentManager())
                .enableSlide(true)
                .enableDividing(true)
                .setTabBarDividingColor(Color.parseColor("#CCCCCC"))
                .setTabBarDividingHeight(0.5f)
                .addTab("key1", createTabItem("分类", R.drawable.tab_1_selector), TestFragment.newInstance("Tab1", Color.BLUE))
                .addTab("key2", createTabItem("购物车", R.drawable.tab_2_selector), TestFragment.newInstance("Tab2", Color.RED))
                .addTab("key3", createTabItem("设置", R.drawable.tab_3_selector), TestFragment.newInstance("Tab3", Color.YELLOW))
                .setPageScrolledListener(this)
                .build();

        setContentView(mTabFrameLayout);
    }

    private ITabItem createTabItem(String title, int iconRes) {
        return new DefaultTabItemView.Builder(getApplicationContext())
                .title(title)
                .titleSize(12)
                .titleNormalColor(Color.GRAY)
                .titleSelectColor(Color.RED)
                .titlePadding(new int[]{0, 0, 2, 5})
                .icon(ContextCompat.getDrawable(getApplicationContext(), iconRes))
                .iconWidth(18)
                .iconHeight(18)
                .build();
    }

    IndicatorDrawable mIndicatorDrawable = null;

    @Override
    public Drawable onScroll(int position, float positionOffset, int positionOffsetPixels, int tabWidth) {
        if (mIndicatorDrawable == null) {
            mIndicatorDrawable = new IndicatorDrawable(TabUtil.dp2Px(getApplicationContext(), 5), tabWidth);
        }
        mIndicatorDrawable.update((int) (position * tabWidth + positionOffset * tabWidth));
        return mIndicatorDrawable;
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onScrollState(int state) {
    }

    class IndicatorDrawable extends Drawable {

        private int mHeight;
        private int mWidth;
        private int x;

        private Paint mPaint;

        public IndicatorDrawable(int height, int width) {
            mHeight = height;
            mWidth = width;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.BLUE);
            mPaint.setStrokeWidth(mHeight);
        }

        public void update(int x) {
            this.x = x;
            Log.d("jason", "update: " + x);
            invalidateSelf();
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            canvas.drawLine(x, getBounds().height() - mHeight / 2, x + mWidth, getBounds().height() - mHeight / 2, mPaint);
        }

        @Override
        public void setAlpha(int i) {
            mPaint.setAlpha(i);
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            mPaint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }
}

