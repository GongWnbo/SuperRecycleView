package com.gwb.superrecycleview.imp;

import android.support.design.widget.AppBarLayout;

import com.orhanobut.logger.Logger;


/**
 * Created by ${GongWenbo} on 2018/5/21 0021.
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
        onStateChanged(i);
        Logger.d("滑动的的高度" + i);
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

    public void onStateChanged(int i) {
    }

    public enum State {
        EXPANDED,       // 展开状态
        COLLAPSED,      // 折叠状态
        IDLE            // 准备状态
    }
}