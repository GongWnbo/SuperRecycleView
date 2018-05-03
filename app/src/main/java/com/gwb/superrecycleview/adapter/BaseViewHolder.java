package com.gwb.superrecycleview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ${GongWenbo} on 2018/3/30 0030.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public static View              itemView;
    private        SparseArray<View> views;
    private        Context           mContext;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        this.views = new SparseArray<>();
        this.itemView = itemView;
    }

    public static BaseViewHolder createViewHolder(Context context, View itemView) {
        return new BaseViewHolder(context, itemView);
    }

    public static BaseViewHolder createViewHolder(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BaseViewHolder(context, view);
    }

    public View getConvertView() {
        return itemView;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View setTitle(@IdRes int viewId, String title) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setText(title);
        } else {
            throw new ClassCastException("you need give me TextView!");
        }
        return view;
    }

    public View setVisible(@IdRes int viewId) {
        return setVisible(viewId, true);
    }

    public View setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return view;
    }

    public void setBackgroundColor(@IdRes int viewId, String color) {
        View view = getView(viewId);
        view.setBackgroundColor(Color.parseColor(color));
    }

}
