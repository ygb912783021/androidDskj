package dingshi.com.hibook.bean;

import java.util.List;

/**
 * @author wangqi
 * @since 2018/1/29 09:47
 */

public class Notice extends Result {


    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * id : 2  通知编号
         * type : 6  通知类型,1.图书，2.书柜，3.图书馆，4.个人名片，5.个人中心消息，6.文本信息,7.打开外部页面
         * content : asdfasdfasdf   内容
         * img : http://testapi.linkbooker.com/files/information/cc1510b3b4a7572d209b77e96411b300.jpg  img
         * source_id : 0  应用内跳转id,type为1=>ISBN,2=>serial_number,3=>catalog_id,4.user_id
         * source_url : null
         * created_at : 2018-01-12 11:53:35
         */

        private int id;
        private int type;
        private String content;
        private String img;
        private String source_id;
        private String source_url;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getSource_id() {
            return source_id;
        }

        public void setSource_id(String source_id) {
            this.source_id = source_id;
        }

        public String getSource_url() {
            return source_url;
        }

        public void setSource_url(String source_url) {
            this.source_url = source_url;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
