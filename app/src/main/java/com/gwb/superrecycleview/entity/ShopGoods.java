package com.gwb.superrecycleview.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GongWenBo on 2018/5/26.
 */

public class ShopGoods implements Serializable {

    private ArrayList<ShopGoodsBean> mList = new ArrayList<>();

    public ShopGoods(ArrayList<ShopGoodsBean> list) {
        mList = list;
    }

    public ArrayList<ShopGoodsBean> getList() {
        return mList;
    }

    public void setList(ArrayList<ShopGoodsBean> list) {
        mList = list;
    }
}
