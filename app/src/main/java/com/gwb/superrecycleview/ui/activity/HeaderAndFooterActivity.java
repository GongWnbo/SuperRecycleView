package com.gwb.superrecycleview.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.adapter.HeaderAndFooterWrapper;
import com.gwb.superrecycleview.adapter.OnItemClickListener;
import com.gwb.superrecycleview.ui.BaseFitsSystemWindowsActivity;
import com.gwb.superrecycleview.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HeaderAndFooterActivity extends BaseFitsSystemWindowsActivity implements BaseAdapter.BaseAdapterListener<String>, OnItemClickListener<String> {

    @BindView(R.id.rv_show)
    RecyclerView mRvShow;
    private List<String> mList = new ArrayList<>();
    private BaseAdapter            mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_header_and_footer;
    }

    @Override
    protected String setTitle() {
        return "HeaderAndFooter";
    }

    @Override
    protected void initView() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        initAdapter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    private void initAdapter() {
        mAdapter = new BaseAdapter(mList, R.layout.item, this);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

        for (int i = 0; i < 20; i++) {
            mList.add("我是数据" + i);
        }

        // header
        TextView tv1 = new TextView(this);
        tv1.setText("header 1");
        mHeaderAndFooterWrapper.addHeader(tv1);
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.mipmap.ic_launcher);
        mHeaderAndFooterWrapper.addHeader(iv1);
        TextView tv2 = new TextView(this);
        tv2.setText("header 2");
        mHeaderAndFooterWrapper.addHeader(tv2);

        // footer
        TextView tv11 = new TextView(this);
        tv11.setText("footer 1");
        mHeaderAndFooterWrapper.addFooter(tv11);
        TextView tv12 = new TextView(this);
        tv12.setText("footer 2");
        mHeaderAndFooterWrapper.addFooter(tv12);


        mRvShow.setLayoutManager(new GridLayoutManager(this, 3));
        mRvShow.setAdapter(mHeaderAndFooterWrapper);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void convert(BaseViewHolder holder, String str) {
        holder.setTitle(R.id.tv, str);
    }

    @OnClick({R.id.btn_linear, R.id.btn_grid, R.id.btn_stagger})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_linear:
                mRvShow.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.btn_grid:
                mRvShow.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.btn_stagger:
                mRvShow.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
        // TODO: 2018/4/24 0024 只有在adapter初始化的时候会调用，切换的时候导致内部设置的方法失效
        mHeaderAndFooterWrapper.onAttachedToRecyclerView(mRvShow);
    }

    @Override
    public void onItemClick(int position, String s) {
        ToastUtil.showToast(this, "位置" + position);
    }
}
