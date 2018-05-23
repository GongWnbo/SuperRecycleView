package com.gwb.superrecycleview.ui.dialog;

import com.gwb.superrecycleview.R;

/**
 * Created by ${GongWenbo} on 2018/5/23 0023.
 */
public class DiscountCouponDialog extends BaseDialog {

    @Override
    protected void init() {
        setCancelable(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_discount_coupon;
    }


}
