package com.gwb.superrecycleview.ui.wedgit;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.gwb.superrecycleview.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 刷新的布局，针对RecycleView
 * Created by ${GongWenbo} on 2018/5/14 0014.
 */
public class RefreshLayout extends LinearLayout {

    //<editor-fold desc="属性变量 property and variable">
    private Context         mContext;
    private View            mHeaderView;
    private View            mFooterView;
    private int             mLastY;
    private Scroller        mScroller;
    private VelocityTracker mVelocityTracker;
    private int             mTouchSlop;
    private int             mMaximumVelocity;
    private int             mMinimumVelocity;
    private int             mPointerId;
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
        mHeaderView = LayoutInflater.from(mContext).inflate(R.layout.refresh_header, this, false);
        addView(mHeaderView, 0);

        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.refresh_footer, this, false);
        addView(mFooterView, getChildCount());
        setOrientation(VERTICAL);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //        int childCount = getChildCount();
        //        for (int i = 0; i < getChildCount(); i++) {
        //            View childView = getChildAt(i);
        //            if (childView == mHeaderView) {
        //                int height = childView.getMeasuredHeight();
        //                int top = childView.getTop();
        //                childView.layout(top - height, t, r, top + height);
        //            }
        //        }
    }

    //<editor-fold desc="滑动判断 judgement of slide">
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = (int) event.getY();
                // 如果没有完成，终止上一次的
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mPointerId = event.getPointerId(0);
                return true;
            case MotionEvent.ACTION_MOVE:
//                int scrollY = getScrollY();
//                if (scrollY <= 0) {
//                    return super.onTouchEvent(event);
//                }
                int deltaY = mLastY - y;
                scrollBy(0, deltaY);
                mLastY = y;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int yVelocity = (int) mVelocityTracker.getYVelocity(mPointerId);
                fling(-yVelocity);
                break;
        }
        return super.onTouchEvent(event);
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
            va.setDuration((long) (space * 1));
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
