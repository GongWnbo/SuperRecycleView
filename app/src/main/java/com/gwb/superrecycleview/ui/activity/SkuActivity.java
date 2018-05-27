package com.gwb.superrecycleview.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.adapter.GoodsPropertyAdapter;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;
import com.gwb.superrecycleview.ui.BaseFitsSystemWindowsActivity;
import com.gwb.superrecycleview.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SkuActivity extends BaseFitsSystemWindowsActivity implements GoodsPropertyAdapter.GoodsSelectListener {
    private static final String TAG = "SkuActivity";
    @BindView(R.id.rv_show)
    RecyclerView mRvShow;
    @BindView(R.id.tv_goods)
    TextView     mTvGoods;
    StringBuilder sb = new StringBuilder();
    @BindView(R.id.tv_count)
    TextView  mTvCount;
    @BindView(R.id.iv)
    ImageView mIv;
    private List<GoodsPropertyBean.AttributesBean> mAttributes;
    private String       remind = "未选择";
    private List<String> mList  = new ArrayList<>();
    private List<GoodsPropertyBean.StockGoodsBean> mStockGoods;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sku;
    }

    @Override
    protected String setTitle() {
        return "SKU";
    }

    @Override
    protected void initView() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        init();
        // 水印
        watermark();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    private void watermark() {
        // TODO: 2018/5/10 0010
        String pic = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=161888459,1712714238&fm=27&gp=0.jpg";
        Glide.with(this).asBitmap().load(pic).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap bitmap = createWatermark(resource, "叶应是叶" + "http://blog.csdn.net/new_one_object");
                mIv.setImageBitmap(bitmap);
            }
        });

        //        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher);
        //        BitmapDrawable bd = (BitmapDrawable) drawable;
        //        Bitmap bmp = bd.getBitmap();
        //        Bitmap bitmap = createWatermark(bmp, "叶应是叶" + "http://blog.csdn.net/new_one_object");
        //        mIv.setImageBitmap(bitmap);
    }

    private Bitmap createWatermark(Bitmap bitmap, String mark) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint();
        // 水印颜色
        p.setColor(Color.YELLOW);
        // 水印字体大小
        p.setTextSize(20);
        //抗锯齿
        p.setAntiAlias(true);
        //绘制图像
        canvas.drawBitmap(bitmap, 0, 0, p);
        //绘制文字
        canvas.drawText(mark, 0, h / 2, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bmp;
    }

    private void init() {
        Gson gson = new Gson();
        GoodsPropertyBean bean = gson.fromJson(getString(R.string.jsonData), GoodsPropertyBean.class);
        mAttributes = bean.getAttributes();
        mStockGoods = bean.getStockGoods();
        GoodsPropertyAdapter adapter = new GoodsPropertyAdapter(mAttributes, mStockGoods, this, R.layout.item_goods);
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
        mList.clear();
        sb.setLength(0);
        sb.append("商品属性");
        for (int i = 0; i < mAttributes.size(); i++) {
            GoodsPropertyBean.AttributesBean attributesBean = mAttributes.get(i);
            String tabName = attributesBean.getTabName();
            String title = sam.get(i);
            if (TextUtils.isEmpty(title)) {
                title = remind;
            } else {
                mList.add(title);
            }
            sb.append(" ").append(tabName).append(" : ").append(title);
        }
        mTvGoods.setText(sb.toString());
        Log.d(TAG, "select: " + sam.toString());
        // TODO: 2018/5/7 0007 如果都选择了
        if (!sb.toString().contains(remind)) {
            int goodsID = -1;
            int count = -1;
            for (GoodsPropertyBean.StockGoodsBean stockGood : mStockGoods) {
                List<GoodsPropertyBean.StockGoodsBean.GoodsInfoBean> goodsInfo = stockGood.getGoodsInfo();
                boolean flag = false;
                for (int i = 0; i < goodsInfo.size(); i++) {
                    GoodsPropertyBean.StockGoodsBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(i);
                    if (!mList.contains(goodsInfoBean.getTabValue())) {
                        break;
                    }
                    if (i == goodsInfo.size() - 1) {
                        flag = true;
                    }
                    if (flag) {
                        goodsID = stockGood.getGoodsID();
                        count = stockGood.getGoodsCount();
                    }
                }
            }
            mTvCount.setText("商品数量:" + count + "\t商品id" + goodsID);
        } else {
            mTvCount.setText("商品数量");
        }
    }

    @OnClick(R.id.btn_order)
    public void onViewClicked() {
        String title = mTvGoods.getText().toString();
        if (title.contains(remind)) {
            ToastUtil.showToast(this, "请选择商品");
        } else
            ToastUtil.showToast(this, "下单成功");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
