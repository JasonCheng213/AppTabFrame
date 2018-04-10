package com.jason.common.tab;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements TabFrameLayout.OnPageScrolledListener, TabFrameLayout.OnTabInterruptListener, TabFrameLayout.OnTabSelectedListener {

    private static final String TAG = "MainActivity";

    TabFrameLayout mTabFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabFrameLayout = new TabFrameLayout.Builder(getApplicationContext(), getSupportFragmentManager())
                .enableSlide(false)
                .enableDividing(true)
                .setTabBarDividingColor(Color.DKGRAY)
                .setTabBarDividingHeight(0.5f)
                .addTab("key1", createTabItem("分类", R.drawable.tab_1_selector), TestFragment.newInstance("Tab1", Color.BLUE))
                .addTab("key2", createTabItem("购物车", R.drawable.tab_2_selector), TestFragment.newInstance("Tab2", Color.RED))
                .addTab("key3", createTabItem("设置", R.drawable.tab_3_selector), TestFragment.newInstance("Tab3", Color.YELLOW))
                .addTab("key4", createTabItem("会员", R.drawable.tab_4_selector), TestFragment.newInstance("Tab4", Color.GREEN))
                .setTabClickListener(this)
                .addTabSelectListener(this)
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

    private int interrupt = 0;

    @Override
    public boolean onInterruptSelect(final int position, ITabItem tab) {
        if (position == 1 && interrupt++ % 2 == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("事件拦截，是否切换？")
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mTabFrameLayout.setCurrentTab(position);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
            return true;
        }
        return false;
    }

    @Override
    public void onTabClicked(int position, ITabItem tab) {
        Log.d(TAG, "onTabClicked: " + position);
    }

    @Override
    public void onTabSelected(int position, ITabItem tab) {
        Log.d(TAG, "onTabSelected: " + position);
    }

    @Override
    public void onTabUnselected(int position, ITabItem tab) {
        Log.d(TAG, "onTabUnselected: " + position);
    }

    @Override
    public void onTabReselected(int position, ITabItem tab) {
        Log.d(TAG, "onTabReselected: " + position);
    }

    @Override
    public void onScroll(int position, int toPosition, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onScroll: " + position + " " + toPosition + " " + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: " + position);
    }

    @Override
    public void onScrollState(int state) {
        Log.d(TAG, "onScrollState: " + state);
    }
}
