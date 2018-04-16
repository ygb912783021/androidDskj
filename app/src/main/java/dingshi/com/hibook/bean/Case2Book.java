package dingshi.com.hibook.bean;

import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/15 15:43
 */

public class Case2Book extends Result{

    /**
     * jsonData : {"name":"上南中学书柜","serial_number":"su120012","image":"","amount":60,"address":"上南中学A栋教学楼","put_on_at":"2017-10-31 15:49:53","lat":31.188807,"lng":121.531359,"cells":[{"cell_id":66,"user_id":0,"aisle_number":44,"book_isbn":9787544290678,"price":1,"status":1,"book_name":"南方高速","book_cover":"https://img3.doubanio.com/mpic/s29529746.jpg","book_author":"[阿根廷] 胡利奥·科塔萨尔","book_press":"南海出版公司","book_publish_time":"2017-10"}]}
     */

    private JsonDataBean jsonData;

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * name : 上南中学书柜
         * serial_number : su120012
         * image :
         * amount : 60
         * address : 上南中学A栋教学楼
         * put_on_at : 2017-10-31 15:49:53
         * lat : 31.188807
         * lng : 121.531359
         * cells : [{"cell_id":66,"user_id":0,"aisle_number":44,"book_isbn":9787544290678,"price":1,"status":1,"book_name":"南方高速","book_cover":"https://img3.doubanio.com/mpic/s29529746.jpg","book_author":"[阿根廷] 胡利奥·科塔萨尔","book_press":"南海出版公司","book_publish_time":"2017-10"}]
         */

        private String name;
        private String serial_number;
        private String image;
        private int amount;
        private String address;
        private String put_on_at;
        private double lat;
        private double lng;
        private List<CellsBean> cells;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPut_on_at() {
            return put_on_at;
        }

        public void setPut_on_at(String put_on_at) {
            this.put_on_at = put_on_at;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public List<CellsBean> getCells() {
            return cells;
        }

        public void setCells(List<CellsBean> cells) {
            this.cells = cells;
        }

        public static class CellsBean {
            /**
             * cell_id : 66
             * user_id : 0
             * aisle_number : 44
             * book_isbn : 9787544290678
             * price : 1
             * status : 1  1、表示可借
             * book_name : 南方高速
             * book_cover : https://img3.doubanio.com/mpic/s29529746.jpg
             * book_author : [阿根廷] 胡利奥·科塔萨尔
             * book_press : 南海出版公司
             * book_publish_time : 2017-10
             */

            private String cell_id;
            private String user_id;
            private String aisle_number;
            private String book_isbn;
            private String price;
            private int status;
            private String book_name;
            private String book_cover;
            private String book_author;
            private String book_press;
            private String book_publish_time;

            public String getCell_id() {
                return cell_id;
            }

            public void setCell_id(String cell_id) {
                this.cell_id = cell_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getAisle_number() {
                return aisle_number;
            }

            public void setAisle_number(String aisle_number) {
                this.aisle_number = aisle_number;
            }

            public String getBook_isbn() {
                return book_isbn;
            }

            public void setBook_isbn(String book_isbn) {
                this.book_isbn = book_isbn;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getBook_name() {
                return book_name;
            }

            public void setBook_name(String book_name) {
                this.book_name = book_name;
            }

            public String getBook_cover() {
                return book_cover;
            }

            public void setBook_cover(String book_cover) {
                this.book_cover = book_cover;
            }

            public String getBook_author() {
                return book_author;
            }

            public void setBook_author(String book_author) {
                this.book_author = book_author;
            }

            public String getBook_press() {
                return book_press;
            }

            public void setBook_press(String book_press) {
                this.book_press = book_press;
            }

            public String getBook_publish_time() {
                return book_publish_time;
            }

            public void setBook_publish_time(String book_publish_time) {
                this.book_publish_time = book_publish_time;
            }
        }
    }
}
