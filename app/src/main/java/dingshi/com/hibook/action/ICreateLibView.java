package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.lib.LibCreate;

/**
 * @author wangqi
 * @since 2017/12/25 10:45
 */

public interface ICreateLibView {
    void onSuccess(LibCreate result);

    void onSelectType(String type, String content);

    void onSelectCheck(String type, String content);

    void onSelectPsw(String type, String content);

    void onError(String str);

    void start();
}
