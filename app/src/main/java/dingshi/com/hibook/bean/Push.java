package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2018/1/29 13:33
 */

public class Push  {

    /**
     * title : 文章标题
     * content : 正文内容
     * payload : {"type":"1","source_id":"su120010","source_url":"0"}
     */

    private String title;
    private String content;
    private PayloadBean payload;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class PayloadBean {
        /**
         * type : 1
         * source_id : su120010
         * source_url : 0
         */

        private String type;
        private String source_id;
        private String source_url;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
    }
}
