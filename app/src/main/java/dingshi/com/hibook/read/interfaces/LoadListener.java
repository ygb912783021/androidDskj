package dingshi.com.hibook.read.interfaces;


import dingshi.com.hibook.read.bean.TxtMsg;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface LoadListener {
    void onSuccess();
    void onFail(TxtMsg txtMsg);
}
