package com.gwb.superrecycleview.ui.wedgit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gwb.superrecycleview.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 刷新的布局，针对RecycleView
 * Created by ${GongWenbo} on 2018/5/14 0014.
 */
public class RefreshLayout extends ViewGroup {

    //<editor-fold desc="属性变量 property and variable">
    private Context mContext;
    private View    mHeaderView;
    private View    mFooterView;
    //</editor-fold>

    //<editor-fold desc="构造方法 construction methods">
    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
    //</editor-fold>

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.refresh_header, this, false);
        addView(mHeaderView, 0);

        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.refresh_footer, this, false);
        addView(mFooterView, getChildCount());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int top = 0;
        int bottom = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            bottom = top + childView.getMeasuredHeight();
            if (childView == mHeaderView) {
//                int headerHeight = childView.getMeasuredHeight();
//                childView.layout(l, -headerHeight, r, headerHeight);
            } else if (childView == mFooterView) {
                childView.layout(l, top, r, bottom);
                top += childView.getMeasuredHeight();
            } else {
                int footerHeight = childView.getMeasuredHeight();
                childView.layout(l, top - footerHeight, r, footerHeight);
            }
        }
    }

    //<editor-fold desc="滑动判断 judgement of slide">
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    //</editor-fold>

    //<editor-fold desc="布局参数 LayoutParams">
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(mContext, attrs);
    }
    //</editor-fold>

}
