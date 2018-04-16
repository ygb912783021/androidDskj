package dingshi.com.hibook.bean;

import java.util.List;

import dingshi.com.hibook.bean.lib.ClubList;

/**
 * @author wangqi
 * @since 2018/2/1 14:02
 */

public class RallySearch extends Result {

    List<ClubList.JsonDataBean.DataBean> jsonData;


    public List<ClubList.JsonDataBean.DataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<ClubList.JsonDataBean.DataBean> jsonData) {
        this.jsonData = jsonData;
    }
}
