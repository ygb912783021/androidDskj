package dingshi.com.hibook.bean.lib;

import java.util.List;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/25 14:32
 */

public class LibIntro extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * user_id : 514930342958731266
         * is_creator : 1
         * card_id : 0
         * name :
         * avatar :
         * total : 0
         * share : 0
         * phone ： 150
         * <p>
         * //书友会
         * "company": "璟薇",
         * "position": "ASP",
         * "introduce": "哈哈哈",
         * "advantage": "",
         * "total": 13,
         * "share": 13,
         * "is_follow": 0
         * <p>
         * <p>
         * //通讯录
         * int sex;
         * String email;
         * String birthday;
         * String follow_at;  交换时间
         */

        private String user_id;
        private int is_creator;
        private String card_id;
        private String name;
        private String avatar;
        private int total;
        private int share;
        private String phone;


        private String company;
        private String position;
        private String introduce;
        private int is_follow;
        private String advantage;

        private int sex;
        private String email;
        private String birthday;
        private String follow_at;


        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getFollow_at() {
            return follow_at;
        }

        public void setFollow_at(String follow_at) {
            this.follow_at = follow_at;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }

        public String getAdvantage() {
            return advantage;
        }

        public void setAdvantage(String advantage) {
            this.advantage = advantage;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getIs_creator() {
            return is_creator;
        }

        public void setIs_creator(int is_creator) {
            this.is_creator = is_creator;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getShare() {
            return share;
        }

        public void setShare(int share) {
            this.share = share;
        }
    }
}
