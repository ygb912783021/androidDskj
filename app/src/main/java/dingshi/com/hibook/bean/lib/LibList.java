package dingshi.com.hibook.bean.lib;

import java.io.Serializable;
import java.util.List;

import dingshi.com.hibook.bean.Result;

/**
 * @author wangqi
 * @since 2017/12/25 13:31
 */

public class LibList extends Result {

    private List<JsonDataBean> jsonData;

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean implements Serializable {
        /**
         * type : 1  目录类别
         * name : 123   名称
         * is_check : 1  加入审核设置,1.不审核，自动加入,2.加入需要我的审核,3.只查看，禁止任何人加入
         * description : 123   目录描述
         * is_encrypt : 1     目录手机号加密方式
         * icon : http://testapi.linkbooker.com/files/catalog/c976c3c7e78bfc66751e0921ac66d789_add.jpg
         * is_open : 1    公开到广场
         * is_consult : 1  允许用户咨询,1：允许 2：不允许，默认为1
         * is_allow : 1    图书馆允许加入 0：不允许加入，1：允许加入
         * message_state : 1   通知提醒设置 1:加入后消息提醒
         * fake_user : 700   虚拟人数
         * user_limit : 100   人数上限
         * created_at : 2017-12-25  创建日期
         * catalog_id : 1      目录id
         * user_total : 0       人数
         * id :   目录编号
         * user_id 用户编号
         * is_del 目录状态：0：已删除 1：正常
         * updated_at 更新时间
         * is_join  加入状态 0：已申请 1：已加入 2：已拒绝 3：已退出 4：已被踢 5：从未加入
         * book_sum  图书数量
         * book_share_sum   共享图书数量
         * <p>
         * apply_president : 是否允许申请成为会长/馆长,1.是,0.否
         * is_apply_president :  是否已经申请会长/馆长,1.是,0.否
         * have_president  : 是否已有会长/馆长,1.是,0.否
         */
        private int book_share_sum;
        private int book_sum;
        private String id;
        private String user_id;
        private int is_del;
        private String updated_at;
        private int is_join;
        private int type;
        private String name;
        private int is_check;
        private String describe;
        private int is_encrypt;
        private String icon;
        private int is_open;
        private int is_consult;
        private int is_allow;
        private int message_state;
        private String fake_user;
        private String user_limit;
        private String created_at;
        private String catalog_id;
        private int user_total;

        private int apply_president;
        private int is_apply_president;
        private int have_president;

        public int getApply_president() {
            return apply_president;
        }

        public void setApply_president(int apply_president) {
            this.apply_president = apply_president;
        }

        public int getIs_apply_president() {
            return is_apply_president;
        }

        public void setIs_apply_president(int is_apply_president) {
            this.is_apply_president = is_apply_president;
        }

        public int getHave_president() {
            return have_president;
        }

        public void setHave_president(int have_president) {
            this.have_president = have_president;
        }

        public int getBook_share_sum() {
            return book_share_sum;
        }

        public void setBook_share_sum(int book_share_sum) {
            this.book_share_sum = book_share_sum;
        }

        public int getBook_sum() {
            return book_sum;
        }

        public void setBook_sum(int book_sum) {
            this.book_sum = book_sum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getIs_join() {
            return is_join;
        }

        public void setIs_join(int is_join) {
            this.is_join = is_join;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getIs_encrypt() {
            return is_encrypt;
        }

        public void setIs_encrypt(int is_encrypt) {
            this.is_encrypt = is_encrypt;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getIs_open() {
            return is_open;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public int getIs_consult() {
            return is_consult;
        }

        public void setIs_consult(int is_consult) {
            this.is_consult = is_consult;
        }

        public int getIs_allow() {
            return is_allow;
        }

        public void setIs_allow(int is_allow) {
            this.is_allow = is_allow;
        }

        public int getMessage_state() {
            return message_state;
        }

        public void setMessage_state(int message_state) {
            this.message_state = message_state;
        }

        public String getFake_user() {
            return fake_user;
        }

        public void setFake_user(String fake_user) {
            this.fake_user = fake_user;
        }

        public String getUser_limit() {
            return user_limit;
        }

        public void setUser_limit(String user_limit) {
            this.user_limit = user_limit;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCatalog_id() {
            return catalog_id;
        }

        public void setCatalog_id(String catalog_id) {
            this.catalog_id = catalog_id;
        }

        public int getUser_total() {
            return user_total;
        }

        public void setUser_total(int user_total) {
            this.user_total = user_total;
        }

        @Override
        public String toString() {
            return "JsonDataBean{" +
                    "type=" + type +
                    ", name='" + name + '\'' +
                    ", is_check=" + is_check +
                    ", description='" + describe + '\'' +
                    ", is_encrypt=" + is_encrypt +
                    ", icon='" + icon + '\'' +
                    ", is_open=" + is_open +
                    ", is_consult=" + is_consult +
                    ", is_allow=" + is_allow +
                    ", message_state=" + message_state +
                    ", fake_user=" + fake_user +
                    ", user_limit=" + user_limit +
                    ", created_at='" + created_at + '\'' +
                    ", catalog_id=" + catalog_id +
                    ", user_total=" + user_total +
                    '}';
        }
    }
}
