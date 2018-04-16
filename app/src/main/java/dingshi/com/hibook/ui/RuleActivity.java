package dingshi.com.hibook.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dingshi.com.hibook.R;
import dingshi.com.hibook.base.BaseActivity;

/**
 * @author wangqi
 * @since 2017/12/22 下午5:03
 */


public class RuleActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_rule;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        requestActionBarStyle(true,"借书规则说明","");

    }
}
