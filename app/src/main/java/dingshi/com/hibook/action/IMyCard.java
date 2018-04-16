package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.card.CardList;

/**
 * @author wangqi
 * @since 2018/3/8 14:33
 */

public interface IMyCard {
    void onCardList(CardList cardList);

    void onDeleteCard();

    void onError(String error);
}
