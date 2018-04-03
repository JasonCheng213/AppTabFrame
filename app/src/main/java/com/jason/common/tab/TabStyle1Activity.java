package com.jason.common.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class TabStyle1Activity extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        Intent starter = new Intent(context, TabStyle1Activity.class);
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
                .addTab(createRoundItem(), TestFragment.newInstance("Tab-Center", Color.WHITE))
                .addTab("key3", createTabItem("设置", R.drawable.tab_3_selector), TestFragment.newInstance("Tab3", Color.YELLOW))
                .addTab("key4", createTabItem("会员", R.drawable.tab_4_selector), TestFragment.newInstance("Tab4", Color.GREEN))
                .build();

        setContentView(mTabFrameLayout);
    }

    private ITabItem createRoundItem() {
        return new DefaultTabItemView.Builder(getApplicationContext())
                .icon(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_launcher_round))
                .titlePadding(new int[]{0, 0, 0, 10})
                .build();
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
}

