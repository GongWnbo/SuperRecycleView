package com.gwb.superrecycleview.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ${GongWenbo} on 2018/3/30 0030.
 */

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T>             mDatas;
    private Context             mContext;
    private int                 layoutId;
    private BaseAdapterListener mBaseAdapterListener;
    private OnItemClickListener mOnItemClickListener;

    public BaseAdapter(List<T> datas, Context context, @LayoutRes int layoutId, BaseAdapterListener baseAdapterListener) {
        this.mDatas = datas;
        this.mContext = context;
        this.layoutId = layoutId;
        this.mBaseAdapterListener = baseAdapterListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.createViewHolder(mContext, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (mBaseAdapterListener != null) {
            mBaseAdapterListener.convert(holder, mDatas.get(position));
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mDatas.get(position), position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T t, int position);
    }

    public interface BaseAdapterListener<T> {
        void convert(BaseViewHolder holder, T t);
    }

}
