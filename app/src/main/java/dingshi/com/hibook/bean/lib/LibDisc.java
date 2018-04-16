package dingshi.com.hibook.bean.lib;

import java.util.List;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/25 18:35
 */

public class LibDisc extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * type : 1
         * name : 测试图书馆
         * is_check : 1
         * describe : aaa
         * is_encrypt : 1
         * icon : http://testapi.linkbooker.com/files/catalog/eb95c2947f3340e11ea3844de85a56ac_add.jpg
         * is_open : 1
         * is_consult : 1
         * is_allow : 1
         * is_join : 1
         * message_state : 1
         * fake_user : 700
         * user_limit : 100
         * created_at : 2017-12-25
         * catalog_id : 1
         * user_total : 1
         */

        private int type;
        private String name;
        private int is_check;
        private String describe;
        private int is_encrypt;
        private String icon;
        private int is_open;
        private int is_consult;
        private int is_allow;
        private int is_join;
        private int message_state;
        private int fake_user;
        private int user_limit;
        private String created_at;
        private String catalog_id;
        private int user_total;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getIs_encrypt() {
            return is_encrypt;
        }

        public void setIs_encrypt(int is_encrypt) {
            this.is_encrypt = is_encrypt;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getIs_open() {
            return is_open;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public int getIs_consult() {
            return is_consult;
        }

        public void setIs_consult(int is_consult) {
            this.is_consult = is_consult;
        }

        public int getIs_allow() {
            return is_allow;
        }

        public void setIs_allow(int is_allow) {
            this.is_allow = is_allow;
        }

        public int getIs_join() {
            return is_join;
        }

        public void setIs_join(int is_join) {
            this.is_join = is_join;
        }

        public int getMessage_state() {
            return message_state;
        }

        public void setMessage_state(int message_state) {
            this.message_state = message_state;
        }

        public int getFake_user() {
            return fake_user;
        }

        public void setFake_user(int fake_user) {
            this.fake_user = fake_user;
        }

        public int getUser_limit() {
            return user_limit;
        }

        public void setUser_limit(int user_limit) {
            this.user_limit = user_limit;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCatalog_id() {
            return catalog_id;
        }

        public void setCatalog_id(String catalog_id) {
            this.catalog_id = catalog_id;
        }

        public int getUser_total() {
            return user_total;
        }

        public void setUser_total(int user_total) {
            this.user_total = user_total;
        }
    }
}
