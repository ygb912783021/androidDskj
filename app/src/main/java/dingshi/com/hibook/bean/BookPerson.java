package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangqi
 * @since 2018/1/2 13:58
 */

public class BookPerson extends Result{


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable{
        /**
         * user_id : 510698779723173897
         * nick_name : 137****7422
         * read_num : 1
         * have_num : 1
         * avatar :
         */

        private String user_id;
        private String nick_name;
        private int read_num;
        private int have_num;
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

        public int getRead_num() {
            return read_num;
        }

        public void setRead_num(int read_num) {
            this.read_num = read_num;
        }

        public int getHave_num() {
            return have_num;
        }

        public void setHave_num(int have_num) {
            this.have_num = have_num;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
