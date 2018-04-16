package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/12 15:59
 */

public class CommentGrade extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable{
        /**
         * id : 1    评论id
         * score : 5   评分
         * praise : 0   点赞数量
         * book_isbn : 9787559803351
         * user_id : 491822919633080325
         * content : 可以好好读的一本书
         * created_at : 2017-12-11 10:12:00
         * nick_name : 137****7421
         * avatar : http://testapi.linkbooker.com/files/avatar/eb95c2947f3340e11ea3844de85a56ac.jpg
         */

        private String id;
        private float score;
        private int praise;
        private String book_isbn;
        private String user_id;
        private String content;
        private String created_at;
        private String nick_name;
        private String avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public String getBook_isbn() {
            return book_isbn;
        }

        public void setBook_isbn(String book_isbn) {
            this.book_isbn = book_isbn;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        @Override
        public String toString() {
            return "JsonDataBean{" +
                    "id='" + id + '\'' +
                    ", score=" + score +
                    ", praise=" + praise +
                    ", book_isbn='" + book_isbn + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", content='" + content + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", nick_name='" + nick_name + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CommentGrade{" +
                "jsonData=" + jsonData +
                '}';
    }
}
