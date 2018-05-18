package com.gwb.superrecycleview.utils;

import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by ${GongWenbo} on 2018/5/18 0018.
 */
public class Util {

    // 下划线
    public static void drawUnderline(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    // 中划线
    public static void drawStrikethrough(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
