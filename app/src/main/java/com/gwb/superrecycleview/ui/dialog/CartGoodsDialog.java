package com.gwb.superrecycleview.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.entity.ShopGoodsBean;
import com.gwb.superrecycleview.ui.activity.ShopGoodsActivity;
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
    public static final String CART_GOODS = "cartGoods";
    @BindView(R.id.rv_cart_goods)
    RecyclerView mRvCartGoods;
    List<ShopGoodsBean> list = new ArrayList<>();
    @BindView(R.id.tv_shopping_cart_count)
    TextView mTvShoppingCartCount;
    private BaseAdapter             mAdapter;
    private int                     allCount;
    private CartGoodsDialogListener mCartGoodsDialogListener;

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<ShopGoodsBean> cartGoodsList = (List<ShopGoodsBean>) bundle.getSerializable(CART_GOODS);
            // 将没有个数的商品移除
            for (int i = 0; i < cartGoodsList.size(); i++) {
                ShopGoodsBean shopGoodsBean = cartGoodsList.get(i);
                int count = shopGoodsBean.getCount();
                if (count != 0) {
                    list.add(shopGoodsBean);
                    allCount += count;
                }
            }
        }
        if (allCount > 0) {
            mTvShoppingCartCount.setVisibility(View.VISIBLE);
            mTvShoppingCartCount.setText(String.valueOf(allCount));
        }
        initAdapter();
    }

    private void initAdapter() {
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
                ShoppingCartDialog dialog = new ShoppingCartDialog();
                dialog.show(getFragmentManager(), "shoppingCart");
                dialog.setShoppingCartDialogListener(new ShoppingCartDialog.ShoppingCartDialogListener() {
                    @Override
                    public void clear() {
                        list.clear();
                        mAdapter.notifyDataSetChanged();
                        if (mCartGoodsDialogListener != null) {
                            mCartGoodsDialogListener.clear();
                            allCount = 0;
                            mTvShoppingCartCount.setVisibility(View.GONE);
                        }
                    }
                });
                break;
            case R.id.tv_shopping_cart_pay:
                ToastUtil.showToast(mContext, "去支付");
                break;
        }
    }

    @Override
    public void convert(final BaseViewHolder holder, final ShopGoodsBean shopGoodsBean) {
        // 商品名
        holder.setTitle(R.id.tv_cart_goods_title, shopGoodsBean.getGoods());
        // 添加
        holder.getView(R.id.iv_cart_goods_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = shopGoodsBean.getCount();
                count++;
                shopGoodsBean.setCount(count);
                holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(count));
                allCount++;
                mTvShoppingCartCount.setText(String.valueOf(allCount));
                if (mCartGoodsDialogListener != null) {
                    mCartGoodsDialogListener.add(allCount, shopGoodsBean);
                }
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
                    holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(count));
                }
                shopGoodsBean.setCount(count);
                allCount--;
                if (allCount <= 0) {
                    allCount = 0;
                    mTvShoppingCartCount.setVisibility(View.GONE);
                }
                mTvShoppingCartCount.setText(String.valueOf(allCount));
                if (mCartGoodsDialogListener != null) {
                    mCartGoodsDialogListener.reduce(allCount, shopGoodsBean);
                }
            }
        });
        // 数量
        holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(shopGoodsBean.getCount()));
    }

    public void setCartGoodsDialogListener(CartGoodsDialogListener cartGoodsDialogListener) {
        mCartGoodsDialogListener = cartGoodsDialogListener;
    }

    public interface CartGoodsDialogListener {

        void add(int allCount, ShopGoodsBean shopGoodsBean);

        void reduce(int allCount, ShopGoodsBean shopGoodsBean);

        void clear();

    }

}
