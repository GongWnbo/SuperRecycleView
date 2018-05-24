package com.gwb.superrecycleview.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.ui.BaseFitsSystemWindowsActivity;
import com.gwb.superrecycleview.ui.wedgit.RefreshLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleViewActivity extends BaseFitsSystemWindowsActivity implements BaseAdapter.BaseAdapterListener<String> {

    @BindView(R.id.rv)
    RecyclerView  mRv;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private List<String> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycle_view;
    }

    @Override
    protected String setTitle() {
        return "周半仙";
    }

    @Override
    protected void initView() {
        mToolbar.setBackgroundColor(Color.parseColor("#3D0000ff"));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initData();
        initAdapter();
    }

    private void initData() {
        for (int i = 0; i < 40; i++) {
            mList.add("我是数据" + i);
        }
    }

    private void initAdapter() {
        BaseAdapter adapter = new BaseAdapter(mList, R.layout.item_text, this);
        mRv.setAdapter(adapter);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void convert(BaseViewHolder holder, String str) {
        holder.setTitle(R.id.tv, str);
    }

}
