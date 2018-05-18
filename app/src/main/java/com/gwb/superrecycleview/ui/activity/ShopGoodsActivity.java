package com.gwb.superrecycleview.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopGoodsActivity extends AppCompatActivity implements BaseAdapter.BaseAdapterListener<String> {

    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    private List<String> mGoodsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_goods);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            mGoodsList.add("");
        }
    }

    private void initView() {
        BaseAdapter adapter = new BaseAdapter(mGoodsList, R.layout.item_shop_goods, this);
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mRvGoods.setAdapter(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, String str) {
        // 原价
        TextView tv_goods_original_price = holder.getView(R.id.tv_goods_original_price);
        Util.drawStrikethrough(tv_goods_original_price);
    }
}
