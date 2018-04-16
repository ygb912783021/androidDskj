package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/12/18 16:00
 */

public class Book {
    private String isbn;
    /**
     * 非必要
     */
    private String isbn10;
    /**
     * 非必要,图书原名
     */

    private String origin_name;

    /**
     * 图书图片
     */
    private String cover;
    /**
     * 图书
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 非必要，译者
     */
    private String translator;

    /**
     * 非必要，出版社
     */
    private String press;
    /**
     * 非必要，出版时间
     */
    private String publish_time;
    /**
     * 非必要，简介
     */
    private String summary;
    /**
     * 必要，价格
     */
    private String price;

    /**
     * 非必要，总页数
     */
    private String page_number;
    /**
     * 非必要，豆瓣评分
     */
    private String douban_grade;
    /**
     * 非必要，豆瓣评分人数
     */
    private String douban_grade_number;
    /**
     * 必要，图书分类
     */
    private String book_type_id;


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDouban_grade() {
        return douban_grade;
    }

    public void setDouban_grade(String douban_grade) {
        this.douban_grade = douban_grade;
    }

    public String getDouban_grade_number() {
        return douban_grade_number;
    }

    public void setDouban_grade_number(String douban_grade_number) {
        this.douban_grade_number = douban_grade_number;
    }

    public String getBook_type_id() {
        return book_type_id;
    }

    public void setBook_type_id(String book_type_id) {
        this.book_type_id = book_type_id;
    }
}
