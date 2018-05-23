package com.gwb.superrecycleview.ui.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * Created by ${GongWenbo} on 2018/4/2 0002.
 */

public abstract class BaseDialog extends DialogFragment {

    private static final int TIME = 600;
    protected View               mView;
    protected Activity           mActivity;
    private   BaseDialogListener mBaseDialogListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
            if (mBaseDialogListener != null) {
                mBaseDialogListener.dismiss();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.dimAmount = setAlpha();
        window.setGravity(setGravity());
        window.setAttributes(windowAttributes);
    }

    /**
     * 位置
     *
     * @return
     */
    public int setGravity() {
        return Gravity.CENTER;
    }

    /**
     * 如果要设置阴影
     *
     * @return
     */
    public float setAlpha() {
        return 0.0f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//设置dialog没有title
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0f);
        mView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mView);
        init();
        return mView;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = context;
    }

    protected abstract void init();

    protected abstract int getLayoutId();

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void show(FragmentManager manager, String tag) {
        show(manager, tag, false);
    }

    public void show(FragmentManager manager, String tag, boolean autoHide) {
        if (autoHide) {
            mHandler.sendEmptyMessageDelayed(0, TIME);
        }
        super.show(manager, tag);
    }

    public void setBaseDialogListener(BaseDialogListener baseDialogListener) {
        mBaseDialogListener = baseDialogListener;
    }

    public interface BaseDialogListener {
        void dismiss();
    }
}
