package com.gwb.superrecycleview.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.gwb.superrecycleview.entity.ShopGoods;
import com.gwb.superrecycleview.ui.activity.ShoppingGoodsActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * 购物车缓存的工具类
 * Created by GongWenBo on 2018/5/26.
 */

public class ShoppingCartHistoryManager {

    public static final  String SP_NAME     = "shops";   // SP的文件名
    private static final String PATH        = Environment.getExternalStorageDirectory().getPath() + File.separator + "goods";
    private static final String FILE_FORMAT = ".txt";
    private static ShoppingCartHistoryManager sShoppingCartHistoryManager;

    private ShoppingCartHistoryManager() {

    }

    public static ShoppingCartHistoryManager getInstance() {
        if (sShoppingCartHistoryManager == null) {
            synchronized (ShoppingCartHistoryManager.class) {
                if (sShoppingCartHistoryManager == null) {
                    sShoppingCartHistoryManager = new ShoppingCartHistoryManager();
                }
            }
        }
        return sShoppingCartHistoryManager;
    }

    /**
     * 添加商品缓存
     *
     * @param ShopId    商品的id
     * @param shopGoods 商品列表的对象
     */
    public ShoppingCartHistoryManager add(int ShopId, @NonNull ShopGoods shopGoods) {
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file.getAbsolutePath() + File.separator + ShopId + FILE_FORMAT);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(shopGoods);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 保存商铺选择的总个数
     *
     * @param context  上下文
     * @param ShopId   商铺id
     * @param allCount 总个数
     */
    public void putAllGoodsCount(@NonNull Context context, int ShopId, int allCount) {
        context.getSharedPreferences(SP_NAME, MODE_PRIVATE)
                .edit()
                .putInt(String.valueOf(ShopId), allCount)
                .commit();
    }

    /**
     * 获取商铺选择的总个数
     *
     * @param context 上下文
     * @param ShopId  商铺id
     * @return
     */
    public int getAllGoodsCount(@NonNull Context context, int ShopId) {
        return context.getSharedPreferences(SP_NAME, MODE_PRIVATE)
                .getInt(String.valueOf(ShopId), 0);
    }

    /**
     * 得到商品缓存
     *
     * @param ShopId 商铺的id
     */
    public ShopGoods get(int ShopId) {
        File file = new File(PATH);
        if (!file.exists()) {
            return null;
        }
        FileInputStream FileInputStream = null;
        ObjectInputStream objectInputStream = null;
        ShopGoods shopGoods = null;
        try {
            FileInputStream = new FileInputStream(file.getAbsolutePath() + File.separator + ShopId + FILE_FORMAT);
            objectInputStream = new ObjectInputStream(FileInputStream);
            shopGoods = (ShopGoods) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopGoods;
    }

    /**
     * 删除商铺缓存,如果数量为0
     *
     * @param ShopId 商铺的id
     */
    public ShoppingCartHistoryManager delete(@NonNull int ShopId) {
        File file = new File(PATH, +ShopId + FILE_FORMAT);
        if (file.exists()) {
            file.delete();
        }
        return this;
    }


}
