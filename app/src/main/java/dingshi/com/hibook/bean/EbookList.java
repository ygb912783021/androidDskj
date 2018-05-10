package dingshi.com.hibook.bean;

import java.io.Serializable;

public class EbookList {
    private int book_id;

    private String isbn;

    private String origin_name;

    private String name;

    private String author;

    private String translator;

    private String press;

    private int price;

    private String page_number;

    private int grade;

    private int grade_number;

    private String publish_time;

    private String summary;

    private String cover;

    private String file_name;

    private int through;

    private String percentage_reading;

    private String read_page_num;

    public void setBook_id(int book_id){
        this.book_id = book_id;
    }
    public int getBook_id(){
        return this.book_id;
    }
    public void setIsbn(String isbn){
        this.isbn = isbn;
    }
    public String getIsbn(){
        return this.isbn;
    }
    public void setOrigin_name(String origin_name){
        this.origin_name = origin_name;
    }
    public String getOrigin_name(){
        return this.origin_name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setTranslator(String translator){
        this.translator = translator;
    }
    public String getTranslator(){
        return this.translator;
    }
    public void setPress(String press){
        this.press = press;
    }
    public String getPress(){
        return this.press;
    }
    public void setPrice(int price){
        this.price = price;
    }
    public int getPrice(){
        return this.price;
    }
    public void setPage_number(String page_number){
        this.page_number = page_number;
    }
    public String getPage_number(){
        return this.page_number;
    }
    public void setGrade(int grade){
        this.grade = grade;
    }
    public int getGrade(){
        return this.grade;
    }
    public void setGrade_number(int grade_number){
        this.grade_number = grade_number;
    }
    public int getGrade_number(){
        return this.grade_number;
    }
    public void setPublish_time(String publish_time){
        this.publish_time = publish_time;
    }
    public String getPublish_time(){
        return this.publish_time;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }
    public String getSummary(){
        return this.summary;
    }
    public void setCover(String cover){
        this.cover = cover;
    }
    public String getCover(){
        return this.cover;
    }
    public void setFile_name(String file_name){
        this.file_name = file_name;
    }
    public String getFile_name(){
        return this.file_name;
    }
    public void setThrough(int through){
        this.through = through;
    }
    public int getThrough(){
        return this.through;
    }
    public void setPercentage_reading(String percentage_reading){
        this.percentage_reading = percentage_reading;
    }
    public String getPercentage_reading(){
        return this.percentage_reading;
    }
    public void setRead_page_num(String read_page_num){
        this.read_page_num = read_page_num;
    }
    public String getRead_page_num(){
        return this.read_page_num;
    }

}
