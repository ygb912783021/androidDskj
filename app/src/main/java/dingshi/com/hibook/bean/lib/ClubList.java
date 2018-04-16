package dingshi.com.hibook.bean.lib;

import java.util.List;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2018/1/31 13:37
 */

public class ClubList extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * type : 6
         * total : 2930
         * type_name : 城市书友会
         * data : [{"id":9,"name":"北京大学书友会","person_num":10,"created_at":"2018-01-25 09:32:00","icon":""}]
         */

        private int type;
        private String total;
        private String type_name;  //城市书友会
        private List<DataBean> data;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 9
             * name : 北京大学书友会
             * person_num : 10
             * created_at : 2018-01-25 09:32:00
             * icon :
             */
            private String catalog_id;
            private int id;
            private String name;
            private String person_num;
            private String created_at;
            private String icon;


            public String getCatalog_id() {
                return catalog_id;
            }

            public void setCatalog_id(String catalog_id) {
                this.catalog_id = catalog_id;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPerson_num() {
                return person_num;
            }

            public void setPerson_num(String person_num) {
                this.person_num = person_num;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}
