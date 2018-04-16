package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2017/12/13 10:49
 */

public class CommGradeAdd extends Result {
    /**
     * jsonData : {"id":3,"score":2,"book_isbn":"9787559803351","user_id":"491822919633080325","content":"只是看看能不能成功111111"}
     */

    private CommentGrade.JsonDataBean jsonData;

    public CommentGrade.JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(CommentGrade.JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }


}
