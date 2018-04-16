package dingshi.com.hibook.bean;

import java.io.Serializable;

/**
 * @author wangqi
 * @since 2017/12/12 15:32
 */

public class BookDetails extends Result {


    /**
     * jsonData : {"book_number":"0","isbn":9787559609519,"isbn10":7559609511,"origin_name":"Pilules Bleues","spare_isbn":0,"name":"蓝色小药丸","author":"[瑞士] 弗雷德里克·佩特斯","translator":"陈帅,易立","press":"后浪丨北京联合出版公司","publish_time":"2017-11","summary":"该书的介绍 ","price":45,"page_number":208,"douban_grade":9.1,"grade":0,"grade_number":0,"douban_grade_number":353,"cover":"https://img3.doubanio.com/mpic/s29559634.jpg"}
     */

    private JsonDataBean jsonData;

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         * book_number : 0
         * isbn : 9787559609519
         * isbn10 : 7559609511
         * origin_name : Pilules Bleues
         * spare_isbn : 0
         * name : 蓝色小药丸
         * author : [瑞士] 弗雷德里克·佩特斯
         * translator : 陈帅,易立
         * press : 后浪丨北京联合出版公司
         * publish_time : 2017-11
         * summary : 该书的介绍   简介
         * price : 45
         * page_number : 208    总页数
         * douban_grade : 9.1   豆瓣评分
         * grade : 0            评分
         * grade_number : 0     评分人数
         * douban_grade_number : 353  豆瓣评分人数
         * cover : https://img3.doubanio.com/mpic/s29559634.jpg
         * <p>
         * available   1、可借  0、不可借
         * is_borrow   是否已被借出去   0.否,1.是
         */
        int is_borrow;
        int available;
        private String book_number;
        private String isbn;
        private String isbn10;
        private String origin_name;
        private String spare_isbn;
        private String name;
        private String author;
        private String translator;
        private String press;
        private String publish_time;
        private String summary;
        private String price;
        private String page_number;
        private float douban_grade;
        private float grade;
        private String grade_number;
        private String douban_grade_number;
        private String cover;

        public int getIs_borrow() {
            return is_borrow;
        }

        public void setIs_borrow(int is_borrow) {
            this.is_borrow = is_borrow;
        }

        public int getAvailable() {
            return available;
        }

        public void setAvailable(int available) {
            this.available = available;
        }

        public String getBook_number() {
            return book_number;
        }

        public void setBook_number(String book_number) {
            this.book_number = book_number;
        }

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

        public String getOrigin_name() {
            return origin_name;
        }

        public void setOrigin_name(String origin_name) {
            this.origin_name = origin_name;
        }

        public String getSpare_isbn() {
            return spare_isbn;
        }

        public void setSpare_isbn(String spare_isbn) {
            this.spare_isbn = spare_isbn;
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

        public String getTranslator() {
            return translator;
        }

        public void setTranslator(String translator) {
            this.translator = translator;
        }

        public String getPress() {
            return press;
        }

        public void setPress(String press) {
            this.press = press;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPage_number() {
            return page_number;
        }

        public void setPage_number(String page_number) {
            this.page_number = page_number;
        }

        public float getDouban_grade() {
            return douban_grade;
        }

        public void setDouban_grade(float douban_grade) {
            this.douban_grade = douban_grade;
        }

        public float getGrade() {
            return grade;
        }

        public void setGrade(float grade) {
            this.grade = grade;
        }

        public String getGrade_number() {
            return grade_number;
        }

        public void setGrade_number(String grade_number) {
            this.grade_number = grade_number;
        }

        public String getDouban_grade_number() {
            return douban_grade_number;
        }

        public void setDouban_grade_number(String douban_grade_number) {
            this.douban_grade_number = douban_grade_number;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        @Override
        public String toString() {
            return "JsonDataBean{" +
                    "book_number='" + book_number + '\'' +
                    ", isbn='" + isbn + '\'' +
                    ", isbn10='" + isbn10 + '\'' +
                    ", origin_name='" + origin_name + '\'' +
                    ", spare_isbn='" + spare_isbn + '\'' +
                    ", name='" + name + '\'' +
                    ", author='" + author + '\'' +
                    ", translator='" + translator + '\'' +
                    ", press='" + press + '\'' +
                    ", publish_time='" + publish_time + '\'' +
                    ", summary='" + summary + '\'' +
                    ", price='" + price + '\'' +
                    ", page_number='" + page_number + '\'' +
                    ", douban_grade='" + douban_grade + '\'' +
                    ", grade='" + grade + '\'' +
                    ", grade_number='" + grade_number + '\'' +
                    ", douban_grade_number='" + douban_grade_number + '\'' +
                    ", cover='" + cover + '\'' +
                    '}';
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "BookDetails{" +
                "jsonData=" + jsonData +
                '}';
    }
}
