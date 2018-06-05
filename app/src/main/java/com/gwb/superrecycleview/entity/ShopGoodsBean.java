package com.gwb.superrecycleview.entity;

import java.io.Serializable;

/**
 * Created by ${GongWenbo} on 2018/5/18 0018.
 */
public class ShopGoodsBean implements Serializable {

    private int    count;
    private String goods;
    private String    goodsId;

    public ShopGoodsBean(int count, String goods, String goodsId) {
        this.count = count;
        this.goods = goods;
        this.goodsId = goodsId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGoods() {
        return goods == null ? "" : goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "count=" + count + ",goods=" + goods;
    }
}
