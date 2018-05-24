package com.gwb.superrecycleview.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.GoodsPropertyAdapter;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;
import com.gwb.superrecycleview.ui.BaseActivity;
import com.gwb.superrecycleview.ui.BaseFitsSystemWindowsActivity;
import com.gwb.superrecycleview.ui.wedgit.FlowLayout;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SteamActivity extends BaseFitsSystemWindowsActivity {

    @BindView(R.id.fl)
    FlowLayout mFl;
    private String[] arr = {"京东", "淘宝", "阿里巴巴", "dnf", "神舟七号", "外卖小哥", "马云？？？"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_steam;
    }

    @Override
    protected String setTitle() {
        return "Steam";
    }

    @Override
    protected void initView() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            TextView view = getView(arr[random.nextInt(arr.length)]);
            mFl.addView(view);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    public TextView getView(String msg) {
        TextView tv = new TextView(this);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.bottomMargin = 10;
        tv.setPadding(10, 10, 10, 10);
        tv.setLayoutParams(lp);
        tv.setTextSize(16);
        tv.setText(msg);
        tv.setBackgroundColor(getRandomColor());
        tv.setTextColor(getRandomColor());
        return tv;
    }


    public int getRandomColor() {
        Random random = new Random();
        String r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        String g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        String b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        int color = Color.parseColor("#" + r + g + b);
        return color;
    }

}
