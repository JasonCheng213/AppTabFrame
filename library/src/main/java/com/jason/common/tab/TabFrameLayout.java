package com.jason.common.tab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * App Tab Frame Layout
 * <p>
 * Created by Jason on 2018/3/26.
 */
public class TabFrameLayout extends RelativeLayout implements ViewPager.OnPageChangeListener {

    protected SlideViewPager mViewPager;
    protected TabBarLayout mTabBar;
    protected LinearLayout mTabItemContainer;

    private Builder mBuilder;

    private int mLastSelectedPosition = 0;

    private TabFrameLayout(Context context, Builder builder) {
        super(context);
        mBuilder = builder;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.tab_frame_layout, this, true);

        mViewPager = findViewById(R.id.pager_main_frame);
        mTabBar = findViewById(R.id.iv_main_frame_tab_background);
        mTabItemContainer = findViewById(R.id.ll_main_frame_tab_container);
        mViewPager.addOnPageChangeListener(this);
        mTabBar.setOnTabBarClickListener(mOnTabBarClickListener);

        mViewPager.setEnableSlide(mBuilder.isEnableSlide);
        if (mBuilder.mTabBarHeight > 0) {
            mTabBar.getLayoutParams().height = mBuilder.mTabBarHeight;
        }
        if (mBuilder.mTabBarColor != -1) {
            mTabBar.setBackgroundColor(mBuilder.mTabBarColor);
        }
        if (mBuilder.mTabBarDrawable != null) {
            mTabBar.setTabBackground(mBuilder.mTabBarDrawable);
        }
        if (mBuilder.mTabBarDividingHeight > 0 && mBuilder.mTabBarDividingColor != -1)
            mTabBar.setDividing(mBuilder.mTabBarDividingHeight, mBuilder.mTabBarDividingColor);
        mTabBar.enableDividing(mBuilder.isEnableDividing);
        if (mBuilder.mListTabItem.size() != mBuilder.mListFragment.size())
            throw new RuntimeException("tab count mot match fragment count.");

        for (int i = 0; i < mBuilder.mListTabItem.size(); i++) {
            ITabItem tabItem = mBuilder.mListTabItem.get(i);
            if (i == 0) {
                changeViewSelectState(tabItem.getTabView(), true);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            mTabItemContainer.addView(tabItem.getTabView(), params);
        }
        mTabBar.bindTabItemContainer(mTabItemContainer);
        mViewPager.setAdapter(new TabPagerAdapter(mBuilder.mFragmentManager, mBuilder.mListFragment));
    }

    public int getCurrentTabPosition() {
        return mLastSelectedPosition;
    }

    public int getTabPositionByKey(String key) {
        for (int i = 0; i < mBuilder.mListTabItem.size(); i++) {
            View tabView = mBuilder.mListTabItem.get(i).getTabView();
            if (tabView.getTag() != null && tabView.getTag().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public void setCurrentTab(int position) {
        mOnTabBarClickListener.onClick(mTabBar, position);
    }

    public void setCurrentTab(@NonNull String key) {
        for (int i = 0; i < mBuilder.mListTabItem.size(); i++) {
            View tabView = mBuilder.mListTabItem.get(i).getTabView();
            if (tabView.getTag() != null && tabView.getTag().equals(key)) {
                mOnTabBarClickListener.onClick(mTabBar, i);
            }
        }
    }

    public ITabItem getTabItem(int position) {
        if (position < 0 || position >= mTabItemContainer.getChildCount())
            return null;
        return mBuilder.mListTabItem.get(position);
    }

    public ITabItem getTabItem(@NonNull String key) {
        for (ITabItem tabItem : mBuilder.mListTabItem) {
            if (tabItem.getTabView().getTag() != null && tabItem.getTabView().getTag().equals(key)) {
                return tabItem;
            }
        }
        return null;
    }

    public String getTabKey(int position) {
        if (position < 0 || position >= mTabItemContainer.getChildCount())
            return null;
        return (String) mBuilder.mListTabItem.get(position).getTabView().getTag();
    }

    public void showDecoration(String key, boolean show) {
        ITabItem tabItem = getTabItem(key);
        tabItem.showDecoration(show);
    }

    public TabBarLayout getTabBar() {
        return mTabBar;
    }

    private TabBarLayout.OnTabBarClickListener mOnTabBarClickListener = new TabBarLayout.OnTabBarClickListener() {
        @Override
        public void onClick(View view, int tab) {
            if (mBuilder.mOnTabInterruptListener == null) {
                mBuilder.mOnTabInterruptListener = new OnTabInterruptListener() {
                    @Override
                    public boolean onInterruptSelect(int position, ITabItem tab) {
                        return false;
                    }
                };
            }

            ITabItem clickTabItem = mBuilder.mListTabItem.get(tab);
            if (mBuilder.mOnTabSelectedListeners != null && !mBuilder.mOnTabSelectedListeners.isEmpty()) {
                for (OnTabSelectedListener listener : mBuilder.mOnTabSelectedListeners) {
                    listener.onTabClicked(tab, clickTabItem);
                }
            }
            mViewPager.setCurrentItem(tab, mBuilder.isEnableSlide);
        }
    };

    private void notifyTabChange(int tab) {
        if (mLastSelectedPosition == tab) {
            if (mBuilder.mOnTabSelectedListeners != null && !mBuilder.mOnTabSelectedListeners.isEmpty()) {
                for (OnTabSelectedListener listener : mBuilder.mOnTabSelectedListeners) {
                    listener.onTabReselected(tab, getTabItem(tab));
                }
            }
        } else {
            changeViewSelectState(mBuilder.mListTabItem.get(mLastSelectedPosition).getTabView(), false);
            if (mBuilder.mOnTabSelectedListeners != null && !mBuilder.mOnTabSelectedListeners.isEmpty()) {
                for (OnTabSelectedListener listener : mBuilder.mOnTabSelectedListeners) {
                    listener.onTabUnselected(mLastSelectedPosition, mBuilder.mListTabItem.get(mLastSelectedPosition));
                }
            }
            mViewPager.setCurrentItem(tab, mBuilder.isEnableSlide);
            changeViewSelectState(getTabItem(tab).getTabView(), true);
            if (mBuilder.mOnTabSelectedListeners != null && !mBuilder.mOnTabSelectedListeners.isEmpty()) {
                for (OnTabSelectedListener listener : mBuilder.mOnTabSelectedListeners) {
                    listener.onTabSelected(tab, getTabItem(tab));
                }
            }
        }
        mLastSelectedPosition = tab;
    }

    private void changeViewSelectState(View view, boolean select) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setSelected(select);
            }
        } else {
            view.setSelected(select);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mBuilder.mOnPageScrolledListener != null) {
            mTabBar.setScrollEffectBackground(mBuilder.mOnPageScrolledListener.onScroll(position,
                    positionOffset, positionOffsetPixels, getWidth() / mTabItemContainer.getChildCount()));
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mBuilder.mOnTabInterruptListener == null || !mBuilder.mOnTabInterruptListener.onInterruptSelect(position, getTabItem(position))) {
            notifyTabChange(position);
        } else {
            mViewPager.setCurrentItem(mLastSelectedPosition, false);
        }
        if (mBuilder.mOnPageScrolledListener != null) {
            mBuilder.mOnPageScrolledListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mBuilder.mOnPageScrolledListener != null) {
            mBuilder.mOnPageScrolledListener.onScrollState(state);
        }
    }

