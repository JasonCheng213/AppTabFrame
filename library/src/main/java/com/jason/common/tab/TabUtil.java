package com.jason.common.tab;

import android.content.Context;

public class TabUtil {

    public static int dp2Px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }

}
