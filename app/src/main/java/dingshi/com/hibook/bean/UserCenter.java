package dingshi.com.hibook.bean;

import java.io.Serializable;

/**
 * @author wangqi
 * @since 2018/1/9 14:31
 */

public class UserCenter extends Result {


    /**
     * jsonData : {"order_num":0,"no_pickup_num":5,"coupon_num":1,"invite_code":"eb95c2947f3340e11ea3844de85a56ac","revised":0}
     */

    private JsonDataBean jsonData;

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         * order_num : 0   订单数量
         * no_pickup_num : 5   借阅数量
         * coupon_num : 1    优惠券数量
         * invite_code : eb95c2947f3340e11ea3844de85a56ac   邀请码
         * revised : 0   邀请码是否已改   0是没有   1是有
         * balance_refund_status  是否审核中
         * share_book_num  分享的书
         * book_num  总共的书
         */

        private String order_num;
        private int no_pickup_num;
        private String coupon_num;
        private String invite_code;
        private int revised;
        private int balance_refund_status;
        private int share_book_num;
        private int book_num;

        public int getShare_book_num() {
            return share_book_num;
        }

        public void setShare_book_num(int share_book_num) {
            this.share_book_num = share_book_num;
        }

        public int getBook_num() {
            return book_num;
        }

        public void setBook_num(int book_num) {
            this.book_num = book_num;
        }

        public int getBalance_refund_status() {
            return balance_refund_status;
        }

        public void setBalance_refund_status(int balance_refund_status) {
            this.balance_refund_status = balance_refund_status;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public int getNo_pickup_num() {
            return no_pickup_num;
        }

        public void setNo_pickup_num(int no_pickup_num) {
            this.no_pickup_num = no_pickup_num;
        }

        public String getCoupon_num() {
            return coupon_num;
        }

        public void setCoupon_num(String coupon_num) {
            this.coupon_num = coupon_num;
        }

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public int getRevised() {
            return revised;
        }

        public void setRevised(int revised) {
            this.revised = revised;
        }
    }
}
