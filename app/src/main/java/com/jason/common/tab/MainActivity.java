package com.jason.common.tab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TabFrameLayout mTabFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabFrameLayout = new TabFrameLayout.Builder(getApplicationContext(), getSupportFragmentManager())
                .addTab("key1", createTabItem("Tab1", -1), TestFragment.newInstance("Tab1", Color.BLUE))
                .addTab("key2", createTabItem("Tab2", -1), TestFragment.newInstance("Tab2", Color.RED))
                .addTab("key3", createTabItem("Tab3", -1), TestFragment.newInstance("Tab3", Color.YELLOW))
                .build();

        setContentView(mTabFrameLayout);
    }

    private ITabItem createTabItem(String title, int drawable) {
        return new DefaultTabItemView.Builder(getApplicationContext())
                .title(title)
                .titleNormalColor(Color.GRAY)
                .titleSelectColor(Color.RED)
                .iconHeight(25)
                .iconHeight(25)
                .iconHeight(drawable)
                .build();
    }
}
