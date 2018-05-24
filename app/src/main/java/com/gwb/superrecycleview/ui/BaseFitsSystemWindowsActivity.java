package com.gwb.superrecycleview.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by ${GongWenbo} on 2018/5/23 0023.
 */
public abstract class BaseFitsSystemWindowsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setHeight();
        }
    }

    public void setHeight() {
        // 获取actionbar的高度
        TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{
                android.R.attr.actionBarSize
        });
        float height = actionbarSizeTypedArray.getDimension(0, 0);
        // ToolBar的top值
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        lp.height = (int) (getStatusBarHeight(this) + height);
        mToolbar.setLayoutParams(lp);
    }

    private double getStatusBarHeight(Context context) {
        double statusBarHeight = Math.ceil(25 * context.getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

}
