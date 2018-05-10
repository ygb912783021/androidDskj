package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

public class EbookGroup extends Result {
    private List<JsonData> jsonData ;

    public List<JsonData> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonData> jsonData) {
        this.jsonData = jsonData;
    }

    public class JsonData implements Serializable {
        private String user_id;

        private String tag;

        private int id;

        private String ebook_id;

        public void setUser_id(String user_id){
            this.user_id = user_id;
        }
        public String getUser_id(){
            return this.user_id;
        }
        public void setTag(String tag){
            this.tag = tag;
        }
        public String getTag(){
            return this.tag;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setEbook_id(String ebook_id){
            this.ebook_id = ebook_id;
        }
        public String getEbook_id(){
            return this.ebook_id;
        }

        @Override
        public String toString() {
            return "JsonData{" +
                    "user_id='" + user_id + '\'' +
                    ", tag='" + tag + '\'' +
                    ", id=" + id +
                    ", ebook_id='" + ebook_id + '\'' +
                    '}';
        }
    }
}
