package com.gwb.superrecycleview.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.adapter.HeaderAndFooterWrapper;
import com.gwb.superrecycleview.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BaseAdapter.BaseAdapterListener<String>, BaseAdapter.OnItemClickListener<String> {

    @BindView(R.id.rv_show)
    RecyclerView mRvShow;
    private List<String> mList = new ArrayList<>();
    private BaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initAdapter();
    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            mList.add("我是数据" + i);
        }
    }

    private void initAdapter() {
        mAdapter = new BaseAdapter(mList, this, R.layout.item, this);
        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

        // header
        TextView tv1 = new TextView(this);
        tv1.setText("header 1");
        headerAndFooterWrapper.addHeader(tv1);
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.mipmap.ic_launcher);
        headerAndFooterWrapper.addHeader(iv1);
        TextView tv2 = new TextView(this);
        tv2.setText("header 2");
        headerAndFooterWrapper.addHeader(tv2);

        // footer
        TextView tv11 = new TextView(this);
        tv11.setText("footer 1");
        headerAndFooterWrapper.addFooter(tv11);
        TextView tv12 = new TextView(this);
        tv12.setText("footer 2");
        headerAndFooterWrapper.addFooter(tv12);


        mRvShow.setLayoutManager(new GridLayoutManager(this,3));
        mRvShow.setAdapter(headerAndFooterWrapper);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void convert(BaseViewHolder holder, String str) {
        holder.setTitle(R.id.tv, str);
    }

    @Override
    public void onItemClick(String str, int position) {
        ToastUtil.showToast(this, str + "位置" + position);
    }
}
