package dingshi.com.hibook.bean;

import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/11 17:17
 */

public class Home extends Result {


    /**
     * jsonData : {"carousel":[{"title":"","file":"http://www.qqzhi.com/images/timg.jpg","chaining":""},{"title":"","file":"http://img1.qunarzz.com/sight/p23/201211/01/71ec8e13656438bb93835fbb.jpg_710x360_b162b67e.jpg","chaining":""},{"title":"","file":"http://d01.res.meilishuo.net/pic/_o/bf/e2/f893a6c6c5409035ed9fbd95e1eb_1800_1194.jpg","chaining":""}],"mid_banner":[{"title":"","file":"http://www.qqzhi.com/images/timg.jpg","chaining":""},{"title":"","file":"http://img1.qunarzz.com/sight/p23/201211/01/71ec8e13656438bb93835fbb.jpg_710x360_b162b67e.jpg","chaining":""},{"title":"","file":"http://d01.res.meilishuo.net/pic/_o/bf/e2/f893a6c6c5409035ed9fbd95e1eb_1800_1194.jpg","chaining":""}],"headline":[{"article_id":2,"title":"他是骨灰级宅男，爱画秃顶老头和舞女","source_url":"https://book.douban.com/review/8951731/"},{"article_id":1,"title":"胡德夫：大地恍神的孩子","source_url":"https://www.douban.com/note/647045601/"}],"nearby_cases":[{"radius":1,"name":"上南中学书柜","serial_number":"su120012","address":"上南中学A栋教学楼","lng":121.531359,"lat":31.188807}],"concern_books":[{"isbn":9787559609519,"isbn10":7559609511,"name":"蓝色小药丸","grade":0,"cover":"https://img3.doubanio.com/mpic/s29559634.jpg"},{"isbn":9787544290678,"isbn10":7544290670,"name":"南方高速","grade":0,"cover":"https://img3.doubanio.com/mpic/s29529746.jpg"},{"isbn":9787544756426,"isbn10":7544756424,"name":"仿生人会梦见电子羊吗？","grade":0,"cover":"https://img3.doubanio.com/mpic/s29578434.jpg"},{"isbn":9787535499639,"isbn10":7535499635,"name":"吃瓜时代的儿女们","grade":0,"cover":"https://img3.doubanio.com/mpic/s29590893.jpg"},{"isbn":9787559606228,"isbn10":7559606229,"name":"好好工作","grade":0,"cover":"https://img3.doubanio.com/mpic/s29508490.jpg"},{"isbn":9787210096177,"isbn10":7210096175,"name":"当自然赋予科技灵感","grade":0,"cover":"https://img3.doubanio.com/mpic/s29513795.jpg"},{"isbn":9787213083426,"isbn10":7213083422,"name":"创造自然","grade":0,"cover":"https://img3.doubanio.com/mpic/s29549031.jpg"},{"isbn":9787533949716,"isbn10":7533949714,"name":"孩子们的诗","grade":0,"cover":"https://img3.doubanio.com/mpic/s29551042.jpg"},{"isbn":9787559605924,"isbn10":7559605923,"name":"这里","grade":0,"cover":"https://img1.doubanio.com/mpic/s29512259.jpg"}],"sell_well_books":[{"isbn":9787544769839,"isbn10":7544769836,"name":"早上九点叫醒我","grade":0,"cover":"https://img1.doubanio.com/mpic/s29607939.jpg"},{"isbn":9787532774456,"isbn10":7532774457,"name":"情感教育","grade":0,"cover":"https://img3.doubanio.com/mpic/s29587843.jpg"},{"isbn":9787562498544,"isbn10":7562498547,"name":"克苏鲁神话合集","grade":0,"cover":"https://img3.doubanio.com/mpic/s29590771.jpg"},{"isbn":9787544769440,"isbn10":7544769445,"name":"时间的噪音","grade":0,"cover":"https://img1.doubanio.com/mpic/s29597557.jpg"},{"isbn":9787536084469,"isbn10":7536084463,"name":"遥远的向日葵地","grade":0,"cover":"https://img3.doubanio.com/mpic/s29593553.jpg"},{"isbn":9787506098267,"isbn10":7506098261,"name":"在孟溪那边","grade":0,"cover":"https://img3.doubanio.com/mpic/s29595424.jpg"},{"isbn":9787531324577,"isbn10":0,"name":"小猪唏哩呼噜","grade":0,"cover":"https://img3.doubanio.com/lpic/s5652432.jpg"},{"isbn":9787532741984,"isbn10":0,"name":"夏洛的网","grade":0,"cover":"https://img3.doubanio.com/lpic/s2508410.jpg"},{"isbn":9787108059703,"isbn10":7108059703,"name":"巴黎浪漫吗？","grade":0,"cover":"https://img1.doubanio.com/mpic/s29548999.jpg"}],"recommend_books":[{"isbn":"9787513314992","isbn10":"","name":"sadfasd","press":"上海译文出版社","publish_time":"2010-10","cover":"https://img3.doubanio.com/mpic/s29535271.jpg"}]}
     */

