package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/28 11:11
 * <p>
 * 我的借阅接口
 */

public class Borrows extends Result {


    private List<JsonDataBean> jsonData;


    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         * isbn : 9787544769839
         * out_trade_no :      订单号
         * name : 早上九点叫醒我    图书名
         * grade : 0        图书评分
         * returned_at   返回类型   0.未还，1.已还
         * serial_number :
         * cover : https://img1.doubanio.com/mpic/s29607939.jpg
         * owner_user_id : 0    拥有者id
         * pay_fee : 1    支付的价格
         * pay_at : 2017-12-15 15:53:11
         * is_personal : 1    是从个人还是书柜借书  0是个人 1是书柜
         * trading_state    0不同意   1是同意
         * borrow_day : 10    借阅天数
         * borrow_interval : 2017-12-15 到 2017-12-25   借阅天数文案
         * borrow_surplus_day : -3   到期天数，负数表示逾期
         * pickup  是都被借出  0、没   1是借出
         * cell_id  柜子的编号
         */
        private int cell_id;
        private int pickup;
        private String isbn;
        private String out_trade_no;
        private String name;
        private int grade;
        private String serial_number;
        private String cover;
        private String owner_user_id;
        private String pay_fee;
        private String pay_at;
        private int is_personal;
        private int trading_state;

        public int getTrading_state() {
            return trading_state;
        }

        public void setTrading_state(int trading_state) {
            this.trading_state = trading_state;
        }

        private int borrow_day;
        private String borrow_interval;
        private int borrow_surplus_day;
        private String returned_at;

        public String getReturned_at() {
            return returned_at;
        }

        public void setReturned_at(String returned_at) {
            this.returned_at = returned_at;
        }

        public int getCell_id() {
            return cell_id;
        }

        public void setCell_id(int cell_id) {
            this.cell_id = cell_id;
        }

        public int getPickup() {
            return pickup;
        }

        public void setPickup(int pickup) {
            this.pickup = pickup;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getSerial_number() {
            return serial_number;
        }

        public void setSerial_number(String serial_number) {
            this.serial_number = serial_number;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getOwner_user_id() {
            return owner_user_id;
        }

        public void setOwner_user_id(String owner_user_id) {
            this.owner_user_id = owner_user_id;
        }

        public String getPay_fee() {
            return pay_fee;
        }

        public void setPay_fee(String pay_fee) {
            this.pay_fee = pay_fee;
        }

        public String getPay_at() {
            return pay_at;
        }

        public void setPay_at(String pay_at) {
            this.pay_at = pay_at;
        }

        public int getIs_personal() {
            return is_personal;
        }

        public void setIs_personal(int is_personal) {
            this.is_personal = is_personal;
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
    }
}
