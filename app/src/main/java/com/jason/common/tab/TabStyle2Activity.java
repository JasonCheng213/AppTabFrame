package com.jason.common.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

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
                .background(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_background_selector))
                .build();
    }

    @Override
    public void onScroll(int position, int toPosition, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onScrollState(int state) {
    }
}

