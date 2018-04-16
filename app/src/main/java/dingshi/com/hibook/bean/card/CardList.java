package dingshi.com.hibook.bean.card;

import java.util.List;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/26 14:23
 */

public class CardList extends Result {

    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * id : 1       名片id
         * name : 3123   姓名
         * avatar :   头像地址
         * position : 132123    职位
         * identify : 0   是否认证,0.未认证,1.已认证
         * is_main:  0不是  1是 主名片
         */

        private String id;
        private String name;
        private String avatar;
        private String position;
        private int identify;
        private int is_main;
        private String company;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getIs_main() {
            return is_main;
        }

        public void setIs_main(int is_main) {
            this.is_main = is_main;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public int getIdentify() {
            return identify;
        }

        public void setIdentify(int identify) {
            this.identify = identify;
        }
    }
}
