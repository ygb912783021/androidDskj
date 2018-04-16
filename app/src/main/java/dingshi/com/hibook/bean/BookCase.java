package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/12 15:35
 * <p>
 * 书柜详情
 */

public class BookCase extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         * radius : 1
         * name : 上南中学书柜
         * image : 书柜图片
         * serial_number : su120012
         * address : 上南中学A栋教学楼
         * lng : 121.531359
         * lat : 31.188807
         * book_num  藏书
         * sub_num   预约
         */
        private int sub_num;
        private int book_num;
        private String radius;
        private String name;
        private String image;
        private String serial_number;
        private String address;
        private String lng;
        private String lat;

        public int getSub_num() {
            return sub_num;
        }

        public void setSub_num(int sub_num) {
            this.sub_num = sub_num;
        }

        public int getBook_num() {
            return book_num;
        }

        public void setBook_num(int book_num) {
            this.book_num = book_num;
        }

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        @Override
        public String toString() {
            return "JsonDataBean{" +
                    "radius='" + radius + '\'' +
                    ", name='" + name + '\'' +
                    ", image='" + image + '\'' +
                    ", serial_number='" + serial_number + '\'' +
                    ", address='" + address + '\'' +
                    ", lng='" + lng + '\'' +
                    ", lat='" + lat + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BookCase{" +
                "jsonData=" + jsonData +
                '}';
    }
}
