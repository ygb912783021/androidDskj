//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.example.administrator.readbook.base.observer;


import com.example.administrator.readbook.utils.NetworkUtil;

public class SimpleObserClass<T> {
    private int code;
    private T t;

    public SimpleObserClass(T t){
        this(t, NetworkUtil.SUCCESS);
    }

    public SimpleObserClass(T t,int code){
        this.t = t;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean success(){
        return code == NetworkUtil.SUCCESS;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
