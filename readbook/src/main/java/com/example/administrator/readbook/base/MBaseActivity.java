//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.base;

import com.example.administrator.readbook.base.impl.BaseActivity;

public abstract class MBaseActivity<T extends IPresenter> extends BaseActivity<T> {
    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }
}
