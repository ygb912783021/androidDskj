package dingshi.com.hibook.bean.lib;

import java.util.List;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/25 17:37
 */

public class LibHome extends Result {


    /**
     * jsonData : {"my_catalog":[{"catalog_id":1,"name":"123","user_total":0,"icon":"http://testapi.linkbooker.com/files/catalog/c976c3c7e78bfc66751e0921ac66d789_add.jpg","created_at":"2017-12-25"},{"catalog_id":2,"name":"www","user_total":0,"icon":"http://testapi.linkbooker.com/files/catalog/c976c3c7e78bfc66751e0921ac66d789_add.jpg","created_at":"2017-12-25"},{"catalog_id":4,"name":"wangqi","user_total":0,"icon":"http://testapi.linkbooker.com/files/catalog/c976c3c7e78bfc66751e0921ac66d789_add.jpg","created_at":"2017-12-25"},{"catalog_id":8,"name":"luguo","user_total":1,"icon":"http://testapi.linkbooker.com/files/catalog/14d8de87457735abed6644c281992bc8_add.jpg","created_at":"2017-12-25"},{"catalog_id":9,"name":"name","user_total":1,"icon":"http://testapi.linkbooker.com/files/catalog/f97dedc8c8ca38d27dd41f90ac4a3375_add.jpg","created_at":"2017-12-25"},{"catalog_id":10,"name":"112","user_total":1,"icon":"http://testapi.linkbooker.com/files/catalog/44521ecf27360399818cbc0bb126ddad_add.jpg","created_at":"2017-12-25"},{"catalog_id":11,"name":"11","user_total":1,"icon":"http://testapi.linkbooker.com/files/catalog/16ee0d2770a71a55e7be69071c2e00b2_add.jpg","created_at":"2017-12-25"}],"join_catalog":[{"catalog_id":11,"name":"11","user_total":1,"icon":"http://testapi.linkbooker.com/files/catalog/16ee0d2770a71a55e7be69071c2e00b2_add.jpg","created_at":"2017-12-25"}]}
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
         * 我创建的图书馆
         */
        private List<MyCatalogBean> my_catalog;

        /**
         * 我创建的书友会
         */
        private List<MyCatalogBean> my_rally;

        /**
         * 我加入的图书目录
         */
        private List<MyCatalogBean> join_catalog;
        /**
         * 我加入的书友会
         */
        private List<MyCatalogBean> join_rally;


        public List<MyCatalogBean> getMy_rally() {
            return my_rally;
        }

        public void setMy_rally(List<MyCatalogBean> my_rally) {
            this.my_rally = my_rally;
        }

        public List<MyCatalogBean> getJoin_rally() {
            return join_rally;
        }

        public void setJoin_rally(List<MyCatalogBean> join_rally) {
            this.join_rally = join_rally;
        }

        public List<MyCatalogBean> getMy_catalog() {
            return my_catalog;
        }

        public void setMy_catalog(List<MyCatalogBean> my_catalog) {
            this.my_catalog = my_catalog;
        }

        public List<MyCatalogBean> getJoin_catalog() {
            return join_catalog;
        }

        public void setJoin_catalog(List<MyCatalogBean> join_catalog) {
            this.join_catalog = join_catalog;
        }

        public static class MyCatalogBean {
            /**
             * catalog_id : 1
             * name : 123
             * user_total : 0
             * icon : http://testapi.linkbooker.com/files/catalog/c976c3c7e78bfc66751e0921ac66d789_add.jpg
             * created_at : 2017-12-25
             * type
             */
            private String catalog_id;
            private String name;
            private int user_total;
            private String icon;
            private String created_at;
            private int type;
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getCatalog_id() {
                return catalog_id;
            }

            public void setCatalog_id(String catalog_id) {
                this.catalog_id = catalog_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getUser_total() {
                return user_total;
            }

            public void setUser_total(int user_total) {
                this.user_total = user_total;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }


            @Override
            public String toString() {
                return "MyCatalogBean{" +
                        "catalog_id=" + catalog_id +
                        ", name='" + name + '\'' +
                        ", user_total=" + user_total +
                        ", icon='" + icon + '\'' +
                        ", created_at='" + created_at + '\'' +
                        '}';
            }
        }


    }
}