    private class TabPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        TabPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    public static class Builder {

        private Context mContext;
        private FragmentManager mFragmentManager;

        private boolean isEnableSlide = true;
        private int mTabBarHeight = -1;
        private int mTabBarColor = -1;
        private Drawable mTabBarDrawable;
        private float mTabBarDividingHeight = -1;
        private int mTabBarDividingColor = -1;
        private boolean isEnableDividing;
        private List<OnTabSelectedListener> mOnTabSelectedListeners;
        private OnTabInterruptListener mOnTabInterruptListener;
        private OnPageScrolledListener mOnPageScrolledListener;

        private final List<ITabItem> mListTabItem = new ArrayList<>();
        private final List<Fragment> mListFragment = new ArrayList<>();

        public Builder(Context context, FragmentManager fragmentManager) {
            mContext = context;
            mFragmentManager = fragmentManager;
        }

        public Builder addTab(ITabItem tabItem, Fragment fragment) {
            addTab(null, tabItem, fragment);
            return this;
        }

        public Builder addTab(String tabKey, ITabItem tabItem, Fragment fragment) {
            if (tabItem == null)
                return this;

            if (tabKey != null)
                tabItem.getTabView().setTag(tabKey);

            mListTabItem.add(tabItem);

            if (fragment == null)
                fragment = new Fragment();
            mListFragment.add(fragment);
            return this;
        }

        public Builder enableSlide(boolean enable) {
            isEnableSlide = enable;
            return this;
        }

        public Builder setTabBackgroundHeight(boolean isDp, int height) {
            if (isDp) {
                mTabBarHeight = TabUtil.dp2Px(mContext, height);
            } else {
                mTabBarHeight = height;
            }
            return this;
        }

        public Builder setTabBackgroundColor(int color) {
            mTabBarColor = color;
            return this;
        }

        public Builder setTabBackgroundDrawable(Drawable drawable) {
            mTabBarDrawable = drawable;
            return this;
        }

        public Builder addTabSelectListener(OnTabSelectedListener listener) {
            if (mOnTabSelectedListeners == null)
                mOnTabSelectedListeners = new ArrayList<>();
            mOnTabSelectedListeners.add(listener);
            return this;
        }

        public Builder setTabClickListener(OnTabInterruptListener listener) {
            mOnTabInterruptListener = listener;
            return this;
        }

        public Builder setPageScrolledListener(OnPageScrolledListener listener) {
            mOnPageScrolledListener = listener;
            return this;
        }

        public Builder setTabBarDividingHeight(float height) {
            mTabBarDividingHeight = height;
            return this;
        }

        public Builder setTabBarDividingColor(int color) {
            mTabBarDividingColor = color;
            return this;
        }

        public Builder enableDividing(boolean enable) {
            isEnableDividing = enable;
            return this;
        }

        public TabFrameLayout build() {
            return new TabFrameLayout(mContext, this);
        }
    }

    public interface OnTabInterruptListener {

        boolean onInterruptSelect(int position, ITabItem tab);
    }

    public interface OnTabSelectedListener {

        void onTabClicked(int position, ITabItem tab);

        void onTabSelected(int position, ITabItem tab);

        void onTabUnselected(int position, ITabItem tab);

        void onTabReselected(int position, ITabItem tab);
    }

    public interface OnPageScrolledListener {

        Drawable onScroll(int position, float positionOffset, int positionOffsetPixels, int tabWidth);

        void onPageSelected(int position);

        void onScrollState(int state);
    }
}
