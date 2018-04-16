package dingshi.com.hibook.action;

import dingshi.com.hibook.bean.Payment;

/**
 * @author wangqi
 * @since 2017/12/14 14:22
 */

public interface IPayMent {
    void onAli(Payment orderInfo);

    void onWx(Payment orderInfo);

    void onMoney(Payment payment);

    void onCoupon();

    void onError(String str);

    void start();
}
