package com.gwb.superrecycleview.ui.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.entity.ShopGoodsBean;
import com.gwb.superrecycleview.utils.ToastUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ${GongWenbo} on 2018/5/22 0022.
 */
public class CartGoodsDialog extends BaseDialog implements BaseAdapter.BaseAdapterListener<ShopGoodsBean> {
    @BindView(R.id.rv_cart_goods)
    RecyclerView mRvCartGoods;
    List<ShopGoodsBean> list = new ArrayList<>();
    private BaseAdapter mAdapter;

    @Override
    protected void init() {
        initAdapter();
    }

    private void initAdapter() {
        for (int i = 0; i < 2; i++) {
            list.add(new ShopGoodsBean(i + 1));
        }
        if (list.size() >= 4) {
            ViewGroup.LayoutParams lp = mRvCartGoods.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = DensityUtil.dp2px(200);
            mRvCartGoods.setLayoutParams(lp);
        }
        mAdapter = new BaseAdapter(list, R.layout.item_cart_goods, this);
        mRvCartGoods.setLayoutManager(new LinearLayoutManager(mContext));
        mRvCartGoods.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_cart_goods;
    }

    @Override
    public float setAlpha() {
        return 0.3f;
    }

    @OnClick({R.id.view_shadow, R.id.tv_cart_goods_clear, R.id.tv_shopping_cart_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_shadow:
                dismiss();
                break;
            case R.id.tv_cart_goods_clear:
                break;
            case R.id.tv_shopping_cart_pay:
                ToastUtil.showToast(mContext, "去支付");
                break;
        }
    }

    @Override
    public void convert(final BaseViewHolder holder, final ShopGoodsBean shopGoodsBean) {
        int position = holder.getLayoutPosition();
        // 商品名
        holder.setTitle(R.id.tv_cart_goods_title, "小猪包套餐" + position);
        // 添加
        holder.getView(R.id.iv_cart_goods_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = shopGoodsBean.getCount();
                count++;
                shopGoodsBean.setCount(count);
                holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(count));
            }
        });
        // 减少
        holder.getView(R.id.iv_cart_goods_reduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = shopGoodsBean.getCount();
                count--;
                if (count == 0) {
                    int layoutPosition = holder.getLayoutPosition();
                    list.remove(layoutPosition);
                    mAdapter.notifyItemRemoved(layoutPosition);
                } else {
                    shopGoodsBean.setCount(count);
                    holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(count));
                }
            }
        });
        // 数量
        holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(shopGoodsBean.getCount()));
    }
}