    private JsonDataBean jsonData;

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        private List<CarouselBean> carousel;
        private List<CarouselBean> mid_banner;
        private List<HeadlineBean> headline;
        private List<NearbyCasesBean> nearby_cases;
        private List<ConcernBooksBean> concern_books;
        private List<SellWellBooksBean> sell_well_books;
        private List<RecommendBooksBean> recommend_books;

        public List<CarouselBean> getCarousel() {
            return carousel;
        }

        public void setCarousel(List<CarouselBean> carousel) {
            this.carousel = carousel;
        }

        public List<CarouselBean> getMid_banner() {
            return mid_banner;
        }

        public void setMid_banner(List<CarouselBean> mid_banner) {
            this.mid_banner = mid_banner;
        }

        public List<HeadlineBean> getHeadline() {
            return headline;
        }

        public void setHeadline(List<HeadlineBean> headline) {
            this.headline = headline;
        }

        public List<NearbyCasesBean> getNearby_cases() {
            return nearby_cases;
        }

        public void setNearby_cases(List<NearbyCasesBean> nearby_cases) {
            this.nearby_cases = nearby_cases;
        }

        public List<ConcernBooksBean> getConcern_books() {
            return concern_books;
        }

        public void setConcern_books(List<ConcernBooksBean> concern_books) {
            this.concern_books = concern_books;
        }

        public List<SellWellBooksBean> getSell_well_books() {
            return sell_well_books;
        }

        public void setSell_well_books(List<SellWellBooksBean> sell_well_books) {
            this.sell_well_books = sell_well_books;
        }

        public List<RecommendBooksBean> getRecommend_books() {
            return recommend_books;
        }

        public void setRecommend_books(List<RecommendBooksBean> recommend_books) {
            this.recommend_books = recommend_books;
        }

        public static class CarouselBean {
            /**
             * title :
             * file : http://www.qqzhi.com/images/timg.jpg
             * chaining :
             * share_link : http://www.baidu.com
             */

            private String title;
            private String file;
            private String chaining;
            private String share_link;

            public String getShare_link() {
                return share_link;
            }

            public void setShare_link(String share_link) {
                this.share_link = share_link;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFile() {
                return file;
            }

            public void setFile(String file) {
                this.file = file;
            }

            public String getChaining() {
                return chaining;
            }

            public void setChaining(String chaining) {
                this.chaining = chaining;
            }


            @Override
            public String toString() {
                return "CarouselBean{" +
                        "title='" + title + '\'' +
                        ", file='" + file + '\'' +
                        ", chaining='" + chaining + '\'' +
                        '}';
            }
        }


        public static class HeadlineBean {
            /**
             * article_id : 2
             * title : 他是骨灰级宅男，爱画秃顶老头和舞女
             * source_url : https://book.douban.com/review/8951731/
             */

            private int article_id;
            private String title;
            private String source_url;

            public int getArticle_id() {
                return article_id;
            }

            public void setArticle_id(int article_id) {
                this.article_id = article_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSource_url() {
                return source_url;
            }

            public void setSource_url(String source_url) {
                this.source_url = source_url;
            }
        }

        public static class NearbyCasesBean {
            /**
             * radius : 1
             * name : 上南中学书柜
             * serial_number : su120012
             * address : 上南中学A栋教学楼
             * lng : 121.531359
             * lat : 31.188807
             */

            private String radius;
            private String name;
            private String serial_number;
            private String address;
            private String lng;
            private String lat;

            public String getRadius() {
                return radius;
            }

            public void setRadius(String radius) {
                this.radius = radius;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSerial_number() {
                return serial_number;
            }

            public void setSerial_number(String serial_number) {
                this.serial_number = serial_number;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }




        }

        public static class ConcernBooksBean {
            /**
             * isbn : 9787559609519
             * isbn10 : 7559609511
             * name : 蓝色小药丸
             * grade : 0
             * cover : https://img3.doubanio.com/mpic/s29559634.jpg
             */

            private String isbn;
            private String isbn10;
            private String name;
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

        public static class SellWellBooksBean {
            /**
             * isbn : 9787544769839
             * isbn10 : 7544769836
             * name : 早上九点叫醒我
             * grade : 0
             * cover : https://img1.doubanio.com/mpic/s29607939.jpg
             */

            private String isbn;
            private String isbn10;
            private String name;
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

        public static class RecommendBooksBean {
            /**
             * isbn : 9787513314992
             * isbn10 :
             * name : sadfasd
             * press : 上海译文出版社
             * publish_time : 2010-10
             * cover : https://img3.doubanio.com/mpic/s29535271.jpg
             * author : 作者
             */

            private String isbn;
            private String isbn10;
            private String name;
            private String press;
            private String publish_time;
            private String cover;
            private String author;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }
        }
    }
}
