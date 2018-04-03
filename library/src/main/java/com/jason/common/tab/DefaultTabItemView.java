package com.jason.common.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 简单Tab Item View.
 * <p>
 * Created by Jason on 2018/3/26.
 */

public class DefaultTabItemView extends LinearLayout implements ITabItem {

    private Builder mBuilder;
    private Rect mRect;

    private DefaultTabItemView(Context context, Builder builder) {
        super(context);
        setOrientation(VERTICAL);
        mBuilder = builder;
        setGravity(mBuilder.gravity);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, this, true);
        ImageView imageView = findViewById(R.id.iv_tab_icon);
        TextView textView = findViewById(R.id.txt_tab_title);
        if (mBuilder.icon != null)
            imageView.setImageDrawable(mBuilder.icon);
        if (mBuilder.iconWidth != -1)
            imageView.getLayoutParams().width = TabUtil.dp2Px(getContext(), mBuilder.iconWidth);
        if (mBuilder.iconHeight != -1)
            imageView.getLayoutParams().height = TabUtil.dp2Px(getContext(), mBuilder.iconHeight);
        if (mBuilder.titleNormalColor != -1 && mBuilder.titleSelectColor != -1)
            textView.setTextColor(createColorStateList(mBuilder.titleNormalColor, mBuilder.titleSelectColor));
        if (mBuilder.titleSize != -1)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mBuilder.titleSize);
        if (TextUtils.isEmpty(mBuilder.title)) {
            textView.setVisibility(GONE);
        } else {
            textView.setVisibility(VISIBLE);
            textView.setText(mBuilder.title);
        }
        if (mBuilder.titlePadding != null && mBuilder.titlePadding.length >= 4) {
            if (textView.getVisibility() == GONE) {
                setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + TabUtil.dp2Px(getContext(), mBuilder.titlePadding[1]) + TabUtil.dp2Px(getContext(), mBuilder.titlePadding[3]));
            } else {
                textView.setPadding(TabUtil.dp2Px(getContext(), mBuilder.titlePadding[0]), TabUtil.dp2Px(getContext(), mBuilder.titlePadding[1]), TabUtil.dp2Px(getContext(), mBuilder.titlePadding[2]), TabUtil.dp2Px(getContext(), mBuilder.titlePadding[3]));
            }
        }
        if (mBuilder.background != null)
            setBackgroundDrawable(mBuilder.background);
    }

    private ColorStateList createColorStateList(int normal, int select) {
        int[] colors = new int[]{select, normal};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    @Override
    public View getTabView() {
        return this;
    }

    @Override
    public void showDecoration(boolean show) {
        mBuilder.decorationDrawable.show(show);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mBuilder.decorationDrawable != null) {
            if (mRect == null) {
                mRect = new Rect(0, 0, getWidth(), getHeight());
            }
            mBuilder.decorationDrawable.setBounds(mRect);
            mBuilder.decorationDrawable.draw(canvas);
        }
    }

    public static final class Builder {
        private Context context;
        private String title;
        private float titleSize = -1;
        private int titleNormalColor = -1;
        private int titleSelectColor = -1;
        private int[] titlePadding;
        private Drawable icon;
        private int iconWidth = -1;
        private int iconHeight = -1;
        private int gravity = Gravity.CENTER;
        private DecorationDrawable decorationDrawable;
        //TODO
        private Drawable background;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder titleSize(float val) {
            titleSize = val;
            return this;
        }

        public Builder titleNormalColor(int val) {
            titleNormalColor = val;
            return this;
        }

        public Builder titleSelectColor(int val) {
            titleSelectColor = val;
            return this;
        }

        public Builder titlePadding(int[] val) {
            titlePadding = val;
            return this;
        }

        public Builder icon(Drawable val) {
            icon = val;
            return this;
        }

        public Builder iconWidth(int val) {
            iconWidth = val;
            return this;
        }

        public Builder iconHeight(int val) {
            iconHeight = val;
            return this;
        }

        public Builder gravity(int val) {
            gravity = val;
            return this;
        }

        public Builder decorationDrawable(DecorationDrawable val) {
            decorationDrawable = val;
            return this;
        }

        public Builder background(Drawable val) {
            background = val;
            return this;
        }

        public DefaultTabItemView build() {
            return new DefaultTabItemView(context, this);
        }
    }

    public static class DecorationDrawable extends Drawable {

        private Builder mBuilder;

        private DecorationDrawable(Builder builder) {
            this.mBuilder = builder;
        }

        public void show(boolean show) {
            mBuilder.show = show;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (!mBuilder.show)
                return;

            Rect rect = getBounds();

            int dotTop, dotLeft = 0;

            switch (mBuilder.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    dotLeft = rect.centerX() - mBuilder.width / 2;
                    break;
                case Gravity.RIGHT:
                    dotLeft = rect.right - mBuilder.width;
                    break;
                case Gravity.LEFT:
                default:
                    dotLeft = rect.left;
                    break;
            }

            dotLeft += mBuilder.margins[0] - mBuilder.margins[2];

            switch (mBuilder.gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    dotTop = rect.top;
                    break;
                case Gravity.CENTER_VERTICAL:
                    dotTop = rect.centerY() - mBuilder.height / 2;
                    break;
                case Gravity.BOTTOM:
                    dotTop = rect.bottom - mBuilder.height;
                    break;
                default:
                    dotTop = rect.top;
            }

            dotTop += mBuilder.margins[1] - mBuilder.margins[3];

            mBuilder.background.setBounds(dotLeft, dotTop, dotLeft + mBuilder.width, dotTop + mBuilder.height);
            mBuilder.background.draw(canvas);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        public static final class Builder {

            private Context context;
            private String text;
            private int width;
            private int height;
            private Drawable background;
            private int[] margins;
            private int gravity = Gravity.LEFT | Gravity.TOP;
            private boolean show;

            public Builder(Context context) {
                this.context = context;
            }

            public Builder text(String val) {
                text = val;
                return this;
            }

            public Builder width(int val) {
                width = dp2Px(context, val);
                return this;
            }

            public Builder height(int val) {
                height = dp2Px(context, val);
                return this;
            }

            public Builder background(Drawable val) {
                background = val;
                return this;
            }

            public Builder margins(int[] val) {
                int[] tmp = new int[val.length];
                for (int i = 0; i < val.length; i++) {
                    tmp[i] = dp2Px(context, val[i]);
                }
                margins = tmp;
                return this;
            }

            public Builder gravity(int val) {
                gravity = val;
                return this;
            }

            public Builder show(boolean val) {
                show = val;
                return this;
            }

            private int dp2Px(Context context, float dp) {
                float scale = context.getResources().getDisplayMetrics().density;
                return (int) (dp * scale + 0.5F);
            }

            public DecorationDrawable build() {
                return new DecorationDrawable(this);
            }
        }
    }
}
