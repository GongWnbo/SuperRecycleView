package com.gwb.superrecycleview.ui.wedgit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * Created by ${GongWenbo} on 2018/4/26 0026.
 */

public class StreamLayout extends ViewGroup {

    private List<List<View>> mViewList   = new ArrayList<>();
    private List<Integer>    mHeightList = new ArrayList<>();

    public StreamLayout(Context context) {
        this(context, null);
    }

    public StreamLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StreamLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取测量模式和大小
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 如果是wrap_content
        int width = 0;
        int height = 0;
        // 一行的宽度
        int lineWidth = 0;
        // 一行的高度
        int lineHeight = 0;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到child的布局管理器
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // getMeasuredWidth是view原始的宽度，getWidth是view显示的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            // 如果宽度大于父控件的，换行
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);
                height += lineHeight;
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }
            // 如果是最后一个
            if (i == childCount - 1) {
                height += lineHeight;
                width = Math.max(width, lineWidth);
            }
        }
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mViewList.clear();
        mHeightList.clear();

        int childCount = getChildCount();
        int width = getWidth();
        // 一行的宽度
        int lineWidth = 0;
        // 一行的高度
        int lineHeight = 0;
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + childWidth > width) {
                mViewList.add(viewList);
                mHeightList.add(lineHeight);
//                lineWidth = childWidth;
                lineWidth = 0;
                viewList = new ArrayList<>();
//                lineHeight = childHeight;
            }
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            viewList.add(child);
        }
        mViewList.add(viewList);
        mHeightList.add(lineHeight);
        int left = 0;
        int top = 0;
        for (int i = 0; i < mViewList.size(); i++) {
            viewList = mViewList.get(i);
            lineHeight = mHeightList.get(i);
            for (View view : viewList) {
                if (view.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + view.getMeasuredWidth();
                int bc = tc + view.getMeasuredHeight();
                view.layout(lc, tc, rc, bc);
                left += view.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

}