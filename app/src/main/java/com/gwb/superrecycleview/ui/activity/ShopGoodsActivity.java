package com.gwb.superrecycleview.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
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

import javax.crypto.Cipher;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopGoodsActivity extends AppCompatActivity implements BaseAdapter.BaseAdapterListener<ShopGoodsBean> {

    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    int reduceLeft = 0;
    int addLeft    = 0;
    @BindView(R.id.fl)
    FrameLayout mFl;
    @BindView(R.id.iv_shopping_cart)
    ImageView   mIvShoppingCart;
    @BindView(R.id.tv_shopping_cart_count)
    TextView    mTvShoppingCartCount;
    private List<ShopGoodsBean> mGoodsList       = new ArrayList<>();
    // 贝塞尔曲线中间过程点坐标
    private float[]             mCurrentPosition = new float[2];
    private int goodsCount;
    private boolean shoppingCart = true;   // 购物车是否为空

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
            mGoodsList.add(new ShopGoodsBean(0));
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
                    goodsCount--;
                } else if (count < 0) {
                    // TODO: 2018/5/18 0018 如果用户点击过快
                    count = 0;
                } else {
                    goodsCount--;
                    tv_goods_count.setText(String.valueOf(count));
                }
                // 商品的数量是否显示
                if (goodsCount == 0) {
                    shoppingCart = true;
                    mTvShoppingCartCount.setVisibility(View.GONE);
                } else {
                    mTvShoppingCartCount.setText(String.valueOf(goodsCount));
                    mTvShoppingCartCount.setVisibility(View.VISIBLE);
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
                goodsCount++;
                if (shoppingCart) {
                    mTvShoppingCartCount.setVisibility(View.VISIBLE);
                    shoppingCart = false;
                }
                mTvShoppingCartCount.setText(String.valueOf(goodsCount));
                if (count == 1) {
                    iv_goods_reduce.setVisibility(View.VISIBLE);
                    animOpen(iv_goods_reduce);
                    ToastUtil.showToast(ShopGoodsActivity.this, "增加");
                }
                addGoods2CartAnim(iv_goods_add);
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

    public void addGoods2CartAnim(ImageView goodsImageView) {
        final ImageView goods = new ImageView(ShopGoodsActivity.this);
        goods.setImageResource(R.mipmap.ic_launcher);
        int size = Util.dp2px(ShopGoodsActivity.this, 16);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
        goods.setLayoutParams(lp);
        mFl.addView(goods);
        // 父布局的位置
        int[] parentLocation = new int[2];
        mFl.getLocationInWindow(parentLocation);
        // 加入点的位置起始点
        int[] startLocation = new int[2];
        goodsImageView.getLocationInWindow(startLocation);
        // 购物车的位置终点
        int[] endLocation = new int[2];
        mIvShoppingCart.getLocationInWindow(endLocation);

        int startX = startLocation[0] - parentLocation[0] + goodsImageView.getWidth() / 2;
        int startY = startLocation[1] - parentLocation[1] + goodsImageView.getHeight() / 2;

        int endX = endLocation[0] - parentLocation[0] + mIvShoppingCart.getWidth() / 5 * 3;
        int endY = endLocation[1] - parentLocation[1];
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
        valueAnimator.setDuration(500);
        valueAnimator.start();
        // 匀速线性插值器

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
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 移除图片
                mFl.removeView(goods);
                // 购物车数量增加
                mTvShoppingCartCount.setText(String.valueOf(goodsCount));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
