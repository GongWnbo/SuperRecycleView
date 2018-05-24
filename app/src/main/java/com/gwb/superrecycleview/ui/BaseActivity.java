package com.gwb.superrecycleview.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.utils.ActivityManager;
import com.gwb.superrecycleview.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

/**
 * Created by ${GongWenbo} on 2018/4/2 0002.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar  mToolbar;
    private   TextView mTv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setStatusBarTextLight(false);
        initToolBar(setTitle());
        initView();
        initData(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        Logger.d("Activities" + ActivityManager.getInstance().getActivities());
    }

    protected abstract int getLayoutId();

    protected abstract String setTitle();

    protected abstract void initView();

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTv_title.setText(title);
        }
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected void initToolBar(String title) {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("");
            mTv_title = findViewById(R.id.tv_title);
            if (!TextUtils.isEmpty(title) && mTv_title != null) {
                mTv_title.setText(title);
            }
            ImageView iv_back = findViewById(R.id.iv_back);
            if (iv_back != null) {
                iv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }
    }

    public void startActivity(Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void showToast(String content) {
        ToastUtil.showToast(this, content);
    }

    /**
     * 设置状态栏问题颜色（黑/白）
     *
     * @param isLight true:白色 false:黑色
     */
    protected void setStatusBarTextLight(boolean isLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | (isLight ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finish(this);
    }
}
