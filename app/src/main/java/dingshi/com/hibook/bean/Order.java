package dingshi.com.hibook.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import dingshi.com.hibook.share.PayWeixin;

/**
 * @author wangqi
 * @since 2017/11/14 16:18
 */

public class Order extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         * out_trade_no : OTN20180102094818
         * user_id : 526741470357491713
         * trade_platform : 1
         * serial_number : su120011
         * payment_status : 1
         * comment_status : 0
         * pay_fee : 1
         * returned : 0
         * pay_at : 2018-01-02 09:48:30
         * pickup_status : 0
         * book : {"isbn":9787559609519,"spare_isbn":"0","name":"蓝色小药丸","author":"[瑞士] 弗雷德里克·佩特斯","translator":"陈帅,易立","press":"后浪丨北京联合出版公司","publish_time":"2017-11","price":45,"page_number":208,"douban_grade":9.1,"grade":0,"grade_number":0,"douban_grade_number":353,"cover":"https://img3.doubanio.com/mpic/s29559634.jpg"}
         * borrow_day : 1
         * borrow_interval : 2018-01-02 到 2018-01-03
         * borrow_surplus_day : 0
         * bookcase_address : 同济大学男生宿舍
         * sign : {"partnerId":"","prepayId":"","package":"","nonceStr":"","timeStamp":"","sign":"Z4rU5t25GEuDJayU0G9Y9RDbrijaP5aZWHCm9uI3RiBG6RYXD+Ok3/OvJ4Eag=="}
         * <p>
         * <p>
         * remark  : 留言
         *
         * bookcase_cell_id  : 柜子编号
         */

        String bookcase_cell_id;
        String remark;
        private String out_trade_no;
        private String user_id;
        private int trade_platform;
        private String serial_number;
        private int payment_status;
        private int comment_status;
        private String pay_fee;
        private int returned;
        private String pay_at;
        private int pickup_status;
        private BookDetails.JsonDataBean book;
        private int borrow_day;
        private String borrow_interval;
        private int borrow_surplus_day;
        private String bookcase_address;
        private PayWeixin sign;

        public String getBookcase_cell_id() {
            return bookcase_cell_id;
        }

        public void setBookcase_cell_id(String bookcase_cell_id) {
            this.bookcase_cell_id = bookcase_cell_id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getTrade_platform() {
            return trade_platform;
        }

        public void setTrade_platform(int trade_platform) {
            this.trade_platform = trade_platform;
        }

        public String getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(String serial_number) {
            this.serial_number = serial_number;
        }

        public int getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(int payment_status) {
            this.payment_status = payment_status;
        }

        public int getComment_status() {
            return comment_status;
        }

        public void setComment_status(int comment_status) {
            this.comment_status = comment_status;
        }

        public String getPay_fee() {
            return pay_fee;
        }

        public void setPay_fee(String pay_fee) {
            this.pay_fee = pay_fee;
        }

        public int getReturned() {
            return returned;
        }

        public void setReturned(int returned) {
            this.returned = returned;
        }

        public String getPay_at() {
            return pay_at;
        }

        public void setPay_at(String pay_at) {
            this.pay_at = pay_at;
        }

        public int getPickup_status() {
            return pickup_status;
        }

        public void setPickup_status(int pickup_status) {
            this.pickup_status = pickup_status;
        }

        public BookDetails.JsonDataBean getBook() {
            return book;
        }

        public void setBook(BookDetails.JsonDataBean book) {
            this.book = book;
        }

        public int getBorrow_day() {
            return borrow_day;
        }

        public void setBorrow_day(int borrow_day) {
            this.borrow_day = borrow_day;
        }

        public String getBorrow_interval() {
            return borrow_interval;
        }

        public void setBorrow_interval(String borrow_interval) {
            this.borrow_interval = borrow_interval;
        }

        public int getBorrow_surplus_day() {
            return borrow_surplus_day;
        }

        public void setBorrow_surplus_day(int borrow_surplus_day) {
            this.borrow_surplus_day = borrow_surplus_day;
        }

        public String getBookcase_address() {
            return bookcase_address;
        }

        public void setBookcase_address(String bookcase_address) {
            this.bookcase_address = bookcase_address;
        }

        public PayWeixin getSign() {
            return sign;
        }

        public void setSign(PayWeixin sign) {
            this.sign = sign;
        }


    }
}
