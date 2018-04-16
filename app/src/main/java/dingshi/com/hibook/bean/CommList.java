package dingshi.com.hibook.bean;

import java.util.List;

/**
 * @author wangqi
 * @since 2018/1/11 18:31
 */

public class CommList extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * comment_total : 1
         * praise_total : 0
         * content : 123131
         * created_at : 2018-01-08 15:25:53
         */

        private int comment_total;
        private int praise_total;
        private String content;
        private String created_at;
        private BookBean book;

        public int getComment_total() {
            return comment_total;
        }

        public void setComment_total(int comment_total) {
            this.comment_total = comment_total;
        }

        public int getPraise_total() {
            return praise_total;
        }

        public void setPraise_total(int praise_total) {
            this.praise_total = praise_total;
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

        public BookBean getBook() {
            return book;
        }

        public void setBook(BookBean book) {
            this.book = book;
        }

        public static class BookBean {
            /**
             * isbn : 9787559609519
             * isbn10 : 7559609511
             * name : 蓝色小药丸
             * author : [瑞士] 弗雷德里克·佩特斯
             * grade : 0
             * cover : https://img3.doubanio.com/mpic/s29559634.jpg
             */

            private String isbn;
            private String isbn10;
            private String name;
            private String author;
            private int grade;
            private String cover;

            public String getIsbn() {
                return isbn;
            }

            public void setIsbn(String isbn) {
                this.isbn = isbn;
            }

            public String getIsbn10() {
                return isbn10;
            }

            public void setIsbn10(String isbn10) {
                this.isbn10 = isbn10;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }
        }
    }
}
