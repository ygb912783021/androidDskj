package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/12/18 12:39
 */

public class Zxing extends Result {
    private JsonDataBean jsonData;

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * book_number : 0
         * isbn : 9787020132003
         * isbn10 : 7020132006
         * origin_name :
         * spare_isbn : 0
         * name : 离开的，留下的
         * author : [意] 埃莱娜·费兰特
         * translator : 陈英
         * press : 人民文学出版社
         * publish_time : 2017-11
         * summary : 两个女人 50年的友谊……
         * price : 62
         * page_number : 492
         * douban_grade : 9.3
         * grade : 0
         * grade_number : 0
         * douban_grade_number : 421
         * cover : https://img3.doubanio.com/mpic/s29535271.jpg
         */

        private String book_number;
        private String isbn;
        private String isbn10;
        private String origin_name;
        private int spare_isbn;
        private String name;
        private String author;
        private String translator;
        private String press;
        private String publish_time;
        private String summary;
        private String price;
        private String page_number;
        private double douban_grade;
        private double grade;
        private String grade_number;
        private int douban_grade_number;
        private String cover;

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

        public int getSpare_isbn() {
            return spare_isbn;
        }

        public void setSpare_isbn(int spare_isbn) {
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

        public double getDouban_grade() {
            return douban_grade;
        }

        public void setDouban_grade(double douban_grade) {
            this.douban_grade = douban_grade;
        }

        public double getGrade() {
            return grade;
        }

        public void setGrade(double grade) {
            this.grade = grade;
        }

        public String getGrade_number() {
            return grade_number;
        }

        public void setGrade_number(String grade_number) {
            this.grade_number = grade_number;
        }

        public int getDouban_grade_number() {
            return douban_grade_number;
        }

        public void setDouban_grade_number(int douban_grade_number) {
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
                    ", spare_isbn=" + spare_isbn +
                    ", name='" + name + '\'' +
                    ", author='" + author + '\'' +
                    ", translator='" + translator + '\'' +
                    ", press='" + press + '\'' +
                    ", publish_time='" + publish_time + '\'' +
                    ", summary='" + summary + '\'' +
                    ", price='" + price + '\'' +
                    ", page_number='" + page_number + '\'' +
                    ", douban_grade=" + douban_grade +
                    ", grade=" + grade +
                    ", grade_number='" + grade_number + '\'' +
                    ", douban_grade_number=" + douban_grade_number +
                    ", cover='" + cover + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Zxing{" +
                "jsonData=" + jsonData +
                '}';
    }
}
