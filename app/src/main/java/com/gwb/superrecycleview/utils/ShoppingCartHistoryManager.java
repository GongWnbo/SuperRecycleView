package com.gwb.superrecycleview.utils;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.gwb.superrecycleview.entity.ShopGoods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 购物车缓存的工具类
 * Created by GongWenBo on 2018/5/26.
 */

public class ShoppingCartHistoryManager {

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

    private static final String PATH        = Environment.getExternalStorageDirectory().getPath() + File.separator + "goods";
    private static final String FILE_FORMAT = ".txt";

    /**
     * 添加商品缓存
     *
     * @param id        商品的id
     * @param shopGoods 商品列表的对象
     */
    public void add(@NonNull int id, ShopGoods shopGoods) {
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file.getAbsolutePath() + File.separator + id + FILE_FORMAT);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(shopGoods);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到商品缓存
     *
     * @param id 商品的id
     */
    public ShopGoods get(@NonNull int id) {
        File file = new File(PATH);
        if (!file.exists()) {
            return null;
        }
        FileInputStream FileInputStream = null;
        ObjectInputStream objectInputStream = null;
        ShopGoods shopGoods = null;
        try {
            FileInputStream = new FileInputStream(file.getAbsolutePath() + File.separator + id + FILE_FORMAT);
            objectInputStream = new ObjectInputStream(FileInputStream);
            shopGoods = (ShopGoods) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shopGoods;
    }

    /**
     * 删除商品缓存,如果数量为0
     *
     * @param id 商品的id
     */
    public void delete(@NonNull int id) {
        File file = new File(PATH, +id + FILE_FORMAT);
        if (file.exists()) {
            file.delete();
        }
    }


}
