package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2018/1/2 10:40
 */

public class OrderDetails extends Result {
    Order.JsonDataBean jsonData;

    public Order.JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(Order.JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }
}
