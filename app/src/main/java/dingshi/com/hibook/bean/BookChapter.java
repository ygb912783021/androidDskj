package dingshi.com.hibook.bean;

import java.io.Serializable;
import java.util.List;

public class BookChapter extends Result {
    private List<JsonData> jsonData;

    public List<JsonData> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonData> jsonData) {
        this.jsonData = jsonData;
    }

    public class JsonData implements Serializable {
        private int id;
        private String title;
        private int ebook_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getEbook_id() {
            return ebook_id;
        }

        public void setEbook_id(int ebook_id) {
            this.ebook_id = ebook_id;
        }
    }
}
