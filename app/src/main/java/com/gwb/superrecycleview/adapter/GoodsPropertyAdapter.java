package com.gwb.superrecycleview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.entity.GoodsPropertyBean;
import com.gwb.superrecycleview.ui.wedgit.FlowLayout;

import java.util.List;

/**
 * Created by ${GongWenbo} on 2018/3/30 0030.
 */

public class GoodsPropertyAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<GoodsPropertyBean.AttributesBean> mAttributes;
    private List<GoodsPropertyBean.StockGoodsBean> mStockGoods;
    private Context                                mContext;
    private int                                    layoutId;
    private TextView[][]                           mTextViews;

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
        for (int j = 0; j < attributesItem.size(); j++) {
            String property = attributesItem.get(j);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(mContext);
            lp.setMargins(5, 5, 5, 5);
            tv.setPadding(10, 10, 10, 10);
            tv.setBackgroundColor(Color.GRAY);
            tv.setLayoutParams(lp);
            tv.setText(property);
            // 添加
            flowLayout.addView(tv);
        }
    }

    @Override
    public int getItemCount() {
        return mAttributes == null ? 0 : mAttributes.size();
    }

}
