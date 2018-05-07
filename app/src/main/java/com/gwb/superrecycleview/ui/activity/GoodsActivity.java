package com.gwb.superrecycleview.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.GoodsPropertyAdapter;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsActivity extends AppCompatActivity {
    @BindView(R.id.rv_show)
    RecyclerView mRvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Gson gson = new Gson();
        GoodsPropertyBean bean = gson.fromJson(getString(R.string.jsonData), GoodsPropertyBean.class);
        List<GoodsPropertyBean.AttributesBean> attributes = bean.getAttributes();
        List<GoodsPropertyBean.StockGoodsBean> stockGoods = bean.getStockGoods();
        GoodsPropertyAdapter adapter = new GoodsPropertyAdapter(attributes, stockGoods, this, R.layout.item_goods);
        mRvShow.setLayoutManager(new LinearLayoutManager(this));
        mRvShow.setAdapter(adapter);
    }

}
