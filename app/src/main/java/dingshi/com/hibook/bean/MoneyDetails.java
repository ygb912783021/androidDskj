package dingshi.com.hibook.bean;

import java.util.List;

/**
 * @author wangqi
 * @since 2018/1/5 14:02
 */

public class MoneyDetails extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * amount : 1
         * out_trade_no : RTN20180108173540
         * order_info : {"pay_at":"","borrow_day":"","serial_number":"","book_isbn":"","case_name":"","belong_user_id":""}
         * channel : 2
         * remark :
         * created_at : 2018-01-08 17:35:46
         */

        private double amount;
        private String out_trade_no;
        private OrderInfoBean order_info;
        private int channel;
        private String remark;
        private String created_at;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public OrderInfoBean getOrder_info() {
            return order_info;
        }

        public void setOrder_info(OrderInfoBean order_info) {
            this.order_info = order_info;
        }

        public int getChannel() {
            return channel;
        }

        public void setChannel(int channel) {
            this.channel = channel;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public static class OrderInfoBean {
            /**
             * pay_at :
             * borrow_day :
             * serial_number :
             * book_isbn :
             * case_name :
             * belong_user_id :
             */

            private String pay_at;
            private String borrow_day;
            private String serial_number;
            private String book_isbn;
            private String case_name;
            private String belong_user_id;
            private String case_address;

            public String getCase_address() {
                return case_address;
            }

            public void setCase_address(String case_address) {
                this.case_address = case_address;
            }


            public String getPay_at() {
                return pay_at;
            }

            public void setPay_at(String pay_at) {
                this.pay_at = pay_at;
            }

            public String getBorrow_day() {
                return borrow_day;
            }

            public void setBorrow_day(String borrow_day) {
                this.borrow_day = borrow_day;
            }

            public String getSerial_number() {
                return serial_number;
            }

            public void setSerial_number(String serial_number) {
                this.serial_number = serial_number;
            }

            public String getBook_isbn() {
                return book_isbn;
            }

            public void setBook_isbn(String book_isbn) {
                this.book_isbn = book_isbn;
            }

            public String getCase_name() {
                return case_name;
            }

            public void setCase_name(String case_name) {
                this.case_name = case_name;
            }

            public String getBelong_user_id() {
                return belong_user_id;
            }

            public void setBelong_user_id(String belong_user_id) {
                this.belong_user_id = belong_user_id;
            }
        }
    }
}
