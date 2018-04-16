package dingshi.com.hibook.bean;

import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/12 15:36
 * <p>
 * 图书共读者数据
 */

public class BookTalent extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * user_id : 510698779723173897
         * nick_name : 137****7422
         * avatar :
         */

        private String user_id;
        private String nick_name;
        private String avatar;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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
    }
}
