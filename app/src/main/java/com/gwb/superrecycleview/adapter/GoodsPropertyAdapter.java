package com.gwb.superrecycleview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;
import com.gwb.superrecycleview.ui.wedgit.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${GongWenbo} on 2018/3/30 0030.
 */

public class GoodsPropertyAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG          = "GoodsPropertyAdapter";
    private final        String COLOR_SELECT = "#ffffff";
    private final        String COLOR_EMPTY  = "#BBBBBB";
    private final        String COLOR_NORMAL = "#6D6D6D";

    private List<GoodsPropertyBean.AttributesBean> mAttributes;
    private List<GoodsPropertyBean.StockGoodsBean> mStockGoods;
    private Context                                mContext;
    private int                                    layoutId;
    private TextView[][]                           mTextViews;
    private List<String>                    selects = new ArrayList<>();
    private SimpleArrayMap<Integer, String> sam     = new SimpleArrayMap<>();

    public GoodsPropertyAdapter(List<GoodsPropertyBean.AttributesBean> attributes, List<GoodsPropertyBean.StockGoodsBean> stockGoods, Context context, @LayoutRes int layoutId) {
        this.mAttributes = attributes;
        this.mStockGoods = stockGoods;
        this.mContext = context;
        this.layoutId = layoutId;
        int size = attributes.size();
        mTextViews = new TextView[size][0];
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.createViewHolder(mContext, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        GoodsPropertyBean.AttributesBean attributesBean = mAttributes.get(position);
        // 标题
        holder.setTitle(R.id.tv_title, attributesBean.getTabName());
        // 一行具体的view
        FlowLayout flowLayout = holder.getView(R.id.flowLayout);
        List<String> attributesItem = attributesBean.getAttributesItem();
        int size = attributesItem.size();
        TextView[] textViews = new TextView[size];
        for (int i = 0; i < attributesItem.size(); i++) {
            final String property = attributesItem.get(i);
            TextView textView = getTextView(property, holder);
            flowLayout.addView(textView);
            textViews[i] = textView;
        }
        mTextViews[position] = textViews;
    }

    public TextView getTextView(final String title, final BaseViewHolder holder) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView tv = new TextView(mContext);
        lp.setMargins(10, 10, 10, 10);
        tv.setPadding(40, 20, 40, 20);
        tv.setBackgroundResource(R.drawable.normal);
        tv.setTextColor(Color.parseColor(COLOR_NORMAL));
        tv.setLayoutParams(lp);
        tv.setText(title);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                Log.d(TAG, "position== " + position);
                TextView[] textViews = mTextViews[position];
                // TODO: 2018/5/4 0004 现在偷懒，默认把所有的都给初始化，后面考虑上一个的
                for (TextView textView : textViews) {
                    textView.setBackgroundResource(R.drawable.normal);
                    textView.setTextColor(Color.parseColor(COLOR_NORMAL));
                }
                sam.put(position, title);

                for (int i = 0; i < mAttributes.size(); i++) {
                    String title = sam.get(i);
                    if (!TextUtils.isEmpty(title)) {
                        for (int j = 0; j < mStockGoods.size(); j++) {
                            GoodsPropertyBean.StockGoodsBean stockGoodsBean = mStockGoods.get(j);
                            List<GoodsPropertyBean.StockGoodsBean.GoodsInfoBean> goodsInfo = stockGoodsBean.getGoodsInfo();
                            List<String> titles = new ArrayList<>();
                            for (int k = 0; k < goodsInfo.size(); k++) {
                                if (i == k) {
                                    GoodsPropertyBean.StockGoodsBean.GoodsInfoBean goodsInfoBean = goodsInfo.get(k);
                                    String tabValue = goodsInfoBean.getTabValue();
                                    titles.add(tabValue);
                                }
                            }
                            TextView[] textView = mTextViews[position];
                            for (int t = 0; t < textView.length; t++) {
                                TextView tv = textView[t];
                                String str = tv.getText().toString();
                                if (titles.contains(str)) {
                                    tv.setBackgroundResource(R.drawable.normal);
                                    tv.setTextColor(Color.parseColor(COLOR_NORMAL));
                                    tv.setEnabled(true);
                                } else {
                                    tv.setBackgroundResource(R.drawable.empty);
                                    tv.setTextColor(Color.parseColor(COLOR_EMPTY));
                                    tv.setEnabled(false);
                                }
                            }
                        }
                    }
                }

                Log.d(TAG, "sam: " + sam.toString());

                tv.setBackgroundResource(R.drawable.select);
                tv.setTextColor(Color.parseColor(COLOR_SELECT));
            }
        });
        return tv;
    }

    @Override
    public int getItemCount() {
        return mAttributes == null ? 0 : mAttributes.size();
    }

}
