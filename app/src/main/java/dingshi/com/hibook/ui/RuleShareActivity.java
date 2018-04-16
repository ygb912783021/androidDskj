package dingshi.com.hibook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;

/**
 * @author wangqi
 * @since 2018/1/5 下午3:08
 */

public class RuleShareActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_rule_share;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true, "共享图书");
    }



}
