package com.gwb.superrecycleview.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.BaseAdapter;
import com.gwb.superrecycleview.adapter.BaseViewHolder;
import com.gwb.superrecycleview.entity.ShopGoods;
import com.gwb.superrecycleview.entity.ShopGoodsBean;
import com.gwb.superrecycleview.entity.StoreGoodsBean;
import com.gwb.superrecycleview.imp.AppBarStateChangeListener;
import com.gwb.superrecycleview.ui.BaseActivity;
import com.gwb.superrecycleview.ui.dialog.DiscountCouponDialog;
import com.gwb.superrecycleview.ui.dialog.ShoppingCartDialog;
import com.gwb.superrecycleview.utils.ShoppingCartHistoryManager;
import com.gwb.superrecycleview.utils.ToastUtil;
import com.gwb.superrecycleview.utils.Util;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class ShoppingGoodsActivity extends BaseActivity implements BaseAdapter.BaseAdapterListener<ShopGoodsBean> {

    private static final long TIME = 300;   // 动画的执行时间
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    int reduceLeft = 0;
    int addLeft    = 0;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.iv_shopping_cart)
    ImageView         mIvShoppingCart;
    @BindView(R.id.tv_shopping_cart_count)
    TextView          mTvShoppingCartCount;
    @BindView(R.id.appBarLayout)
    AppBarLayout      mAppBarLayout;
    @BindView(R.id.tv_title)
    TextView          mTvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout    mRlHeader;
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private ArrayList<ShopGoodsBean> mGoodsList       = new ArrayList<>();
    // 贝塞尔曲线中间过程点坐标
    private float[]                  mCurrentPosition = new float[2];
    private int         allCount;
    private BaseAdapter mAdapter;
    private int    RC_CAMERA_AND_LOCATION = 0x1;
    // 商品的id
    private String SHOP_ID                = "12";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_goods;
    }

    @Override
    protected String setTitle() {
        return "ShoppingGoods";
    }

    private void initToolbar() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    // 展开状态
                    mTvTitle.setText("");
                    mRlHeader.setVisibility(View.VISIBLE);
                } else if (state == State.COLLAPSED) {
                    // 折叠状态
                    mTvTitle.setText("芭比馒头");
                    mRlHeader.setVisibility(View.GONE);
                } else {
                    mTvTitle.setText("");
                    mRlHeader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStateChanged(int i) {
                float height = mRlHeader.getHeight();
                float alpha = i / height;
                Logger.d("透明度" + (1 - Math.abs(alpha)));
                mRlHeader.setAlpha(1 - Math.abs(alpha));
            }
        });
    }

    public void initHeight() {
        // ToolBar的top值
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        lp.topMargin = (int) getStatusBarHeight(this);
        mToolbar.setLayoutParams(lp);
        // AppBarLayout的高度
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mAppBarLayout.getLayoutParams();
        layoutParams.height = DensityUtil.dp2px(200) + (int) getStatusBarHeight(this);
        mAppBarLayout.setLayoutParams(layoutParams);
    }

    private double getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initData() {
        int id = 0x100;
        HashMap<String, Integer> hashMap = ShoppingCartHistoryManager.getInstance().get(SHOP_ID);
        this.allCount = ShoppingCartHistoryManager.getInstance().getAllGoodsCount(SHOP_ID);
        showToast("商品总数" + allCount);
        // 根据缓存是否显示
        mTvShoppingCartCount.setVisibility(allCount == 0 ? View.GONE : View.VISIBLE);
        mTvShoppingCartCount.setText(String.valueOf(allCount));
        // TODO: 2018/6/5 0005 模拟请求到的数据
        for (int i = 0; i < 10; i++) {
            mGoodsList.add(new ShopGoodsBean(0, "小猪包套餐" + i, id++ + ""));
        }
        if (hashMap != null) {
            for (ShopGoodsBean bean : mGoodsList) {
                String goodsId = bean.getGoodsId();
                if (hashMap.containsKey(goodsId)) {
                    Integer count = hashMap.get(goodsId);
                    bean.setCount(count);
                }
            }
        }
    }

    @Override
    protected void initView() {
        EasyPermissions.requestPermissions(this, null, RC_CAMERA_AND_LOCATION, perms);

        initData();
        initToolbar();
        initHeight();
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new BaseAdapter(mGoodsList, R.layout.item_shop_goods, this);
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mRvGoods.setAdapter(mAdapter);
        mRvGoods.setItemAnimator(null);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void convert(final BaseViewHolder holder, ShopGoodsBean bean) {
        // 原价
        TextView tv_goods_original_price = holder.getView(R.id.tv_goods_original_price);
        // 中划线
        Util.drawStrikethrough(tv_goods_original_price);
        // 商品数
        final TextView tv_goods_count = holder.getView(R.id.tv_goods_count);
        // 减少
        final ImageView iv_goods_reduce = holder.getView(R.id.iv_goods_reduce);
        iv_goods_reduce.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取减少图标的位置
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
                // 防止过快点击出现多个关闭动画
                if (count == 0) {
                    animClose(iv_goods_reduce);
                    tv_goods_count.setText("");
                    // 考虑到用户点击过快
                    allCount--;
                } else if (count < 0) {
                    // 防止过快点击出现商品数为负数
                    count = 0;
                } else {
                    allCount--;
                    tv_goods_count.setText(String.valueOf(count));
                }
                // 商品的数量是否显示
                if (allCount <= 0) {
                    allCount = 0;
                    mTvShoppingCartCount.setVisibility(View.GONE);
                } else {
                    mTvShoppingCartCount.setText(String.valueOf(allCount));
                    mTvShoppingCartCount.setVisibility(View.VISIBLE);
                }
                shopGoodsBean.setCount(count);
            }
        });
        // 增加
        final ImageView iv_goods_add = holder.getView(R.id.iv_goods_add);
        iv_goods_add.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取增加图标的位置
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
                allCount++;
                if (allCount > 0) {
                    mTvShoppingCartCount.setVisibility(View.VISIBLE);
                }
                mTvShoppingCartCount.setText(String.valueOf(allCount));
                if (count == 1) {
                    iv_goods_reduce.setVisibility(View.VISIBLE);
                    animOpen(iv_goods_reduce);
                }
                addGoods2CartAnim(iv_goods_add);
                tv_goods_count.setText(String.valueOf(count));
                shopGoodsBean.setCount(count);
            }
        });
        int count = bean.getCount();
        tv_goods_count.setText(count == 0 ? "" : String.valueOf(count));
        iv_goods_reduce.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);
        // 标题
        holder.setTitle(R.id.tv_goods_title, bean.getGoods());
    }

    public void animOpen(final ImageView imageView) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(imageView, "translationX", addLeft - reduceLeft, 0);
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageView, "rotation", 0, 180);
        animatorSet.play(translationAnim).with(rotationAnim);
        animatorSet.setDuration(TIME).start();
    }

    public void animClose(final ImageView imageView) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator translationAnim = ObjectAnimator.ofFloat(imageView, "translationX", 0, addLeft - reduceLeft);
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageView, "rotation", 0, 180);
        animatorSet.play(translationAnim).with(rotationAnim);
        animatorSet.setDuration(TIME).start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO: 2018/5/19 因为属性动画会改变位置,所以当结束的时候,要回退的到原来的位置,同时用补间动画的位移不好控制
                ObjectAnimator oa = ObjectAnimator.ofFloat(imageView, "translationX", addLeft - reduceLeft, 0);
                oa.setDuration(0);
                oa.start();
                imageView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 贝塞尔曲线动画
     *
     * @param goodsImageView
     */
    public void addGoods2CartAnim(ImageView goodsImageView) {
        final ImageView goods = new ImageView(ShoppingGoodsActivity.this);
        goods.setImageResource(R.mipmap.icon_goods_add);
        int size = Util.dp2px(ShoppingGoodsActivity.this, 24);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
        goods.setLayoutParams(lp);
        mCoordinatorLayout.addView(goods);
        // 控制点的位置
        int[] recyclerLocation = new int[2];
        mCoordinatorLayout.getLocationInWindow(recyclerLocation);
        // 加入点的位置起始点
        int[] startLocation = new int[2];
        goodsImageView.getLocationInWindow(startLocation);
        // 购物车的位置终点
        int[] endLocation = new int[2];
        mIvShoppingCart.getLocationInWindow(endLocation);
        // TODO: 2018/5/21 0021 考虑到状态栏的问题，不然会往下偏移状态栏的高度
        int startX = startLocation[0] - recyclerLocation[0];
        int startY = startLocation[1] - recyclerLocation[1];
        // TODO: 2018/5/21 0021 和上面一样
        int endX = endLocation[0] - recyclerLocation[0];
        int endY = endLocation[1] - recyclerLocation[1];
        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点位置(即贝塞尔曲线的起点)
        path.moveTo(startX, startY);
        // 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + endX) / 2, startY, endX, endY);
        // mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，如果是true，path会形成一个闭环
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        // 计算距离
        int tempX = Math.abs(startX - endX);
        int tempY = Math.abs(startY - endY);
        // 根据距离计算时间
        int time = (int) (0.3 * Math.sqrt((tempX * tempX) + tempY * tempY));
        valueAnimator.setDuration(time);
        valueAnimator.start();
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                // mCurrentPosition此时就是中间距离点的坐标值
                pathMeasure.getPosTan(value, mCurrentPosition, null);
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 移除图片
                mCoordinatorLayout.removeView(goods);
                // 购物车数量增加
                mTvShoppingCartCount.setText(String.valueOf(allCount));
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.layout_shopping_cart, R.id.tv_shopping_cart_pay, R.id.tv_goods_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.iv_back:
                ToastUtil.showToast(ShoppingGoodsActivity.this, "返回");
                break;
            // 购物车的弹窗
            case R.id.layout_shopping_cart:
                shoppingCartDialog();
                break;
            // 支付
            case R.id.tv_shopping_cart_pay:
                pay();
                break;
            // 领取
            case R.id.tv_goods_get:
                showDiscountCouponDialog();
                break;
        }
    }

    public void shoppingCartDialog() {
        ShoppingCartDialog dialog = new ShoppingCartDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ShoppingCartDialog.CART_GOODS, mGoodsList);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "cartGoods");
        dialog.setCartGoodsDialogListener(new ShoppingCartDialog.CartGoodsDialogListener() {
            @Override
            public void add(int allCount, ShopGoodsBean shopGoodsBean) {
                notifyItemChanged(allCount, shopGoodsBean);
            }

            @Override
            public void reduce(int allCount, ShopGoodsBean shopGoodsBean) {
                notifyItemChanged(allCount, shopGoodsBean);
            }

            @Override
            public void clear() {
                clearShoppingGoods();
            }
        });
    }

    public void showDiscountCouponDialog() {
        DiscountCouponDialog dialog = new DiscountCouponDialog();
        dialog.show(getFragmentManager(), "discountCoupon", true);
    }

    public void pay() {
        String remind = "购物车中空空如也";
        if (allCount > 0) {
            remind = "去支付";
        }
        ToastUtil.showToast(ShoppingGoodsActivity.this, remind);
    }

    public void notifyItemChanged(int allCount, ShopGoodsBean shopGoodsBean) {
        this.allCount = allCount;
        mTvShoppingCartCount.setVisibility(allCount == 0 ? View.GONE : View.VISIBLE);
        mTvShoppingCartCount.setText(String.valueOf(allCount));
        // 遍历，格局id查找对象
        for (int i = 0; i < mGoodsList.size(); i++) {
            ShopGoodsBean bean = mGoodsList.get(i);
            String goodsId = bean.getGoodsId();
            if (goodsId.equals(shopGoodsBean.getGoodsId())) {
                bean = shopGoodsBean;
                mAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    public void clearShoppingGoods() {
        allCount = 0;
        mTvShoppingCartCount.setVisibility(View.GONE);
        for (ShopGoodsBean bean : mGoodsList) {
            bean.setCount(0);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (allCount != 0) {
            HashMap<String, Integer> hashMap = new HashMap<>();
            StoreGoodsBean storeGoodsBean = new StoreGoodsBean(hashMap);
            for (ShopGoodsBean bean : mGoodsList) {
                int count = bean.getCount();
                String goodsId = bean.getGoodsId();
                if (count != 0) {
                    hashMap.put(goodsId, count);
                }
            }
            ShoppingCartHistoryManager.getInstance().add(SHOP_ID, storeGoodsBean);
        } else {
            ShoppingCartHistoryManager.getInstance().delete(SHOP_ID);
        }
    }
}
