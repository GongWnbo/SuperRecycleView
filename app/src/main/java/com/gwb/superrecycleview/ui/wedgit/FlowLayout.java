package com.gwb.superrecycleview.ui.wedgit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

    private static final String TAG = "FlowLayout";

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int widthLine = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            // TODO: 2018/6/11 换行
            if (widthLine + childWidth > withSize) {
                height += childHeight;
                width = Math.max(widthLine, width);
                // TODO: 2018/6/11 这边要特别注意，要在获取最大值的后面
                widthLine = childWidth;
                Log.d(TAG, "onMeasure: 换行=" + height);
            } else {
                Log.d(TAG, "onMeasure: widthLine=" + widthLine);
                widthLine += childWidth;
            }
            if (i == getChildCount() - 1) {
                width = Math.max(widthLine, width);
                height += childHeight;
                Log.d(TAG, "onMeasure: 最后一行=" + height);
            }
        }
        // TODO: 2018/6/19 考虑到padding的情况存在
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(withMode == MeasureSpec.EXACTLY ? withSize : width
                , heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();
        int left = getPaddingLeft();
        int widthLine = 0;
        int width = getWidth();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            // TODO: 2018/6/16  item的宽度
            int tempWidth = childWidth + lp.leftMargin + lp.rightMargin;
            // TODO: 2018/6/16 换行
            if (widthLine + tempWidth > width) {
                left = getPaddingLeft();
                top += childHeight + lp.topMargin + lp.bottomMargin;
                widthLine = tempWidth;
            } else {
                widthLine += tempWidth;
            }
            int lc = left + lp.leftMargin;
            int tc = top + lp.topMargin;
            int rc = lc + childWidth;
            int bc = tc + childHeight;
            childView.layout(lc, tc, rc, bc);
            left += childWidth + lp.rightMargin;
        }

    }

}
