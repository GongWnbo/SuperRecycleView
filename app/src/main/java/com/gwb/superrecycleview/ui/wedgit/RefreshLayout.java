package com.gwb.superrecycleview.ui.wedgit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 刷新的布局，针对RecycleView
 * Created by ${GongWenbo} on 2018/5/14 0014.
 */
public class RefreshLayout extends LinearLayout {

    private Context mContext;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 设置为垂直
        setOrientation(VERTICAL);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        TextView headerView = new TextView(mContext);
        headerView.setText("header");
        addView(headerView, 0);

        TextView footerView = new TextView(mContext);
        footerView.setText("footer");
        addView(footerView, getChildCount());
    }
}
