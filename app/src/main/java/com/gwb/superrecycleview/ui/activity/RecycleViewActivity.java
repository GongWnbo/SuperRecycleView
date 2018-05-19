package com.gwb.superrecycleview.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleViewActivity extends AppCompatActivity implements BaseAdapter.BaseAdapterListener<String> {

    @BindView(R.id.rv)
    RecyclerView mRv;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        ButterKnife.bind(this);
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
        mRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void convert(BaseViewHolder holder, String str) {
        holder.setTitle(R.id.tv, str);
    }
}
