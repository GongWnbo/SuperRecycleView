package com.gwb.superrecycleview.ui.wedgit;

import android.animation.ValueAnimator;
import android.content.Context;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.gwb.superrecycleview.R;


/**
 * 刷新的布局，针对RecycleView
 * Created by ${GongWenbo} on 2018/5/14 0014.
 */
public class RefreshLayout extends LinearLayout {

    private static final String TAG = "RefreshLayout";
    //<editor-fold desc="属性变量 property and variable">
    private Context         mContext;
    private View            mHeaderView;
    private View            mFooterView;
    private float           mLastY;
    private Scroller        mScroller;
    private VelocityTracker mVelocityTracker;
    private int             mTouchSlop;
    private int             mMaximumVelocity;
    private int             mMinimumVelocity;
    private int             mHeaderHeight;
    private float           mLastX;
    private RecyclerView    mRecyclerView;
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

        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }
    //</editor-fold>

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 添加头部
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.refresh_header, this, false);
        addView(mHeaderView, 0);
        // 获取刷新的控件，暂时为RecycleView
        View childView = getChildAt(1);
        if (childView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) childView;
        }
        // 添加尾部
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.refresh_footer, this, false);
        addView(mFooterView, getChildCount());
        setOrientation(VERTICAL);
    }

    //    @Override
    //    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //        int height = 0;
    //        int width = 0;
    //        for (int i = 0; i < getChildCount(); i++) {
    //            View childView = getChildAt(i);
    //            childView.measure(widthMeasureSpec,heightMeasureSpec);
    //            height += childView.getMeasuredHeight();
    //            width = childView.getMeasuredWidth();
    //        }
    //        setMeasuredDimension(View.resolveSize(width, widthMeasureSpec)
    //                , View.resolveSize(height, heightMeasureSpec));
    //    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView == mHeaderView) {
                mHeaderHeight = childView.getMeasuredHeight();
                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                lp.topMargin = -mHeaderHeight;
                childView.setLayoutParams(lp);
            }
        }
    }

    //<editor-fold desc="滑动判断 judgement of slide">
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int y = (int) event.getY();
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                mLastX = event.getX();
                // 如果没有完成，终止上一次的
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                super.dispatchTouchEvent(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                int deltaY = Math.round(mLastY - y);
                int deltaX = Math.round(mLastX - x);
                if (deltaY < mTouchSlop) {
                    return super.dispatchTouchEvent(event);
                }
                // 保证能横向滑动
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    return super.dispatchTouchEvent(event);
                }
                if (mRecyclerView != null) {
                    // 竖直方向recyclerView可否下拉
                    if (mRecyclerView.computeVerticalScrollOffset() <= 0) {
                        return super.dispatchTouchEvent(event);
                    }
                    // 竖直方向recyclerView可否上拉
                    if (mRecyclerView.computeVerticalScrollExtent() + mRecyclerView.computeVerticalScrollOffset()
                            >= mRecyclerView.computeVerticalScrollRange()) {
                        return super.dispatchTouchEvent(event);
                    }
                }

                Log.d(TAG, "dispatchTouchEvent: " + y + ",mHeaderHeight:" + mHeaderHeight);
                scrollBy(0, deltaY);
                mLastY = y;
                postInvalidate();
                return true;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int yVelocity = (int) mVelocityTracker.getYVelocity();
                fling(-yVelocity);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void fling(int velocityY) {
        mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0, 0, 0);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            // 设置一个滚动动画
            float startY = mScroller.getStartY();
            float finalY = mScroller.getFinalY();
            float space = Math.abs(startY - finalY);
            final ValueAnimator va = ValueAnimator.ofFloat(startY, finalY);
            va.setDuration((long) (space * 0.5f));
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float y = (float) va.getAnimatedValue();
                    scrollTo(mScroller.getCurrX(), (int) y);
                }
            });
            va.start();
            postInvalidate();
        }
    }
    //</editor-fold>


}
