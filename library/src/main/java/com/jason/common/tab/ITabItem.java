package com.jason.common.tab;

import android.view.View;

/**
 * Tab Item接口
 * <p>
 * Created by Jason on 2018/3/29.
 */

public interface ITabItem {

    View getTabView();

    void showDecoration(boolean show);

}
