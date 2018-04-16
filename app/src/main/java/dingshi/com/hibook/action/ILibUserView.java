package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.lib.LibIntro;

/**
 * @author wangqi
 * @since 2018/3/1 10:38
 */

public interface ILibUserView {
    void onUserList(LibIntro libIntro);
    void onDeleteUser();
    void onError(String error);
}
