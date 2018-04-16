package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/12 16:04
 */

public class CommentInfo extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable{
        /**
         * id : 2
         * comment_id : 0
         * book_isbn : 9787559803351
         * user_id : 491822919633080325
         * content : 只是看看能不能成功
         * created_at : 2017-12-11 10:12:00
         * nick_name : 137****7421
         * praise : 0
         * avatar : http://testapi.linkbooker.com/files/avatar/eb95c2947f3340e11ea3844de85a56ac.jpg
         * comment_total : 2
         * comment_cascade : [{"id":4,"comment_id":2,"book_isbn":"9787559803351","user_id":"491822919633080325","nick_name":"137****7421","praise":0,"avatar":"http://testapi.linkbooker.com/files/avatar/eb95c2947f3340e11ea3844de85a56ac.jpg","content":"11111功111111","created_at":"2017-12-11 10:12:00"}]
         */

        private String id;
        private String comment_id;
        private String book_isbn;
        private String user_id;
        private String content;
        private String created_at;
        private String nick_name;
        private int praise;
        private String avatar;
        private int comment_total;
        private List<CommentCascadeBean> comment_cascade;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
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

        public int getPraise() {
            return praise;
        }

        public void setPraise(int praise) {
            this.praise = praise;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getComment_total() {
            return comment_total;
        }

        public void setComment_total(int comment_total) {
            this.comment_total = comment_total;
        }

        public List<CommentCascadeBean> getComment_cascade() {
            return comment_cascade;
        }

        public void setComment_cascade(List<CommentCascadeBean> comment_cascade) {
            this.comment_cascade = comment_cascade;
        }

        public static class CommentCascadeBean implements Serializable{
            /**
             * id : 4
             * comment_id : 2
             * book_isbn : 9787559803351
             * user_id : 491822919633080325
             * nick_name : 137****7421
             * praise : 0
             * avatar : http://testapi.linkbooker.com/files/avatar/eb95c2947f3340e11ea3844de85a56ac.jpg
             * content : 11111功111111
             * created_at : 2017-12-11 10:12:00
             */

            private String id;
            private String comment_id;
            private String book_isbn;
            private String user_id;
            private String nick_name;
            private int praise;
            private String avatar;
            private String content;
            private String created_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getComment_id() {
                return comment_id;
            }

            public void setComment_id(String comment_id) {
                this.comment_id = comment_id;
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

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public int getPraise() {
                return praise;
            }

            public void setPraise(int praise) {
                this.praise = praise;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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


            @Override
            public String toString() {
                return "CommentCascadeBean{" +
                        "id='" + id + '\'' +
                        ", comment_id='" + comment_id + '\'' +
                        ", book_isbn='" + book_isbn + '\'' +
                        ", user_id='" + user_id + '\'' +
                        ", nick_name='" + nick_name + '\'' +
                        ", praise=" + praise +
                        ", avatar='" + avatar + '\'' +
                        ", content='" + content + '\'' +
                        ", created_at='" + created_at + '\'' +
                        '}';
            }
        }
    }
}
