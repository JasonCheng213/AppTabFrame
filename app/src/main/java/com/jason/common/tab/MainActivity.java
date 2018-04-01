package com.jason.common.tab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TabFrameLayout mTabFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabFrameLayout = new TabFrameLayout.Builder(getApplicationContext(), getSupportFragmentManager())
                .enableSlide(true)
                .enableDividing(true)
                .setTabBarDividingColor(Color.DKGRAY)
                .setTabBarDividingHeight(1)
                .addTab("key1", createTabItem("分类", R.drawable.tab_1_selector), TestFragment.newInstance("Tab1", Color.BLUE))
                .addTab("key2", createTabItem("购物车", R.drawable.tab_2_selector), TestFragment.newInstance("Tab2", Color.RED))
                .addTab("key3", createTabItem("设置", R.drawable.tab_3_selector), TestFragment.newInstance("Tab3", Color.YELLOW))
                .build();

        setContentView(mTabFrameLayout);
    }

    private ITabItem createTabItem(String title, int iconRes) {
        return new DefaultTabItemView.Builder(getApplicationContext())
                .title(title)
                .titleNormalColor(Color.GRAY)
                .titleSelectColor(Color.RED)
                .titlePadding(new int[]{0, 0, 2, 5})
                .icon(ContextCompat.getDrawable(getApplicationContext(), iconRes))
                .iconWidth(18)
                .iconHeight(18)
                .build();
    }
}
