package com.gwb.superrecycleview.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.entity.ShopGoodsBean;
import com.gwb.superrecycleview.utils.ToastUtil;
import com.gwb.superrecycleview.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopGoodsActivity extends AppCompatActivity implements BaseAdapter.BaseAdapterListener<ShopGoodsBean> {

    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    int reduceLeft = 0;
    int addLeft    = 0;
    private List<ShopGoodsBean> mGoodsList = new ArrayList<>();
    //    private SparseArray<Integer> mSparseArrayCount = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_goods);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mGoodsList.add(new ShopGoodsBean(i));
        }
    }

    private void initView() {
        BaseAdapter adapter = new BaseAdapter(mGoodsList, R.layout.item_shop_goods, this);
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mRvGoods.setAdapter(adapter);
    }

    @Override
    public void convert(final BaseViewHolder holder, ShopGoodsBean bean) {
        // 原价
        TextView tv_goods_original_price = holder.getView(R.id.tv_goods_original_price);
        Util.drawStrikethrough(tv_goods_original_price);
        // TODO: 2018/5/18 0018 动画效果
        // 商品数
        final TextView tv_goods_count = holder.getView(R.id.tv_goods_count);
        // 减少
        final ImageView iv_goods_reduce = holder.getView(R.id.iv_goods_reduce);
        iv_goods_reduce.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                reduceLeft = iv_goods_reduce.getLeft();
                iv_goods_reduce.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        iv_goods_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                ShopGoodsBean shopGoodsBean = mGoodsList.get(position);
                int count = shopGoodsBean.getCount();
                count--;
                if (count == 0) {
                    animClose(iv_goods_reduce);
                    tv_goods_count.setText("");
                } else {
                    tv_goods_count.setText(String.valueOf(count));
                }
                shopGoodsBean.setCount(count);
                ToastUtil.showToast(ShopGoodsActivity.this, "减少");
            }
        });
        // 增加
        final ImageView iv_goods_add = holder.getView(R.id.iv_goods_add);
        iv_goods_add.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addLeft = iv_goods_add.getLeft();
                iv_goods_add.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        iv_goods_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                ShopGoodsBean shopGoodsBean = mGoodsList.get(position);
                int count = shopGoodsBean.getCount();
                count++;
                if (count == 1) {
                    iv_goods_reduce.setVisibility(View.VISIBLE);
                    animOpen(iv_goods_reduce);
                    ToastUtil.showToast(ShopGoodsActivity.this, "增加");
                }
                tv_goods_count.setText(String.valueOf(count));
                shopGoodsBean.setCount(count);
            }
        });
        int count = bean.getCount();
        tv_goods_count.setText(count == 0 ? "" : String.valueOf(count));
        iv_goods_reduce.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);
    }

    public void animOpen(ImageView imageView) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(imageView, "translationX", addLeft - reduceLeft, 0);
        oa.setDuration(300);
        oa.start();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageView, "rotation", 0, 180);
        oa1.setDuration(300);
        oa1.start();
    }

    public void animClose(ImageView imageView) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(imageView, "translationX", 0, addLeft - reduceLeft);
        oa.setDuration(300);
        oa.start();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageView, "rotation", 0, 180);
        oa1.setDuration(300);
        oa1.start();
    }
}
