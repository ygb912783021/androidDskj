package dingshi.com.hibook.ui;

import android.os.Bundle;

import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;

public class AddNewADddressActivity extends BaseActivity {
    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"新增收货地址","保存");}

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_new_address;
    }
}
