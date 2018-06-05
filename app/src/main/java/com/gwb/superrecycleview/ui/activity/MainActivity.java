package com.gwb.superrecycleview.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gwb.superrecycleview.R;
import com.gwb.superrecycleview.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getClass().getSimpleName();

        range(0);

        range(1);
        range(10);

        range(11);
        range(20);

        range(91);
        range(100);

        int color = R.color.colorAccent;
    }

    public void range(int num) {
        int pic = 0;
        if (num != 0) {
            int i = (num - 1) / 10;
            pic = i + 1;
        }
        System.out.println("图片" + pic + "\n");
    }

    @OnClick({R.id.btn_headerAndFooter, R.id.btn_sku, R.id.btn_steam, R.id.btn_shoppingGoods})
    public void onViewClicked(View view) {
        Class<MainActivity> mainActivityClass = MainActivity.class;
        switch (view.getId()) {
            case R.id.btn_headerAndFooter:
                startActivity(HeaderAndFooterActivity.class);
                break;
            case R.id.btn_sku:
                startActivity(SkuActivity.class);
                break;
            case R.id.btn_steam:
                startActivity(SteamActivity.class);
                break;
            case R.id.btn_shoppingGoods:
                startActivity(ShoppingGoodsActivity.class);
                break;
        }
    }

    public void startActivity(Class<? extends BaseActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}
