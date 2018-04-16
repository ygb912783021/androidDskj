package dingshi.com.hibook.bean;

import java.util.List;

import dingshi.com.hibook.share.PayWeixin;

/**
 * @author wangqi
 * @since 2017/12/14 14:15
 */

public class Payment extends Result {

    private PayWeixin jsonData;

    public PayWeixin getJsonData() {
        return jsonData;
    }

    public void setJsonData(PayWeixin jsonData) {
        this.jsonData = jsonData;
    }

}
