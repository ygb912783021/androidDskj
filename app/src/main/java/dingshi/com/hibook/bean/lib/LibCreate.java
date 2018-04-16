package dingshi.com.hibook.bean.lib;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/25 11:53
 */

public class LibCreate extends Result {


    private LibList.JsonDataBean jsonData;

    public LibList.JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(LibList.JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

}
