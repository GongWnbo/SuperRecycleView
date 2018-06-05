package com.gwb.superrecycleview.entity;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${GongWenbo} on 2018/6/4 0004.
 */
public class StoreGoodsBean implements Serializable {

  private int allCount;                         // 商品总数

  private HashMap<String, Integer> mHashMap;    // 每件商品的个数

  public StoreGoodsBean(HashMap<String, Integer> hashMap) {
    this.mHashMap = hashMap;
    for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
      Integer value = entry.getValue();
      if (value != 0) {
        allCount += value;
      }
    }
  }

  public int getAllCount() {
    return allCount;
  }

  public HashMap<String, Integer> getHashMap() {
    return mHashMap;
  }

}
