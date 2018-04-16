package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/19 16:32
 */

public interface ICreateCardView extends IRequestView<Result>{

    void onCardType(String content, String id);

    void onGender(String content, String id);


    void onBirth(String birth);

    void start();

}
