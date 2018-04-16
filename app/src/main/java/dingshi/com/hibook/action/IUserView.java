package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.User;

/**
 * @author wangqi
 * @since 2017/11/9 17:54
 */

public interface IUserView {

    void updateNick(String nick);

    void updateGender(String gender);

    void updateBorn(String born);

    void updateAvatar(User avatar);

    void error(String error);

}
