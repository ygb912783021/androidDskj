package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

public class EbookGratis extends Result {
    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         *  "book_id": 7,
         "isbn": "",
         "origin_name": "The Swift Programming Language 中文版",
         "name": "Swift 开发中文版",
         "author": "Apple",
         "translator": "Swift中文翻译组",
         "press": "极客学院",
         "price": 0,
         "page_number": "30",
         "grade": 0,
         "grade_number": 0,
         "publish_time": "2016-9-23",
         "summary": "Swift，苹果于2014年WWDC（苹果开发者大会）发布的新开发语言，可与Objective-C*共同运行于Mac OS和iOS平台，用于搭建基于苹果平台的应用程序。",
         "cover": "http://testapi.linkbooker.com/files/ebooks/d1df47e38ebf7789334ac8cc40e7f146.jpg",
         "file_name": "http://testapi.linkbooker.com/files/ebooks/c7108e841be94c46bdc61c1ca17e2595.pdf"
         */
        int book_id;
        String isbn;
        String origin_name;
        String name;
        String author;
        String translator;
        String press;
        int price;
        String page_number;
        int grade;
        int grade_number;
        String publish_time;
        String summary;
        String cover;
        String  file_name;

        public int getBook_id() {
            return book_id;
        }

        public void setBook_id(int book_id) {
            this.book_id = book_id;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getOrigin_name() {
            return origin_name;
        }

        public void setOrigin_name(String origin_name) {
            this.origin_name = origin_name;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getPage_number() {
            return page_number;
        }

        public void setPage_number(String page_number) {
            this.page_number = page_number;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getGrade_number() {
            return grade_number;
        }

        public void setGrade_number(int grade_number) {
            this.grade_number = grade_number;
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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        @Override
        public String toString() {
            return "JsonDataBean{" +
                    "book_id=" + book_id +
                    ", isbn='" + isbn + '\'' +
                    ", origin_name='" + origin_name + '\'' +
                    ", name='" + name + '\'' +
                    ", author='" + author + '\'' +
                    ", translator='" + translator + '\'' +
                    ", press='" + press + '\'' +
                    ", price=" + price +
                    ", page_number='" + page_number + '\'' +
                    ", grade=" + grade +
                    ", grade_number=" + grade_number +
                    ", publish_time='" + publish_time + '\'' +
                    ", summary='" + summary + '\'' +
                    ", cover='" + cover + '\'' +
                    ", file_name='" + file_name + '\'' +
                    '}';
        }
    }
}
