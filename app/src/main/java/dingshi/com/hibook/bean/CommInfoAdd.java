package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/12/13 10:49
 */

public class CommInfoAdd extends Result {

    /**
     * jsonData : {"id":3,"comment_id":1,"book_isbn":"9787559803351","user_id":"491822919633080325","content":"只是看看能不能成功111111"}
     */

    private CommentInfo.JsonDataBean jsonData;

    public CommentInfo.JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(CommentInfo.JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

}
