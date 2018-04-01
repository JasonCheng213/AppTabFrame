package com.jason.common.tab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jason on 2018/4/1.
 */

public class TestFragment extends Fragment {

    public static TestFragment newInstance(String title, int color) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("color", color);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText(getArguments().getString("title"));
        textView.setBackgroundColor(getArguments().getInt("color"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }
}
