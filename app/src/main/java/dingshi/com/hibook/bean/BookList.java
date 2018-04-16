package dingshi.com.hibook.bean;

import java.util.List;

/**
 * @author wangqi
 * @since 2017/12/15 16:32
 */

public class BookList extends Result {


    public List<BookDetails.JsonDataBean> jsonData;

    public List<BookDetails.JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<BookDetails.JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

}
