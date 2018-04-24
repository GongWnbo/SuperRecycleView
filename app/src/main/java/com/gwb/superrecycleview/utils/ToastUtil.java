package com.gwb.superrecycleview.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;


/**
 * Created by ${GongWenbo} on 2018/4/13 0013.
 */

public class ToastUtil {

    private static final String THREAD_MAIN = "main";
    private static          Context sContext;
    private static          String  sContent;
    private volatile static Toast   sToast;
    /**
     * 在主线程中做操作
     */
    private volatile static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            toast(sContext, sContent);
            sToast.show();
        }
    };

    public static void showToast(Context context, String content) {
        String name = Thread.currentThread().getName();
        // 判断是否是主线程，如果不是通过handler
        if (!name.equals(THREAD_MAIN)) {
            mHandler.sendEmptyMessage(0);
            sContext = context;
            sContent = content;
        } else {
            toast(context, content);
            sToast.show();
        }
    }

    private static void toast(Context context, String content) {
        if (sToast == null) {
            sToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(content);
        }
    }

}
