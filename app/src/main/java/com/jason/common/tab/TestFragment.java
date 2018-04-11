package com.jason.common.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jason on 2018/4/1.
 */

public class TestFragment extends Fragment {

    private View mRootView;

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
        if (mRootView == null)
            mRootView = inflater.inflate(R.layout.fragment_test, container, false);

        mRootView.setBackgroundColor(getArguments().getInt("color"));
        TextView textView = mRootView.findViewById(R.id.txt_hint);
        textView.setText(getArguments().getString("title"));
        mRootView.findViewById(R.id.btn_tab_style_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TabStyle1Activity.getIntent(getContext()));
            }
        });

        mRootView.findViewById(R.id.btn_tab_style_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(TabStyle2Activity.getIntent(getContext()));
            }
        });
        return mRootView;
    }
}
