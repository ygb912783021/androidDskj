package dingshi.com.hibook.read.interfaces;


import dingshi.com.hibook.read.bean.TxtMsg;
import dingshi.com.hibook.read.main.TxtReaderContext;

/*
* create by bifan-wei
* 2017-11-13
*/
public interface TaskParcel {
    void onNextTask(TaskParcel parcel, TxtReaderContext readerContext);
    void onBack(TxtMsg msg);
}
