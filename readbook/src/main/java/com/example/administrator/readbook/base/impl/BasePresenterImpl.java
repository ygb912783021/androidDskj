package com.example.administrator.readbook.base.impl;

import android.support.annotation.NonNull;

import com.example.administrator.readbook.base.IPresenter;
import com.example.administrator.readbook.base.IView;


public abstract class BasePresenterImpl<T extends IView> implements IPresenter {
    protected T mView;

    @Override
    public void attachView(@NonNull IView iView) {
        mView = (T) iView;
    }
}
