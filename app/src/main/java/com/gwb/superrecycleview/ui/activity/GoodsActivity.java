package com.gwb.superrecycleview.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.GoodsPropertyAdapter;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;
import com.gwb.superrecycleview.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsActivity extends AppCompatActivity implements GoodsPropertyAdapter.GoodsSelectListener {
    private static final String TAG = "GoodsActivity";
    @BindView(R.id.rv_show)
    RecyclerView mRvShow;
    @BindView(R.id.tv_goods)
    TextView     mTvGoods;
    StringBuilder sb = new StringBuilder();
    private List<GoodsPropertyBean.AttributesBean> mAttributes;
    private String remind = "未选择";

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
        mAttributes = bean.getAttributes();
        List<GoodsPropertyBean.StockGoodsBean> stockGoods = bean.getStockGoods();
        GoodsPropertyAdapter adapter = new GoodsPropertyAdapter(mAttributes, stockGoods, this, R.layout.item_goods);
        mRvShow.setLayoutManager(new LinearLayoutManager(this));
        mRvShow.setAdapter(adapter);
        adapter.setGoodsSelectListener(this);

        sb.append("商品属性");
        for (GoodsPropertyBean.AttributesBean attribute : mAttributes) {
            String tabName = attribute.getTabName();
            sb.append(" ").append(tabName).append(" : ").append(remind);
        }
        mTvGoods.setText(sb.toString());
    }

    @Override
    public void select(HashMap<Integer, String> sam) {
        sb.setLength(0);
        sb.append("商品属性");
        for (int i = 0; i < mAttributes.size(); i++) {
            GoodsPropertyBean.AttributesBean attributesBean = mAttributes.get(i);
            String tabName = attributesBean.getTabName();
            String title = sam.get(i);
            if (TextUtils.isEmpty(title)) {
                title = remind;
            }
            sb.append(" ").append(tabName).append(" : ").append(title);
        }
        mTvGoods.setText(sb.toString());
        Log.d(TAG, "select: " + sam.toString());
    }

    @OnClick(R.id.btn_order)
    public void onViewClicked() {
        String title = mTvGoods.getText().toString();
        if (title.contains(remind)) {
            ToastUtil.showToast(this, "请选择商品");
        } else {
            ToastUtil.showToast(this, "下单成功");
        }
    }

}
