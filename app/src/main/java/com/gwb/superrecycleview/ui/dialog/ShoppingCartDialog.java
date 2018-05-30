package com.gwb.superrecycleview.ui.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class ShoppingCartDialog extends BaseDialog implements BaseAdapter.BaseAdapterListener<ShopGoodsBean> {
    public static final  String CART_GOODS = "cartGoods";
    private static final long   TIME       = 300;       // 动画的时间
    @BindView(R.id.rv_cart_goods)
    RecyclerView mRvCartGoods;
    List<ShopGoodsBean> list = new ArrayList<>();
    @BindView(R.id.tv_shopping_cart_count)
    TextView     mTvShoppingCartCount;
    @BindView(R.id.ll_shopping_cart)
    LinearLayout mLlShoppingCart;
    private BaseAdapter             mAdapter;
    private int                     allCount;
    private CartGoodsDialogListener mCartGoodsDialogListener;
    private int                     mHeightPixels;

    @Override
    protected void init() {
        initData();
        initAdapter();
        initScreen();
        // 打开的动画
        openAnim();
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<ShopGoodsBean> cartGoodsList = (List<ShopGoodsBean>) bundle.getSerializable(CART_GOODS);
            // 将没有个数的商品移除，只是为了模拟数据，网络数据看情况
            for (int i = 0; i < cartGoodsList.size(); i++) {
                ShopGoodsBean shopGoodsBean = cartGoodsList.get(i);
                int count = shopGoodsBean.getCount();
                if (count != 0) {
                    list.add(shopGoodsBean);
                    allCount += count;
                }
            }
        }
        // 如果个数大于0，那么要显示商品数
        if (allCount > 0) {
            mTvShoppingCartCount.setVisibility(View.VISIBLE);
            mTvShoppingCartCount.setText(String.valueOf(allCount));
        }
    }

    private void initScreen() {
        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 获取屏幕的高度
        mHeightPixels = dm.heightPixels;
    }

    public void openAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mLlShoppingCart, "translationY", mHeightPixels, 0);
        objectAnimator.setDuration(TIME);
        objectAnimator.start();
    }

    public void closeAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mLlShoppingCart, "translationY", 0, mHeightPixels);
        objectAnimator.setDuration(TIME);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dismiss();
            }
        });
    }

    private void initAdapter() {
        // 如果商品个数大于指定数时,高度写死,其他wrap_content
        if (list.size() >= 4) {
            ViewGroup.LayoutParams lp = mRvCartGoods.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = DensityUtil.dp2px(200);
            mRvCartGoods.setLayoutParams(lp);
        }
        mAdapter = new BaseAdapter(list, R.layout.item_cart_goods, this);
        mRvCartGoods.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvCartGoods.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_shopping_cart;
    }

    @Override
    public float setAlpha() {
        return 0.3f;
    }

    @Override
    public int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @OnClick({R.id.view_shadow, R.id.ll_cart_goods_clear, R.id.tv_shopping_cart_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 点击阴影消失
            case R.id.view_shadow:
                closeAnim();
                break;
            // 清空购物车
            case R.id.ll_cart_goods_clear:
                if (allCount > 0) {
                    clearCartGoodsDialog();
                }else {
                    ToastUtil.showToast(mActivity, "购物车中空空如也");
                }
                break;
            // 去支付
            case R.id.tv_shopping_cart_pay:
                pay();
                break;
        }
    }

    public void pay() {
        String remind = "购物车中空空如也";
        if (allCount > 0) {
            remind = "去支付";
        }
        ToastUtil.showToast(mActivity, remind);
    }

    public void clearCartGoodsDialog() {
        ClearShoppingCartDialog dialog = new ClearShoppingCartDialog();
        dialog.show(getFragmentManager(), "shoppingCart");
        dialog.setShoppingCartDialogListener(new ClearShoppingCartDialog.ShoppingCartDialogListener() {
            @Override
            public void clear() {
                list.clear();
                mAdapter.notifyDataSetChanged();
                if (mCartGoodsDialogListener != null) {
                    mCartGoodsDialogListener.clear();
                    allCount = 0;
                    mTvShoppingCartCount.setVisibility(View.GONE);
                }
                dismiss();
            }
        });
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
                allCount++;
                shopGoodsBean.setCount(count);
                holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(count));
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
                allCount--;
                if (count == 0) {
                    int layoutPosition = holder.getLayoutPosition();
                    list.remove(layoutPosition);
                    mAdapter.notifyItemRemoved(layoutPosition);
                } else {
                    holder.setTitle(R.id.tv_cart_goods_count, String.valueOf(count));
                }
                shopGoodsBean.setCount(count);
                // 防止点击过快
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
