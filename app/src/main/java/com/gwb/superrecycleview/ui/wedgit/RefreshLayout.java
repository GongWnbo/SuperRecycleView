package com.gwb.superrecycleview.ui.wedgit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gwb.superrecycleview.R;

/**
 * 刷新的布局，针对RecycleView
 * Created by ${GongWenbo} on 2018/5/14 0014.
 */
public class RefreshLayout extends LinearLayout {

    //<editor-fold desc="常量 displacement">
    private Context mContext;
    //</editor-fold>

    //<editor-fold desc="构造方法 displacement">
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
    //</editor-fold>

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View headerView = LayoutInflater.from(mContext).inflate(R.layout.refresh_header, this, false);
        addView(headerView, 0);

        View footerView = LayoutInflater.from(mContext).inflate(R.layout.refresh_footer, this, false);
        addView(footerView, getChildCount());
    }

}
