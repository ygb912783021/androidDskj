package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/11/9 10:52
 */

public class User extends Result {


    private JsonDataBean jsonData;

    private String token = "";


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }


    public static class JsonDataBean {
        /**
         * openid :
         * platform_type : 3
         * user_id : 491823389864890376
         * sex : 0
         * nick_name : 15055407294
         * avatar :
         * balance : 0  余额
         * mobile : 15055407294
         * birthday: "2017-11-16",
         * province :
         * city :
         * cert_status :  押金 没0  交过1
         * disable : 0
         * signable : 1
         * created_at : 2017-11-09 14:05:16
         * token : a15da4b74b8c15d8220e41c5323a6ef886a5a814
         * foregift : 用户押金的钱是多少
         * read_num   读书量
         * book_num   藏书量
         * balance_refund_status  押金的状态  0、无状态  1、审核中
         */

        private String read_num;
        private String book_num;
        private String openid;
        private int platform_type;
        private String user_id;
        private int sex;
        private String nick_name;
        private String avatar;
        private String balance;
        private String mobile;
        private String province;
        private String city;
        private int cert_status;
        private int disable;
        private int signable;
        private String created_at;
        private String foregift;
        private int balance_refund_status;

        public int getBalance_refund_status() {
            return balance_refund_status;
        }

        public void setBalance_refund_status(int balance_refund_status) {
            this.balance_refund_status = balance_refund_status;
        }

        public String getRead_num() {
            return read_num;
        }

        public void setRead_num(String read_num) {
            this.read_num = read_num;
        }

        public String getBook_num() {
            return book_num;
        }

        public void setBook_num(String book_num) {
            this.book_num = book_num;
        }

        public String getForegift() {
            return foregift;
        }

        public void setForegift(String foregift) {
            this.foregift = foregift;
        }

        private String birthday;


        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public int getPlatform_type() {
            return platform_type;
        }

        public void setPlatform_type(int platform_type) {
            this.platform_type = platform_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCert_status() {
            return cert_status;
        }

        public void setCert_status(int cert_status) {
            this.cert_status = cert_status;
        }

        public int getDisable() {
            return disable;
        }

        public void setDisable(int disable) {
            this.disable = disable;
        }

        public int getSignable() {
            return signable;
        }

        public void setSignable(int signable) {
            this.signable = signable;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }


        @Override
        public String toString() {
            return "JsonDataBean{" +
                    "read_num='" + read_num + '\'' +
                    ", book_num='" + book_num + '\'' +
                    ", openid='" + openid + '\'' +
                    ", platform_type=" + platform_type +
                    ", user_id='" + user_id + '\'' +
                    ", sex=" + sex +
                    ", nick_name='" + nick_name + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", balance='" + balance + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", cert_status='" + cert_status + '\'' +
                    ", disable=" + disable +
                    ", signable=" + signable +
                    ", created_at='" + created_at + '\'' +
                    ", foregift='" + foregift + '\'' +
                    ", birthday='" + birthday + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "jsonData=" + jsonData +
                ", token='" + token + '\'' +
                '}';
    }
}
